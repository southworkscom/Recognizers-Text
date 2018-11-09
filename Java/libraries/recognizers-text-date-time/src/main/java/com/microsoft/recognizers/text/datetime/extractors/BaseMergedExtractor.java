package com.microsoft.recognizers.text.datetime.extractors;

import com.microsoft.recognizers.text.ExtractResult;
import com.microsoft.recognizers.text.datetime.Constants;
import com.microsoft.recognizers.text.datetime.DateTimeOptions;
import com.microsoft.recognizers.text.datetime.extractors.config.IMergedExtractorConfiguration;
import com.microsoft.recognizers.text.datetime.extractors.config.ProcessedSuperfluousWords;
import com.microsoft.recognizers.text.datetime.extractors.config.ResultIndex;
import com.microsoft.recognizers.text.datetime.utilities.MatchingUtil;
import com.microsoft.recognizers.text.datetime.utilities.Token;
import com.microsoft.recognizers.text.matcher.MatchResult;
import com.microsoft.recognizers.text.utilities.Match;
import com.microsoft.recognizers.text.utilities.MatchGroup;
import com.microsoft.recognizers.text.utilities.RegExpUtility;
import com.microsoft.recognizers.text.utilities.StringUtility;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseMergedExtractor implements IDateTimeExtractor {

    private final IMergedExtractorConfiguration config;

    @Override
    public String getExtractorName() {
        return "";
    }

    public BaseMergedExtractor(IMergedExtractorConfiguration config) {
        this.config = config;
    }

    @Override
    public List<ExtractResult> extract(String input, LocalDateTime reference) {
        List<ExtractResult> ret = new ArrayList<>();
        String originInput = input;
        Iterable<MatchResult<String>> superfluousWordMatches = null;
        if(this.config.getOptions() != null && this.config.getOptions() != DateTimeOptions.EnablePreview)
        {
            ProcessedSuperfluousWords processedSuperfluousWords = MatchingUtil.PreProcessTextRemoveSuperfluousWords(input, this.config.getSuperfluousWordMatcher());
            input = processedSuperfluousWords.text;
            superfluousWordMatches = processedSuperfluousWords.superfluousWordMatches;
        }
        // The order is important, since there is a problem in merging
        AddTo(ret, this.config.getDateExtractor().extract(input, reference), input);
        AddTo(ret, this.config.getTimeExtractor().extract(input, reference), input);
        AddTo(ret, this.config.getDurationExtractor().extract(input, reference), input);
        AddTo(ret, this.config.getDatePeriodExtractor().extract(input, reference), input);
        AddTo(ret, this.config.getDateTimeExtractor().extract(input, reference), input);
        AddTo(ret, this.config.getTimePeriodExtractor().extract(input, reference), input);
        AddTo(ret, this.config.getSetExtractor().extract(input, reference), input);
        AddTo(ret, this.config.getHolidayExtractor().extract(input, reference), input);

        if (this.config.getOptions() != null && this.config.getOptions() != DateTimeOptions.EnablePreview)
        {
            AddTo(ret, this.config.getTimeZoneExtractor().extract(input, reference), input);
            ret = this.config.getTimeZoneExtractor().removeAmbiguousTimezone(ret);
        }

        // This should be at the end since if need the extractor to determine the previous text contains time or not
        AddTo(ret, NumberEndingRegexMatch(input, ret), input);

        // modify time entity to an alternative DateTime expression if it follows a DateTime entity
        if (this.config.getOptions() != null && this.config.getOptions() != DateTimeOptions.ExtendedTypes)
        {
            ret = this.config.getDateTimeAltExtractor().extract(ret, input, reference);
        }

        ret = FilterUnspecificDatePeriod(ret, input);
        AddMod(ret, input);

        // filtering
        if (this.config.getOptions() != null && this.config.getOptions() != DateTimeOptions.CalendarMode)
        {
            CheckCalendarFilterList(ret, input);
        }

        Collections.sort(ret);

        if (this.config.getOptions() != null && this.config.getOptions() != DateTimeOptions.EnablePreview)
        {
            ret = MatchingUtil.PosProcessExtractionRecoverSuperfluousWords(ret, superfluousWordMatches, originInput);
        }

        return ret;
    }

    @Override
    public List<ExtractResult> extract(String input) {
        return this.extract(input, LocalDateTime.now());
    }

    private void AddTo(List<ExtractResult> dst, List<ExtractResult> src, String text)
    {
        for (ExtractResult result: src)
        {
            if (config.getOptions() != null && config.getOptions() != DateTimeOptions.SkipFromToMerge)
            {
                if (ShouldSkipFromToMerge(result)) continue;
            }

            boolean isFound = false ;
            ArrayList<Integer> overlapIndexes = new ArrayList<>();
            int firstIndex = -1;
            for (int i = 0; i < dst.size(); i++)
            {
                if (dst.get(i).IsOverlap(result))
                {
                    isFound = true;
                    if (dst.get(i).IsCover(result))
                    {
                        if (firstIndex == -1) overlapIndexes.add(i);
                    }
                    else
                    {
                        break;
                    }
                }
            }

            if (!isFound) dst.add(result);
            else if (overlapIndexes.size() > 0)
            {
                List<ExtractResult> tempDst = new ArrayList<>();
                for (int i = 0; i < dst.size(); i++)
                {
                    if (!overlapIndexes.contains(i)) tempDst.add(dst.get(i));
                }

                // insert at the first overlap occurrence to keep the order
                tempDst.add(firstIndex, result);
                dst.clear();
                dst.addAll(tempDst);
            }
        }
    }

    private boolean ShouldSkipFromToMerge(ExtractResult er)
    {
        return Arrays.stream(RegExpUtility.getMatches(config.getFromToRegex(), er.text)).findFirst().isPresent();
    }

    private List<ExtractResult> NumberEndingRegexMatch(String text, List<ExtractResult> extractResults)
    {
        List<Token> tokens = new ArrayList<>();

        for (ExtractResult extractResult: extractResults)
        {
            if (extractResult.type.equals(Constants.SYS_DATETIME_TIME) || extractResult.type.equals(Constants.SYS_DATETIME_DATETIME))
            {
                String stringAfter = text.substring(extractResult.start + extractResult.length);
                Pattern numberEndingPattern = this.config.getNumberEndingPattern();
                Optional<Match> match = Arrays.stream(RegExpUtility.getMatches(numberEndingPattern, text)).findFirst();
                if (match.isPresent())
                {
                    MatchGroup newTime = match.get().getGroup("newTime");
                    List<ExtractResult> numRes = this.config.getIntegerExtractor().extract(newTime.toString());
                    if (numRes.size() > 0) continue;

                    int startPosition = extractResult.start + extractResult.length + newTime.index;
                    tokens.add(new Token(startPosition, startPosition + newTime.length));
                }

            }
        }

        return  Token.mergeAllTokens(tokens, text, Constants.SYS_DATETIME_TIME);
    }

    private List<ExtractResult> FilterUnspecificDatePeriod(List<ExtractResult> ers, String text)
    {
        List<ExtractResult> newResults = new ArrayList<>();
        for (ExtractResult extractResult: ers)
        {
            if (extractResult.text.matches(this.config.getUnspecificDatePeriodRegex().pattern()))
            {
                newResults.add(extractResult);
            }
        }
        return newResults;
    }

    private void AddMod(List<ExtractResult> ers, String text)
    {
        int lastEnd = 0;
        for (ExtractResult er : ers)
        {
            String beforeStr = text.substring(lastEnd, er.start ).toLowerCase(Locale.ROOT);

            ResultIndex resultIndex = HasTokenIndex(beforeStr.replaceAll("\\s+$", ""), config.getBeforeRegex());
            if (resultIndex.result)
            {
                int modLenght = beforeStr.length() - resultIndex.index;
                er = er.withLength(er.length + modLenght);
                er = er.withStart(er.start - modLenght);
                er = er.withText(er.text.substring(er.start, er.start + er.length));
            }

            resultIndex = HasTokenIndex(beforeStr.replaceAll("\\s+$", ""), config.getAfterRegex());
            if (resultIndex.result)
            {
                int modLenght = beforeStr.length() - resultIndex.index;
                er = er.withLength(er.length + modLenght);
                er = er.withStart(er.start - modLenght);
                er = er.withText(er.text.substring(er.start, er.start + er.length));
            }

            resultIndex = HasTokenIndex(beforeStr.replaceAll("\\s+$", ""), config.getSinceRegex());
            if (resultIndex.result)
            {
                int modLenght = beforeStr.length() - resultIndex.index;
                er = er.withLength(er.length + modLenght);
                er = er.withStart(er.start - modLenght);
                er = er.withText(er.text.substring(er.start, er.start + er.length));
            }

            if (er.type.equals(Constants.SYS_DATETIME_DATEPERIOD))
            {
                // 2012 or after/above
                String afterStr = text.substring(er.start + er.length).toLowerCase(Locale.ROOT);

                Optional<Match> match = Arrays.stream(RegExpUtility.getMatches(config.getYearAfterRegex(), text)).findFirst();
                if (match.isPresent() && match.get().index == 0 && match.get().length == afterStr.replaceAll("^\\s+", "").length())
                {
                    int modLenght = match.get().length + afterStr.indexOf(match.get().value);
                    er = er.withLength(er.length + modLenght);
                    er = er.withText(er.text.substring(er.start, er.start + er.length));
                }
            }
        }
    }

    private ResultIndex HasTokenIndex(String text, Pattern pattern)
    {
        ResultIndex resultIndex = new ResultIndex(false, -1);

        Matcher regexMatcher = pattern.matcher(text);
        while (regexMatcher.find())
        {
            if (StringUtility.isNullOrWhiteSpace(text.substring(regexMatcher.start(), regexMatcher.start() + regexMatcher.end())))
            {
                resultIndex = new ResultIndex(true  , regexMatcher.start());
                return resultIndex;
            }
            // Support cases has two or more specific tokens
            // For example, "show me sales after 2010 and before 2018 or before 2000"
            // When extract "before 2000", we need the second "before" which will be matched in the second Regex match
        }

        return resultIndex;
    }

    private void CheckCalendarFilterList(List<ExtractResult> ers, String text)
    {
        List<ExtractResult> shallowCopy = ers.subList(0, ers.size());
        Collections.reverse(shallowCopy);
        for (ExtractResult er : shallowCopy)
        {
            for (Pattern negRegex : this.config.getFilterWordRegexList())
            {
                Matcher regexMatcher = negRegex.matcher(er.text);
                if (regexMatcher.find()) ers.remove(er);
            }
        }
    }
}

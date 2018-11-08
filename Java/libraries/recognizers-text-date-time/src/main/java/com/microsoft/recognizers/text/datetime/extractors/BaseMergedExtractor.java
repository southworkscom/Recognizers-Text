package com.microsoft.recognizers.text.datetime.extractors;

import com.google.common.collect.Iterators;
import com.microsoft.recognizers.text.ExtractResult;
import com.microsoft.recognizers.text.datetime.Constants;
import com.microsoft.recognizers.text.datetime.DateTimeOptions;
import com.microsoft.recognizers.text.datetime.extractors.config.IMergedExtractorConfiguration;
import com.microsoft.recognizers.text.datetime.extractors.config.ProcessedSuperfluousWords;
import com.microsoft.recognizers.text.datetime.utilities.MatchingUtil;
import com.microsoft.recognizers.text.datetime.utilities.Token;
import com.microsoft.recognizers.text.matcher.MatchResult;
import com.microsoft.recognizers.text.utilities.Match;
import com.microsoft.recognizers.text.utilities.MatchGroup;
import com.microsoft.recognizers.text.utilities.RegExpUtility;

import java.time.LocalDateTime;
import java.util.*;
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
}

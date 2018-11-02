package com.microsoft.recognizers.text.datetime.extractors;

import com.microsoft.recognizers.text.ExtractResult;
import com.microsoft.recognizers.text.datetime.Constants;
import com.microsoft.recognizers.text.datetime.DateTimeOptions;
import com.microsoft.recognizers.text.datetime.extractors.config.IDateTimeExtractorConfiguration;
import com.microsoft.recognizers.text.datetime.utilities.Token;
import com.microsoft.recognizers.text.utilities.RegExpUtility;
import com.microsoft.recognizers.text.utilities.Match;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

public class BaseDateTimeExtractor implements IDateTimeExtractor {

    private final IDateTimeExtractorConfiguration config;

    @Override
    public String getExtractorName() {
        return Constants.SYS_DATETIME_DATETIME;
    }

    public BaseDateTimeExtractor(IDateTimeExtractorConfiguration config) {
        this.config = config;
    }

    @Override
    public List<ExtractResult> extract(String input, LocalDateTime reference) {
        List<Token> tokens = new ArrayList<>();

        tokens.addAll(MergeDateAndTime(input, reference));
        tokens.addAll(BasicRegexMatch(input));
        tokens.addAll(TimeOfTodayBefore(input, reference));
        tokens.addAll(TimeOfTodayAfter(input, reference));
        tokens.addAll(SpecialTimeOfDate(input, reference));
        tokens.addAll(DurationWithBeforeAndAfter(input, reference));
        tokens.addAll(SpecialTimeOfDay(input, reference));

        return Token.mergeAllTokens(tokens, input, getExtractorName());
    }

    @Override
    public List<ExtractResult> extract(String input) {
        return this.extract(input, LocalDateTime.now());
    }

    // Parses a specific time of today, tonight, this afternoon, like "seven this afternoon"
    public List<Token> TimeOfTodayAfter(String input, LocalDateTime reference) {
        List<Token> ret = new ArrayList<>();

        List<ExtractResult> ers = this.config.getTimePointExtractor().extract(input, reference);

        for(ExtractResult er : ers){
            var afterStr = input.substring(er.Start + er.Length ?? 0);
            if (string.IsNullOrEmpty(afterStr)){
                continue; //@here
            }

            var match = this.config.TimeOfTodayAfterRegex.Match(afterStr);
            if (match.Success){
                var begin = er.Start ?? 0;
                var end = (er.Start ?? 0) + (er.Length ?? 0) + match.Length;
                ret.Add(new Token(begin, end));
            }
        }

        var matches = this.config.SimpleTimeOfTodayAfterRegex.Matches(input);
        foreach (Match match in matches){
            ret.Add(new Token(match.Index, match.Index + match.Length));
        }

        return ret;
    }

    public List<Token> TimeOfTodayBefore(String input, LocalDateTime reference){
        List<Token> ret = new ArrayList<>();
        List<ExtractResult> ers = this.config.getTimePointExtractor().extract(input, reference);
        for (ExtractResult er : ers){
            int erStart = er.getStart();
            String beforeStr = input.substring(0, erStart == null ? 0 : erStart);

            // handle "this morningh at 7am"
            Match innerMatch = Arrays.stream(RegExpUtility.getMatches(this.config.getTimeOfDayRegex(), er.Text)).findFirst().get();
            if (innerMatch != null && innerMatch.index == 0){
                int erStart = er.getStart();
                beforeStr = input.substring(0, (erStart == null ? 0 : erStart) + innerMatch.length);
            }

            if (beforeStr == ""){
                continue;
            }
            
            Match match = Arrays.stream(RegExpUtility.getMatches(this.config.getTimeOfTodayBeforeRegex(), er.Text)).findFirst().get();
            if (match != null){
                int begin = match.Index;
                int end = er.start + er.length;
                ret.add(new Token(begin, end));
            }
        }

        Match[] matches = RegExpUtility.getMatches(this.config.getSimpleTimeOfTodayBeforeRegex(), input);
        for(Match match : matches){
            ret.add(new Token(match.index, match.index + match.length));
        }

        return ret;
    }
    // Match "now"
    public List<Token> BasicRegexMatch(String input){
        List<Token> ret = new ArrayList<>();
        input = input.trim().toLowerCase();

        // Handle "now"
        Match[] matches = RegExpUtility.getMatches(this.config.getNowRegex(), input);
        
        for (Match match : matches){
            ret.add(new Token(match.index, match.index + match.length));
        }

        return ret;
    }
    // Merge a Date entity and a Time entity, like "at 7 tomorrow"
    public List<Token> MergeDateAndTime(String input, LocalDateTime reference) {
        String SYS_NUM_INTEGER = com.microsoft.recognizers.text.number.Constants.SYS_NUM;
        List<Token> ret = new ArrayList<>();
        List<ExtractResult> dateErs = this.config.getDatePointExtractor().extract(input, reference);
        if (dateErs.size() == 0){
            return ret;
        }
        
        List<ExtractResult> timeErs = this.config.getTimePointExtractor().extract(input, reference);
        Match[] timeNumMatches = RegExpUtility.getMatches(config.getNumberAsTimeRegex(), input);

        for (Match match : timeNumMatches) {
            ret.add(new Token(match.index, match.index + match.length));
        }
        if (timeErs.size() == 0 && timeNumMatches.length == 0){
            return ret;
        }

        List<ExtractResult> ers = dateErs;
        ers.addAll(timeErs);

        // handle cases which use numbers as time points
        // only enabled in CalendarMode
        if (this.config.getOptions() == DateTimeOptions.CalendarMode){
            List<ExtractResult> numErs = new ArrayList<>();
            for (Match timeNumMatch : timeNumMatches){
                ExtractResult node = new ExtractResult(timeNumMatch.index, timeNumMatch.length, timeNumMatch.value, SYS_NUM_INTEGER);
                numErs.add(node);
            }
            ers.addAll(numErs);
        }

        Collections.sort(ers, (ExtractResult ExtractResult1, ExtractResult ExtractResult2)-> (ExtractResult1.start < ExtractResult2.start) ? 1 : -1 );
        int i = 0;
        while (i < ers.size() - 1){
            int j = i + 1;
            /* REIMPLEMENT IsOverlap */
            while (j < ers.size() && ers.get(i).IsOverlap(ers.get(j))){
                j++;
            }
            /* REIMPLEMENT IsOverlap */
            if (j >= ers.size()){
                break;
            }

            if (ers.get(i).type == Constants.SYS_DATETIME_DATE && ers.get(j).type == Constants.SYS_DATETIME_TIME ||
                ers.get(i).type == Constants.SYS_DATETIME_TIME && ers.get(j).type == Constants.SYS_DATETIME_DATE ||
                ers.get(i).type == Constants.SYS_DATETIME_DATE && ers.get(j).type == SYS_NUM_INTEGER){
                int middleBegin = (ers.get(i).start != null && ers.get(i).length != null) ? ers.get(i).start + ers.get(i).length : 0;
                int middleEnd = ers.get(j).start != null ? ers.get(j).start : 0;
                if (middleBegin > middleEnd){
                    i = j + 1;
                    continue;
                }

                String middleStr = input.substring(middleBegin, middleEnd - middleBegin).trim().toLowerCase();
                boolean valid = false;
                // for cases like "tomorrow 3",  "tomorrow at 3"
                if (ers.get(j).type == SYS_NUM_INTEGER){
                    Optional<Match> matches = Arrays.stream(RegExpUtility.getMatches(this.config.getDateNumberConnectorRegex(), input)).findFirst();
                    if ((middleStr == null || middleStr == "") || matches.isPresent()){
                        valid = true;
                    }
                } else {
                    if (this.config.IsConnector(middleStr)){
                        valid = true;
                    }
                }

                if (valid){
                    int begin = ers.get(i).start != null ? ers.get(i).start : 0;
                    int end = (ers.get(i).start != null && ers.get(i).length != null) ? ers.get(i).start + ers.get(i).length : 0;
                    ret.add(new Token(begin, end));
                }

                i = j + 1;
                continue;
            }
            i = j;
        }

        // Handle "in the afternoon" at the end of entity
        for (int idx = 0; idx < ret.size(); idx++){
            String afterStr = input.substring(ret.get(idx).getEnd());
            Optional<Match> match = Arrays.stream(RegExpUtility.getMatches(this.config.getSuffixRegex(), afterStr)).findFirst();
            if (match.isPresent()){
                ret.set(idx, new Token(ret.get(idx).getStart(), ret.get(idx).getEnd() + match.get().length));
            }
        }

        // Handle "day" prefixes
        for (int idx = 0; idx < ret.size(); idx++){
            String beforeStr = input.substring(0, ret.get(idx).getStart());            
            Optional<Match> match = Arrays.stream(RegExpUtility.getMatches(this.config.getUtilityConfiguration().getCommonDatePrefixRegex(), beforeStr)).findFirst();
            if (match.isPresent()){
                ret.set(idx, new Token(ret.get(idx).getStart() - match.get().length, ret.get(idx).getEnd()));
            }
        }

        return ret;
    }
    private boolean isOverlap(ExtractResult extract1, ExtractResult extract2){
        boolean result = false;
        //if(extract1.)
        return result;
    }
}
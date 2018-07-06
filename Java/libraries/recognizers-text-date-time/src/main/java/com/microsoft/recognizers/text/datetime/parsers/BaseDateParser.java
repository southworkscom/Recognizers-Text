package com.microsoft.recognizers.text.datetime.parsers;

import com.google.common.collect.ImmutableMap;
import com.microsoft.recognizers.text.ParseResult;
import com.microsoft.recognizers.text.ExtractResult;
import com.microsoft.recognizers.text.datetime.Constants;
import com.microsoft.recognizers.text.datetime.TimeTypeConstants;
import com.microsoft.recognizers.text.datetime.parsers.config.IDateParserConfiguration;
import com.microsoft.recognizers.text.datetime.utilities.DateTimeResolutionResult;
import com.microsoft.recognizers.text.datetime.utilities.FormatUtil;
import com.microsoft.recognizers.text.utilities.Match;
import com.microsoft.recognizers.text.utilities.RegExpUtility;
import com.microsoft.recognizers.text.utilities.StringUtility;
import jdk.vm.ci.meta.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class BaseDateParser implements IDateTimeParser {

    private final IDateParserConfiguration config;

    public BaseDateParser(IDateParserConfiguration config) {
        this.config = config;
    }

    @Override
    public ParseResult parse(ExtractResult extResult) {
        return parse(extResult, LocalDateTime.now());
    }

    @Override
    public String getParserName() {
        return Constants.SYS_DATETIME_DATE;
    }

    @Override
    public DateTimeParseResult parse(ExtractResult er, LocalDateTime reference) {

        LocalDateTime referenceDate = reference;

        Object value = null;

        if (er.type.equals(getParserName())) {
            DateTimeResolutionResult innerResult = this.parseBasicRegexMatch(er.text, referenceDate);

//            if (!innerResult.getSuccess()) {
//                innerResult = this.parseImplicitDate(er.text, referenceDate);
//            }
//
//            if (!innerResult.getSuccess())
//            {
//                innerResult = this.parseWeekdayOfMonth(er.text, referenceDate);
//            }
//
//            if (!innerResult.getSuccess())
//            {
//                innerResult = this.parseDurationWithAgoAndLater(er.text, referenceDate);
//            }
//
//            // NumberWithMonth must be the second last one, because it only need to find a number and a month to get a "success"
//            if (!innerResult.getSuccess())
//            {
//                innerResult = this.parseNumberWithMonth(er.text, referenceDate);
//            }
//
//            // SingleNumber last one
//            if (!innerResult.getSuccess())
//            {
//                innerResult = this.parseSingleNumber(er.text, referenceDate);
//            }
//
//            if (innerResult.getSuccess())
//            {
//                innerResult.futureResolution = new ImmutableMap<String, String>
//                {
//                    {TimeTypeConstants.DATE, FormatUtil.FormatDate((DateObject) innerResult.getFutureValue()}
//                };
//
//                innerResult.PastResolution = new ImmutableMap<String, String>
//                {
//                    {TimeTypeConstants.DATE, FormatUtil.FormatDate((DateObject) innerResult.getPastValue())}
//                };
//
//                value = innerResult;
//            }
        }

        DateTimeParseResult ret = new DateTimeParseResult(
                er.start,
                er.length,
                er.text,
                er.type,
                er.data,
                value,
                "",
                value == null ? "" : ((DateTimeResolutionResult)value).getTimex());

        return ret;
    }

    @Override
    public List<DateTimeParseResult> filterResults(String query, List<DateTimeParseResult> candidateResults) {
        return candidateResults;
    }

    // parse basic patterns in DateRegexList
    private DateTimeResolutionResult parseBasicRegexMatch(String text, LocalDateTime referenceDate)
    {
        String trimedText = text.trim();

        for (Pattern regex : this.config.getDateRegexes()) {
            Integer offset = 0;
            Optional<Match> match = Arrays.stream(RegExpUtility.getMatches(regex, trimedText)).findFirst();

            if (!match.isPresent() || match.get().index != 0) {
                match =  Arrays.stream(RegExpUtility.getMatches(regex, this.config.getDateTokenPrefix() + trimedText)).findFirst();
                offset = this.config.getDateTokenPrefix().length();
            }

            if (match.isPresent() && match.get().index == offset && match.get().length == trimedText.length())
            {
                // LUIS value string will be set in Match2Date method
                DateTimeResolutionResult ret = this.match2Date(match, referenceDate);
                return ret;
            }
        }

        return new DateTimeResolutionResult();
    }

    // parse a regex match which includes 'day', 'month' and 'year' (optional) group
    private DateTimeResolutionResult match2Date(Optional<Match> match, LocalDateTime referenceDate)
    {
        DateTimeResolutionResult ret = new DateTimeResolutionResult();

        String monthStr = match.get().getGroup("month").value.toLowerCase();
        String dayStr = match.get().getGroup("day").value.toLowerCase();
        String yearStr = match.get().getGroup("year").value.toLowerCase();
        Integer month = 0, day = 0, year = 0;

        if (this.config.getMonthOfYear().containsKey(monthStr) && this.config.getDayOfMonth().containsKey(dayStr))
        {
            month = this.config.getMonthOfYear().get(monthStr);
            day = this.config.getDayOfMonth().get(dayStr);
            if (!StringUtility.isNullOrEmpty(yearStr))
            {
                year = Integer.parseInt(yearStr);
                if (year < 100 && year >= Constants.MinTwoDigitYearPastNum)
                {
                    year += 1900;
                }
                else if (year >= 0 && year < Constants.MaxTwoDigitYearFutureNum)
                {
                    year += 2000;
                }
            }
        }

        Boolean noYear = false;
        if (year == 0)
        {
            year = referenceDate.getYear();
            ret.setTimex(FormatUtil.LuisDate(-1, month, day));
            noYear = true;
        }
        else
        {
            ret.setTimex(FormatUtil.LuisDate(year, month, day));
        }


        // todo:review
        LocalDateTime futureDate = LocalDateTime.of(year, month, day, 0, 0);//DateObject.MinValue.SafeCreateFromValue(year, month, day);
        LocalDateTime pastDate = LocalDateTime.of(year, month, day, 0, 0);//DateObject.MinValue.SafeCreateFromValue(year, month, day);

        if (noYear && futureDate.isBefore(referenceDate))
        {
            futureDate = futureDate.plusYears(1);
        }

        if (noYear && (pastDate.isEqual(referenceDate) || pastDate.isAfter(referenceDate)))
        {
            pastDate = pastDate.minusYears(1);
        }

        ret.setFutureValue(futureDate);
        ret.setPastValue(pastDate);
        ret.setSuccess(true);

        return ret;
    }
}
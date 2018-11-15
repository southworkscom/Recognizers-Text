package com.microsoft.recognizers.text.datetime.parsers;

import com.google.common.collect.ImmutableMap;
import com.microsoft.recognizers.text.ExtractResult;
import com.microsoft.recognizers.text.ParseResult;
import com.microsoft.recognizers.text.datetime.Constants;
import com.microsoft.recognizers.text.datetime.DatePeriodTimexType;
import com.microsoft.recognizers.text.datetime.TimeTypeConstants;
import com.microsoft.recognizers.text.datetime.extractors.BaseDateExtractor;
import com.microsoft.recognizers.text.datetime.parsers.config.IDatePeriodParserConfiguration;
import com.microsoft.recognizers.text.datetime.utilities.*;
import com.microsoft.recognizers.text.utilities.Match;
import com.microsoft.recognizers.text.utilities.MatchGroup;
import com.microsoft.recognizers.text.utilities.RegExpUtility;
import com.microsoft.recognizers.text.utilities.StringUtility;
import org.javatuples.Pair;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class BaseDatePeriodParser implements IDateTimeParser{

    private static final String parserName = Constants.SYS_DATETIME_DATEPERIOD; //"DatePeriod";
    private static boolean inclusiveEndPeriod = false;

    private final IDatePeriodParserConfiguration config;

    public BaseDatePeriodParser(IDatePeriodParserConfiguration config) {
        this.config = config;
    }

    @Override
    public String getParserName() {
        return parserName;
    }

    @Override
    public ParseResult parse(ExtractResult extractResult) {
        return this.parse(extractResult, LocalDateTime.now());
    }

    @Override
    public DateTimeParseResult parse(ExtractResult er, LocalDateTime refDate) {

        DateTimeResolutionResult value = null;
        if (er.type.equals(parserName)) {
            DateTimeResolutionResult innerResult = parseBaseDatePeriod(er.text, refDate);

            if (!innerResult.getSuccess()) {
                innerResult = parseComplexDatePeriod(er.text, refDate);
            }

            if (innerResult.getSuccess()) {
                if (innerResult.getMod().equals(Constants.BEFORE_MOD)) {
                    innerResult.setFutureResolution(ImmutableMap.<String,String>builder()
                        .put(TimeTypeConstants.END_DATE,
                            FormatUtil.formatDate((LocalDateTime)innerResult.getFutureValue()))
                        .build());

                    innerResult.setPastResolution(ImmutableMap.<String,String>builder()
                        .put(TimeTypeConstants.END_DATE,
                            FormatUtil.formatDate((LocalDateTime)innerResult.getPastValue()))
                        .build());
                } else if (innerResult.getMod().equals(Constants.AFTER_MOD)) {
                    innerResult.setFutureResolution(ImmutableMap.<String,String>builder()
                            .put(TimeTypeConstants.START_DATE,
                                    FormatUtil.formatDate((LocalDateTime)innerResult.getFutureValue()))
                            .build());

                    innerResult.setPastResolution(ImmutableMap.<String,String>builder()
                            .put(TimeTypeConstants.START_DATE,
                                    FormatUtil.formatDate((LocalDateTime)innerResult.getPastValue()))
                            .build());
                } else if (innerResult.getFutureValue() != null && innerResult.getPastValue() != null) {
                    innerResult.setFutureResolution(ImmutableMap.<String,String>builder()
                            .put(TimeTypeConstants.START_DATE,
                                    FormatUtil.formatDate(((Pair<LocalDateTime,LocalDateTime>)innerResult.getFutureValue()).getValue0()))
                            .put(TimeTypeConstants.END_DATE,
                                    FormatUtil.formatDate(((Pair<LocalDateTime,LocalDateTime>)innerResult.getFutureValue()).getValue1()))
                            .build());

                    innerResult.setPastResolution(ImmutableMap.<String,String>builder()
                            .put(TimeTypeConstants.START_DATE,
                                    FormatUtil.formatDate(((Pair<LocalDateTime,LocalDateTime>)innerResult.getPastValue()).getValue0()))
                            .put(TimeTypeConstants.END_DATE,
                                    FormatUtil.formatDate(((Pair<LocalDateTime,LocalDateTime>)innerResult.getPastValue()).getValue1()))
                            .build());
                } else {
                    innerResult.setFutureResolution(new HashMap<>());
                    innerResult.setPastResolution(new HashMap<>());
                }
                value = innerResult;
            }
        }

        DateTimeParseResult ret = new DateTimeParseResult(er.start, er.length, er.text, er.type, er.data, value, "","");

        if (value != null) {
            ret = ret.withTimexStr(value.getTimex());
        }

        return ret;
    }

    // Process case like "from|between START to|and END" where START/END can be dateRange or datePoint
    private DateTimeResolutionResult parseComplexDatePeriod(String text, LocalDateTime referenceDate) {
        DateTimeResolutionResult ret = new DateTimeResolutionResult();
        Optional<Match> match = Arrays.stream(RegExpUtility.getMatches(this.config.getComplexDatePeriodRegex(), text)).findFirst();

        if (match.isPresent()) {
            LocalDateTime futureBegin = LocalDateTime.MIN;
            LocalDateTime futureEnd = LocalDateTime.MIN;
            LocalDateTime pastBegin = LocalDateTime.MIN;
            LocalDateTime pastEnd = LocalDateTime.MIN;
            boolean isSpecificDate = false;
            boolean isStartByWeek = false;
            boolean isEndByWeek = false;

            DateTimeResolutionResult startResolution = parseSingleTimePoint(match.get().getGroup("start").value.trim(), referenceDate);

            if (startResolution.getSuccess()) {
                futureBegin = (LocalDateTime)startResolution.getFutureValue();
                pastBegin = (LocalDateTime)startResolution.getPastValue();
                isSpecificDate = true;
            } else {
                startResolution = parseBaseDatePeriod(match.get().getGroup("start").value.trim(), referenceDate);
                if (startResolution.getSuccess()) {
                    futureBegin = ((Pair<LocalDateTime,LocalDateTime>)startResolution.getFutureValue()).getValue0();
                    pastBegin = ((Pair<LocalDateTime,LocalDateTime>)startResolution.getPastValue()).getValue0();

                    if (startResolution.getTimex().contains("-W")) {
                        isStartByWeek = true;
                    }
                }
            }

            if (startResolution.getSuccess()) {
                DateTimeResolutionResult endResolution = parseSingleTimePoint(match.get().getGroup("end").value.trim(), referenceDate);

                if (endResolution.getSuccess()) {
                    futureEnd = (LocalDateTime)endResolution.getFutureValue();
                    pastEnd = (LocalDateTime)endResolution.getPastValue();
                    isSpecificDate = true;
                } else {
                    endResolution = parseBaseDatePeriod(match.get().getGroup("end").value.trim(), referenceDate);

                    if (endResolution.getSuccess()) {
                        futureEnd = ((Pair<LocalDateTime,LocalDateTime>)startResolution.getFutureValue()).getValue0();
                        pastEnd = ((Pair<LocalDateTime,LocalDateTime>)startResolution.getPastValue()).getValue0();

                        if (endResolution.getTimex().contains("-W")) {
                            isStartByWeek = true;
                        }
                    }
                }

                if (endResolution.getSuccess()) {
                    if (futureBegin.isAfter(futureEnd)) {
                        futureBegin = pastBegin;
                    }

                    if (pastEnd.isBefore(pastBegin)) {
                        pastEnd = futureEnd;
                    }

                    // If both begin/end are date ranges in "Month", the Timex should be ByMonth
                    // The year period case should already be handled in Basic Cases
                    DatePeriodTimexType datePeriodTimexType = DatePeriodTimexType.ByMonth;

                    if (isSpecificDate) {
                        // If at least one of the begin/end is specific date, the Timex should be ByDay
                        datePeriodTimexType = DatePeriodTimexType.ByDay;
                    } else if (isStartByWeek && isEndByWeek) {
                        // If both begin/end are date ranges in "Week", the Timex should be ByWeek
                        datePeriodTimexType = DatePeriodTimexType.ByWeek;
                    }

                    ret.setTimex(TimexUtility.generateDatePeriodTimex(futureBegin,futureEnd, datePeriodTimexType));

                    ret.setFutureValue(new Pair<>(futureBegin,futureEnd));
                    ret.setPastValue(new Pair<>(pastBegin,pastEnd));
                    ret.setSuccess(true);
                }
            }
        }
        return ret;
    }

    private DateTimeResolutionResult parseBaseDatePeriod(String text, LocalDateTime referenceDate) {
        DateTimeResolutionResult innerResult = parseMonthWithYear(text, referenceDate);

        if (!innerResult.getSuccess()) {
            innerResult = parseSimpleCases(text, referenceDate);
        }

        if (!innerResult.getSuccess()) {
            innerResult = parseOneWordPeriod(text, referenceDate);
        }
        
        if (!innerResult.getSuccess()) {
            innerResult = mergeTwoTimePoints(text, referenceDate);
        }
        
        if (!innerResult.getSuccess()) {
            innerResult = parseYear(text, referenceDate);
        }
        
        if (!innerResult.getSuccess()) {
            innerResult = parseWeekOfMonth(text, referenceDate);
        }
        
        if (!innerResult.getSuccess()) {
            innerResult = parseWeekOfYear(text, referenceDate);
        }
        
        if (!innerResult.getSuccess()) {
            innerResult = parseHalfYear(text, referenceDate);
        }
        
        if (!innerResult.getSuccess()) {
            innerResult = parseQuarter(text, referenceDate);
        }
        
        if (!innerResult.getSuccess()) {
            innerResult = parseSeason(text, referenceDate);
        }
        
        if (!innerResult.getSuccess()) {
            innerResult = parseWhichWeek(text, referenceDate);
        }
        
        if (!innerResult.getSuccess()) {
            innerResult = parseWeekOfDate(text, referenceDate);
        }
        
        if (!innerResult.getSuccess()) {
            innerResult = parseMonthOfDate(text, referenceDate);
        }
        
        if (!innerResult.getSuccess()) {
            innerResult = parseDecade(text, referenceDate);
        }
        
        // Cases like "within/less than/more than x weeks from/before/after today"
        if (!innerResult.getSuccess()) {
            innerResult = parseDatePointWithAgoAndLater(text, referenceDate);
        }
        
        // Parse duration should be at the end since it will extract "the last week" from "the last week of July"
        if (!innerResult.getSuccess()) {
            innerResult = parseDuration(text, referenceDate);
        }
        
        // Cases like "21st century"
        if (!innerResult.getSuccess()) {
            innerResult = parseOrdinalNumberWithCenturySuffix(text, referenceDate);
        }

        return innerResult;
    }

    private DateTimeResolutionResult parseOrdinalNumberWithCenturySuffix(String text, LocalDateTime referenceDate) {
        DateTimeResolutionResult ret = new DateTimeResolutionResult();
        Optional<ExtractResult> er = this.config.getOrdinalExtractor().extract(text).stream().findFirst();

        if (er.isPresent() && er.get().start + er.get().length < text.length()) {
            String afterString = text.substring(er.get().start + er.get().length).trim();

            // It falls into the cases like "21st century"
            if (Arrays.stream(RegExpUtility.getMatches(this.config.getCenturySuffixRegex(), afterString)).findFirst().isPresent()) {
                ParseResult number = this.config.getNumberParser().parse(er.get());

                if (number.value != null) {
                    // Note that 1st century means from year 0 - 100
                    int startYear = (Math.round(((Double)number.value).floatValue()) - 1) * Constants.CenturyYearsCount;
                    LocalDateTime startDate = DateUtil.safeCreateFromMinValue(startYear, 1, 1);
                    LocalDateTime endDate = DateUtil.safeCreateFromMinValue(startYear + Constants.CenturyYearsCount, 1, 1);

                    String startLuisStr = FormatUtil.luisDate(startDate);
                    String endLuisStr = FormatUtil.luisDate(endDate);
                    String durationTimex = "P" + Constants.CenturyYearsCount + "Y";

                    ret.setTimex(String.format("(%s,%s,%s)",startLuisStr,endLuisStr,durationTimex));
                    ret.setFutureValue(new Pair<>(startDate,endDate));
                    ret.setPastValue(new Pair<>(startDate,endDate));
                    ret.setSuccess(true);
                }
            }
        }

        return ret;
    }

    private DateTimeResolutionResult parseDatePointWithAgoAndLater(String text, LocalDateTime referenceDate) {

        DateTimeResolutionResult ret = new DateTimeResolutionResult();
        Optional<ExtractResult> er = this.config.getDateExtractor().extract(text, referenceDate).stream().findFirst();

        if (er.isPresent()) {
            String beforeString = text.substring(0, er.get().start);
            boolean isAgo = Arrays.stream(RegExpUtility.getMatches(this.config.getAgoRegex(),er.get().text)).findFirst().isPresent();
            boolean isLater = Arrays.stream(RegExpUtility.getMatches(this.config.getLaterRegex(),er.get().text)).findFirst().isPresent();

            if (!StringUtility.isNullOrEmpty(beforeString) && (isAgo || isLater)) {
                boolean isLessThanOrWithIn = false;
                boolean isMoreThan = false;

                // cases like "within 3 days from yesterday/tomorrow" does not make any sense
                if (er.get().text.contains("today") || er.get().text.contains("now")) {
                    Optional<Match> match = Arrays.stream(RegExpUtility.getMatches(this.config.getWithinNextPrefixRegex(),beforeString)).findFirst();
                    if (match.isPresent()) {
                        boolean isNext = !StringUtility.isNullOrEmpty(match.get().getGroup("next").value);

                        // cases like "within the next 5 days before today" is not acceptable
                        if (!(isNext && isAgo)) {
                            isLessThanOrWithIn = true;
                        }
                    }
                }

                isLessThanOrWithIn = isLessThanOrWithIn || (Arrays.stream(RegExpUtility.getMatches(this.config.getLessThanRegex(),beforeString)).findFirst().isPresent());
                isMoreThan = Arrays.stream(RegExpUtility.getMatches(this.config.getMoreThanRegex(),beforeString)).findFirst().isPresent();

                DateTimeParseResult pr = this.config.getDateParser().parse(er.get(), referenceDate);
                Optional<ExtractResult> durationExtractionResult = this.config.getDurationExtractor().extract(er.get().text).stream().findFirst();

                if (durationExtractionResult.isPresent()) {
                    ParseResult duration = this.config.getDurationParser().parse(durationExtractionResult.get());
                    long durationInSeconds = (long)((DateTimeResolutionResult)(duration.value)).getPastValue();

                    if (isLessThanOrWithIn) {
                        LocalDateTime startDate = LocalDateTime.MIN;
                        LocalDateTime endDate = LocalDateTime.MIN;

                        if (isAgo) {
                            startDate = (LocalDateTime)((DateTimeResolutionResult)(pr.value)).getPastValue();
                            endDate = startDate.plusSeconds(durationInSeconds);
                        } else if (isLater) {
                            endDate = (LocalDateTime)((DateTimeResolutionResult)(pr.value)).getFutureValue();
                            startDate = endDate.minusSeconds(durationInSeconds);
                        }

                        if (startDate != LocalDateTime.MIN) {
                            String startLuisStr = FormatUtil.luisDate(startDate);
                            String endLuisStr = FormatUtil.luisDate(endDate);
                            String durationTimex = ((DateTimeResolutionResult)(duration.value)).getTimex();

                            ret.setTimex(String.format("(%s,%s,%s)",startLuisStr,endLuisStr,durationTimex));
                            ret.setFutureValue(new Pair<>(startDate,endDate));
                            ret.setPastValue(new Pair<>(startDate,endDate));
                            ret.setSuccess(true);
                        }
                    } else if (isMoreThan && (isAgo || isLater)) {
                        if (isAgo) {
                            ret.setMod(Constants.BEFORE_MOD);
                        } else if (isLater) {
                            ret.setMod(Constants.AFTER_MOD);
                        }

                        ret.setTimex(pr.timexStr);
                        ret.setFutureValue((LocalDateTime)((DateTimeResolutionResult)(pr.value)).getFutureValue());
                        ret.setPastValue((LocalDateTime)((DateTimeResolutionResult)(pr.value)).getPastValue());
                        ret.setSuccess(true);
                    }
                }
            }
        }

        return ret;
    }

    private DateTimeResolutionResult parseSingleTimePoint(String text, LocalDateTime referenceDate) {

        DateTimeResolutionResult ret = new DateTimeResolutionResult();
        ExtractResult er =  this.config.getDateExtractor().extract(text, referenceDate).stream().findFirst().orElse(null);

        if (er != null) {
            Optional<Match> match = Arrays.stream(RegExpUtility.getMatches(this.config.getWeekWithWeekDayRangeRegex(),text)).findFirst();
            String weekPrefix = null;
            if (match.isPresent()) {
                weekPrefix = match.get().getGroup("week").toString();
            }

            if (!StringUtility.isNullOrEmpty(weekPrefix)) {
                er = er.withText(weekPrefix + " " + er.text);
            }

            ParseResult pr = this.config.getDateParser().parse(er, referenceDate);

            if (pr != null) {
                ret.setTimex("(" + ((DateTimeParseResult) pr).timexStr);
                ret.setFutureValue((LocalDateTime)((DateTimeResolutionResult)pr.value).getFutureValue());
                ret.setPastValue((LocalDateTime)((DateTimeResolutionResult)pr.value).getPastValue());
                ret.setSuccess(true);
            }
        }

        return ret;
    }

    private DateTimeResolutionResult parseSimpleCases(String text, LocalDateTime referenceDate) {

        DateTimeResolutionResult ret = new DateTimeResolutionResult();
        int year = referenceDate.getYear(), month = referenceDate.getMonthValue();
        int beginDay, endDay;
        boolean noYear = true;

        String trimedText = text.trim();
        Optional<Match> match = Arrays.stream(RegExpUtility.getMatches(this.config.getMonthFrontBetweenRegex(),trimedText)).findFirst();
        String beginLuisStr, endLuisStr;

        if (!match.isPresent()) {
            match = Arrays.stream(RegExpUtility.getMatches(this.config.getBetweenRegex(),trimedText)).findFirst();
        }

        if (!match.isPresent()) {
            match = Arrays.stream(RegExpUtility.getMatches(this.config.getMonthFrontSimpleCasesRegex(),trimedText)).findFirst();
        }

        if (!match.isPresent()) {
            match = Arrays.stream(RegExpUtility.getMatches(this.config.getSimpleCasesRegex(),trimedText)).findFirst();
        }

        if (match.isPresent() && match.get().index == 0 && match.get().length == trimedText.length()) {
            MatchGroup days = match.get().getGroup("day");
            beginDay = this.config.getDayOfMonth().get(days.captures[0].toLowerCase());
            endDay = this.config.getDayOfMonth().get(days.captures[1].toLowerCase());

            // parse year
            year = ((BaseDateExtractor)this.config.getDateExtractor()).getYearFromText(match.get());
            if (year != Constants.InvalidYear) {
                noYear = false;
            } else {
                year = referenceDate.getYear();
            }

            String monthStr = match.get().getGroup("month").value;
            if (!StringUtility.isNullOrEmpty(monthStr)) {
                month = this.config.getMonthOfYear().get(monthStr.toLowerCase());
            } else {
                monthStr = match.get().getGroup("relmonth").value.trim().toLowerCase();
                int swiftMonth = this.config.getSwiftDayOrMonth(monthStr);
                switch (swiftMonth) {
                    case 1:
                        if (month != 12) {
                            month += 1;
                        } else {
                            month = 1;
                            year += 1;
                        }
                        break;
                    case -1:
                        if (month != 1) {
                            month -= 1;
                        } else {
                            month = 12;
                            year -= 1;
                        }
                        break;
                    default:
                        break;
                }

                if (this.config.isFuture(monthStr)) {
                    noYear = false;
                }
            }
        } else {
            return ret;
        }

        if (noYear) {
            beginLuisStr = FormatUtil.luisDate(-1, month, beginDay);
            endLuisStr = FormatUtil.luisDate(-1, month, endDay);
        } else {
            beginLuisStr = FormatUtil.luisDate(year, month, beginDay);
            endLuisStr = FormatUtil.luisDate(year, month, endDay);
        }

        int futureYear = year, pastYear = year;
        LocalDateTime startDate = DateUtil.safeCreateFromMinValue(year,month,beginDay);

        if (noYear && startDate.isBefore(referenceDate)) {
            futureYear++;
        }

        if (noYear && (startDate.isAfter(referenceDate) ||  startDate.isEqual(referenceDate))) {
            pastYear--;
        }

        ret.setTimex(String.format("(%s,%s,P%sD)", beginLuisStr,endLuisStr,(endDay - beginDay)));
        ret.setFutureValue(new Pair<>(DateUtil.safeCreateFromMinValue(futureYear, month, beginDay),
                DateUtil.safeCreateFromMinValue(futureYear, month, endDay)));
        ret.setPastValue(new Pair<>(DateUtil.safeCreateFromMinValue(pastYear, month, beginDay),
                DateUtil.safeCreateFromMinValue(pastYear, month, endDay)));
        ret.setSuccess(true);

        return ret;
    }

    private DateTimeResolutionResult parseOneWordPeriod(String text, LocalDateTime referenceDate) {

        DateTimeResolutionResult ret = new DateTimeResolutionResult();
        int year = referenceDate.getYear(), month = referenceDate.getMonthValue();
        int futureYear = year, pastYear = year;
        boolean earlyPrefix = false;
        boolean latePrefix = false;
        boolean midPrefix = false;
        boolean isRef = false;

        boolean earlierPrefix = false;
        boolean laterPrefix = false;

        String trimmedText = text.trim().toLowerCase();
        Optional<Match> match = Arrays.stream(RegExpUtility.getMatches(this.config.getOneWordPeriodRegex(),trimmedText)).findFirst();

        if (!(match.isPresent() && match.get().index == 0 && match.get().length == trimmedText.length())) {
            match = Arrays.stream(RegExpUtility.getMatches(this.config.getLaterEarlyPeriodRegex(),trimmedText)).findFirst();
        }

        // For cases "that week|month|year"
        if (!(match.isPresent() && match.get().index == 0 && match.get().length == trimmedText.length())) {
            match = Arrays.stream(RegExpUtility.getMatches(this.config.getReferenceDatePeriodRegex(),trimmedText)).findFirst();
            isRef = true;
            ret.setMod(Constants.REF_UNDEF_MOD);
        }

        if (match.isPresent() && match.get().index == 0 && match.get().length == trimmedText.length()) {
            if (!match.get().getGroup("EarlyPrefix").value.equals("")) {
                earlyPrefix = true;
                trimmedText = match.get().getGroup(Constants.SuffixGroupName).toString();
                ret.setMod(Constants.EARLY_MOD);
            } else if (!match.get().getGroup("LatePrefix").value.equals("")) {
                latePrefix = true;
                trimmedText = match.get().getGroup(Constants.SuffixGroupName).toString();
                ret.setMod(Constants.LATE_MOD);
            } else if (!match.get().getGroup("MidPrefix").value.equals("")) {
                midPrefix = true;
                trimmedText = match.get().getGroup(Constants.SuffixGroupName).toString();
                ret.setMod(Constants.MID_MOD);
            }

            if (!match.get().getGroup("RelEarly").value.equals("")) {
                earlierPrefix = true;
                ret.setMod(null);
            } else if (!match.get().getGroup("RelLate").value.equals("")) {
                laterPrefix = true;
                ret.setMod(null);
            }

            String monthStr = match.get().getGroup("month").value;
            if (this.config.isYearToDate(trimmedText)) {
                ret.setTimex(String.format("%04d",referenceDate.getYear()));
                ret.setFutureValue(new Pair<>(
                        DateUtil.safeCreateFromMinValue(referenceDate.getYear(),1,1), referenceDate));
                ret.setPastValue(new Pair<>(
                        DateUtil.safeCreateFromMinValue(referenceDate.getYear(),1,1), referenceDate));

                ret.setSuccess(true);
                return ret;
            }

            if (this.config.isMonthToDate(trimmedText)) {
                ret.setTimex(String.format("%04d-%02d", referenceDate.getYear(), referenceDate.getMonthValue()));
                ret.setFutureValue(new Pair<>(
                        DateUtil.safeCreateFromMinValue(referenceDate.getYear(), referenceDate.getMonthValue(), 1),
                        referenceDate));
                ret.setPastValue(new Pair<>(
                        DateUtil.safeCreateFromMinValue(referenceDate.getYear(), referenceDate.getMonthValue(), 1),
                        referenceDate));

                ret.setSuccess(true);
                return ret;
            }

            if (!StringUtility.isNullOrEmpty(monthStr)) {
                int swift = this.config.getSwiftYear(trimmedText);

                month = this.config.getMonthOfYear().get(monthStr.toLowerCase());

                if (swift >= -1) {
                    ret.setTimex(String.format("%04d-%02d",referenceDate.getYear()+swift,month));
                    year = year + swift;
                    futureYear = pastYear = year;
                } else {
                    ret.setTimex(String.format("XXXX-%02d",month));
                    if (month < referenceDate.getMonthValue()) {
                        futureYear++;
                    }

                    if (month >= referenceDate.getMonthValue()) {
                        pastYear--;
                    }
                }
            } else {
                int swift = this.config.getSwiftDayOrMonth(trimmedText);

                if (this.config.isWeekOnly(trimmedText)) {
                    LocalDateTime monday = DateUtil.next(referenceDate, DayOfWeek.MONDAY.getValue()).plusDays(7 * swift);

                    ret.setTimex(isRef ? TimexUtility.generateWeekTimex() : TimexUtility.generateWeekTimex(monday));

                    LocalDateTime beginDate = DateUtil.next(referenceDate,DayOfWeek.MONDAY.getValue()).plusDays(7 * swift);

                    LocalDateTime endValue = DateUtil.next(referenceDate, DayOfWeek.SUNDAY.getValue()).plusDays(7 * swift);

                    LocalDateTime endDate = inclusiveEndPeriod ? endValue : endValue.plusDays(1);

                    if (earlyPrefix) {
                        endValue = DateUtil.next(referenceDate, DayOfWeek.WEDNESDAY.getValue()).plusDays(7 * swift);
                        endDate = inclusiveEndPeriod ? endValue : endValue.plusDays(1);
                    } else if (midPrefix) {
                        beginDate = DateUtil.next(referenceDate,DayOfWeek.TUESDAY.getValue()).plusDays(7 * swift);
                        endValue = DateUtil.next(referenceDate,DayOfWeek.FRIDAY.getValue()).plusDays(7 * swift);
                        endDate = inclusiveEndPeriod ? endValue : endValue.plusDays(1);
                    } else if (latePrefix) {
                        beginDate = DateUtil.next(referenceDate, DayOfWeek.THURSDAY.getValue()).plusDays(7 * swift);
                    }

                    if (earlierPrefix && swift == 0) {
                        if (endDate.isAfter(referenceDate)) {
                            endDate = referenceDate;
                        }
                    } else if (laterPrefix && swift == 0) {
                        if (beginDate.isBefore(referenceDate)) {
                            beginDate = referenceDate;
                        }
                    }

                    ret.setFutureValue(new Pair<>(beginDate,endDate));
                    ret.setPastValue(new Pair<>(beginDate,endDate));

                    ret.setSuccess(true);
                    return ret;
                }

                if (this.config.isWeekend(trimmedText)) {
                    LocalDateTime beginDate = DateUtil.next(referenceDate,DayOfWeek.SATURDAY.getValue()).plusDays(7 * swift);
                    LocalDateTime endValue = DateUtil.next(referenceDate,DayOfWeek.SUNDAY.getValue()).plusDays(7 * swift);
                    LocalDateTime endDate = inclusiveEndPeriod ? endValue : endValue.plusDays(1);

                    ret.setTimex(isRef ? TimexUtility.generateWeekendTimex() : TimexUtility.generateWeekendTimex(beginDate));

                    ret.setFutureValue(new Pair<>(beginDate,endDate));
                    ret.setPastValue(new Pair<>(beginDate,endDate));

                    ret.setSuccess(true);
                    return ret;
                }

                if (this.config.isMonthOnly(trimmedText))
                {
                    LocalDateTime date = referenceDate.plusMonths(swift);
                    month = date.getMonthValue();
                    year = date.getYear();

                    ret.setTimex(isRef ? TimexUtility.generateMonthTimex() : TimexUtility.generateMonthTimex(date));

                    futureYear = pastYear = year;
                } else if (this.config.isYearOnly(trimmedText)) {
                    LocalDateTime date = referenceDate.plusYears(swift);
                    year = date.getYear();

                    LocalDateTime beginDate = DateUtil.safeCreateFromMinValue(year,1,1);

                    LocalDateTime endValue = DateUtil.safeCreateFromMinValue(year,12,31);
                    LocalDateTime endDate = inclusiveEndPeriod ? endValue : endValue.plusDays(1);

                    if (earlyPrefix) {
                        endValue = DateUtil.safeCreateFromMinValue(year,6,30);
                        endDate = inclusiveEndPeriod ? endValue : endValue.plusDays(1);
                    } else if (midPrefix) {
                        beginDate = DateUtil.safeCreateFromMinValue(year, 4, 1);
                        endValue = DateUtil.safeCreateFromMinValue(year,9,30);
                        endDate = inclusiveEndPeriod ? endValue : endValue.plusDays(1);
                    } else if (latePrefix) {
                        beginDate = DateUtil.safeCreateFromMinValue(year, 7, 1);
                    }

                    if (earlierPrefix && swift == 0) {
                        if (endDate.isAfter(referenceDate)) {
                            endDate = referenceDate;
                        }
                    } else if (laterPrefix && swift == 0) {
                        if (beginDate.isBefore(referenceDate)) {
                            beginDate = referenceDate;
                        }
                    }

                    ret.setTimex(isRef ? TimexUtility.generateYearTimex() : TimexUtility.generateYearTimex(date));

                    ret.setFutureValue(new Pair<>(beginDate,endDate));
                    ret.setPastValue(new Pair<>(beginDate,endDate));

                    ret.setSuccess(true);
                    return ret;
                }
            }
        } else {
            return ret;
        }

        // only "month" will come to here
        LocalDateTime futureStart = DateUtil.safeCreateFromMinValue(futureYear, month, 1);
        LocalDateTime futureEnd = inclusiveEndPeriod ? futureStart.plusMonths(1).minusDays(1) : futureStart.plusMonths(1);


        LocalDateTime pastStart = DateUtil.safeCreateFromMinValue(pastYear, month, 1);
        LocalDateTime pastEnd = inclusiveEndPeriod ? pastStart.plusMonths(1).minusDays(1) : pastStart.plusMonths(1).minusDays(1);

        if (earlyPrefix) {
            futureEnd = inclusiveEndPeriod
                ? DateUtil.safeCreateFromMinValue(futureYear,month,15)
                : DateUtil.safeCreateFromMinValue(futureYear,month,15).plusDays(1);
            pastEnd = inclusiveEndPeriod
                ? DateUtil.safeCreateFromMinValue(pastYear,month,15)
                : DateUtil.safeCreateFromMinValue(pastYear,month,15).plusDays(1);
        } else if (midPrefix) {
            futureStart = DateUtil.safeCreateFromMinValue(futureYear, month, 10);
            pastStart = DateUtil.safeCreateFromMinValue(pastYear, month, 10);
            futureEnd = inclusiveEndPeriod
                ? futureStart.plusDays(10)
                : futureStart.plusDays(11);
            pastEnd = inclusiveEndPeriod
                ? pastStart.plusDays(10)
                : pastStart.plusDays(11);
        } else if (latePrefix) {
            futureStart = DateUtil.safeCreateFromMinValue(futureYear, month, 16);
            pastStart = DateUtil.safeCreateFromMinValue(pastYear, month, 16);
        }

        if (earlierPrefix && futureEnd == pastEnd)
        {
            if (futureEnd.isAfter(referenceDate))
            {
                futureEnd = pastEnd = referenceDate;
            }
        } else if (laterPrefix && futureStart == pastStart) {
            if (futureStart.isBefore(referenceDate)) {
                futureStart = pastStart = referenceDate;
            }
        }

        ret.setFutureValue(new Pair<>(futureStart, futureEnd));

        ret.setPastValue(new Pair<>(pastStart, pastEnd));

        ret.setSuccess(true);

        return ret;
    }

    private DateTimeResolutionResult parseMonthWithYear(String text, LocalDateTime referenceDate) {

        DateTimeResolutionResult ret = new DateTimeResolutionResult();

        Optional<Match> match = Arrays.stream(RegExpUtility.getMatches(this.config.getMonthWithYear(), text)).findFirst();
        if (!match.isPresent())
        {
            match = Arrays.stream(RegExpUtility.getMatches(this.config.getMonthNumWithYear(), text)).findFirst();
        }

        if (match.isPresent() && match.get().length == text.length())
        {
            String monthStr = match.get().getGroup("month").value.toLowerCase();
            String orderStr = match.get().getGroup("order").value.toLowerCase();

            int month = this.config.getMonthOfYear().get(monthStr.toLowerCase());

            int year = ((BaseDateExtractor)this.config.getDateExtractor()).getYearFromText(match.get());
            if (year == Constants.InvalidYear)
            {
                int swift = this.config.getSwiftYear(orderStr);
                if (swift < -1)
                {
                    return ret;
                }
                year = referenceDate.getYear() + swift;
            }

            LocalDateTime startValue = DateUtil.safeCreateFromMinValue(year, month,1);
            LocalDateTime endValue = inclusiveEndPeriod
                ? DateUtil.safeCreateFromMinValue(year, month,1).plusMonths(1).minusDays(1)
                : DateUtil.safeCreateFromMinValue(year, month,1).plusMonths(1);

            ret.setFutureValue(new Pair<>(startValue,endValue));
            ret.setPastValue(new Pair<>(startValue,endValue));

            ret.setTimex(String.format("%04d-%02d", year,month));

            ret.setSuccess(true);
        }

        return ret;
    }

    private DateTimeResolutionResult parseYear (String text, LocalDateTime referenceDate) {
        DateTimeResolutionResult ret = new DateTimeResolutionResult();
        int year = Constants.InvalidYear;

        Optional<Match> match = Arrays.stream(RegExpUtility.getMatches(this.config.getYearPeriodRegex(), text)).findFirst();
        Optional<Match> matchMonth = Arrays.stream(RegExpUtility.getMatches(this.config.getMonthWithYear(), text)).findFirst();;

        if (match.isPresent() && !matchMonth.isPresent()) {
            int beginYear = Constants.InvalidYear;
            int endYear = Constants.InvalidYear;

            Match[] matches = RegExpUtility.getMatches(this.config.getYearRegex(), text);
            if (matches.length == 2) {
                // (from|during|in|between)? 2012 (till|to|until|through|-) 2015
                if (!matches[0].value.equals("")) {
                    beginYear = ((BaseDateExtractor)this.config.getDateExtractor()).getYearFromText(matches[0]);
                    if (!(beginYear >= Constants.MinYearNum && beginYear <= Constants.MaxYearNum)) {
                        beginYear = Constants.InvalidYear;
                    }
                }

                if (!matches[1].value.equals("")) {
                    endYear = ((BaseDateExtractor)this.config.getDateExtractor()).getYearFromText(matches[1]);
                    if (!(endYear >= Constants.MinYearNum && endYear <= Constants.MaxYearNum)) {
                        endYear = Constants.InvalidYear;
                    }
                }
            }

            if (beginYear != Constants.InvalidYear && endYear != Constants.InvalidYear) {
                LocalDateTime beginDay = DateUtil.safeCreateFromMinValue(beginYear, 1, 1);

                LocalDateTime endDayValue = DateUtil.safeCreateFromMinValue(endYear,1,1);
                LocalDateTime endDay = inclusiveEndPeriod
                        ? endDayValue.minusDays(1)
                        : endDayValue;

                ret.setTimex(String.format("(%s,%s,P%sY)", FormatUtil.luisDate(beginDay), FormatUtil.luisDate(endDay),(endYear - beginYear)));
                ret.setFutureValue(new Pair<>(beginDay, endDay));
                ret.setPastValue(new Pair<>(beginDay, endDay));
                ret.setSuccess(true);

                return ret;
            }
        } else {
            match = Arrays.stream(RegExpUtility.getMatches(this.config.getYearRegex(), text)).findFirst();
            if (match.isPresent() && match.get().length == text.trim().length()) {
                year = ((BaseDateExtractor)this.config.getDateExtractor()).getYearFromText(match.get());
                if (!(year >= Constants.MinYearNum && year <= Constants.MaxYearNum)) {
                    year = Constants.InvalidYear;
                }
            } else {
                match = Arrays.stream(RegExpUtility.getMatches(this.config.getYearPlusNumberRegex(), text)).findFirst();
                if (match.isPresent() && match.get().length == text.trim().length()) {
                    year = ((BaseDateExtractor)this.config.getDateExtractor()).getYearFromText(match.get());
                }
            }

            if (year != Constants.InvalidYear) {
                LocalDateTime beginDay = DateUtil.safeCreateFromMinValue(year, 1, 1);

                LocalDateTime endDayValue = DateUtil.safeCreateFromMinValue(year+1,1,1);
                LocalDateTime endDay = inclusiveEndPeriod
                        ? endDayValue.minusDays(1)
                        : endDayValue;

                ret.setTimex(String.format("%04d",year));
                ret.setFutureValue(new Pair<>(beginDay, endDay));
                ret.setPastValue(new Pair<>(beginDay, endDay));
                ret.setSuccess(true);

                return ret;
            }
        }

        return ret;
    }

    // parse entities that made up by two time points
    private DateTimeResolutionResult mergeTwoTimePoints (String text, LocalDateTime referenceDate) {
        DateTimeResolutionResult ret = new DateTimeResolutionResult();

        List<ExtractResult> er = this.config.getDateExtractor().extract(text, referenceDate);
        if (er.size() < 2) {
            er = this.config.getDateExtractor().extract(this.config.getTokenBeforeDate() + text, referenceDate);
            if (er.size() < 2)
            {
                return ret;
            }
            er.set(0, er.get(0).withStart(er.get(0).start - this.config.getTokenBeforeDate().length()));
            er.set(1, er.get(1).withStart(er.get(1).start - this.config.getTokenBeforeDate().length()));
        }

        Optional<Match> match = Arrays.stream(RegExpUtility.getMatches(this.config.getWeekWithWeekDayRangeRegex(), text)).findFirst();
        String weekPrefix = null;
        if (match.isPresent()) {
            weekPrefix = match.get().getGroup("week").value;
        }

        if (!StringUtility.isNullOrEmpty(weekPrefix)) {
            er.set(0, er.get(0).withText(String.format("%s %s", weekPrefix,er.get(0).text)));
            er.set(1, er.get(1).withText(String.format("%s %s", weekPrefix,er.get(1).text)));
        }

        ParseResult pr1 = this.config.getDateParser().parse(er.get(0), referenceDate);
        ParseResult pr2 = this.config.getDateParser().parse(er.get(1), referenceDate);
        if (pr1.value == null || pr2.value == null) {
            return ret;
        }

        List<Object> subDateTimeEntities = new ArrayList<Object>();
        subDateTimeEntities.add(pr1);
        subDateTimeEntities.add(pr2);
        ret.setSubDateTimeEntities(subDateTimeEntities);

        LocalDateTime futureBegin = (LocalDateTime) ((DateTimeResolutionResult)pr1.value).getFutureValue();
        LocalDateTime futureEnd = (LocalDateTime) ((DateTimeResolutionResult)pr2.value).getFutureValue();

        LocalDateTime pastBegin = (LocalDateTime)((DateTimeResolutionResult)pr1.value).getPastValue();
        LocalDateTime pastEnd = (LocalDateTime)((DateTimeResolutionResult)pr2.value).getPastValue();

        if (futureBegin.isAfter(futureEnd))
        {
            futureBegin = pastBegin;
        }

        if (pastEnd.isBefore(pastBegin))
        {
            pastEnd = futureEnd;
        }

        ret.setTimex(String.format("(%s,%s,P%sD", ((DateTimeParseResult) pr1).timexStr , ((DateTimeParseResult) pr2).timexStr, (ChronoUnit.DAYS.between(futureEnd,futureBegin))));
        ret.setFutureValue(new Pair<>(futureBegin, futureEnd));
        ret.setPastValue(new Pair<>(pastBegin, pastEnd));
        ret.setSuccess(true);

        return ret;
    }

    private DateTimeResolutionResult parseDuration(String text, LocalDateTime referenceDate) {
        DateTimeResolutionResult ret = new DateTimeResolutionResult();
        LocalDateTime beginDate = referenceDate;
        LocalDateTime endDate = referenceDate;
        String timex = "";
        boolean restNowSunday = false;

        List<ExtractResult> ers = config.getDurationExtractor().extract(text, referenceDate);
        if (ers.size() == 1) {
            ParseResult pr = config.getDurationParser().parse(ers.get(0));
            String beforeStr = text.substring(0, (pr.start != null) ? pr.start : 0).trim().toLowerCase();
            String afterStr = text.substring(((pr.start != null) ? pr.start : 0)+ ((pr.length != null) ? pr.length : 0)).trim().toLowerCase();
            GetModAndDateResult getModAndDateResult = new GetModAndDateResult();

            if (pr.value != null) {
                DateTimeResolutionResult durationResult = (DateTimeResolutionResult)pr.value;

                if (StringUtility.isNullOrEmpty(durationResult.getTimex())) {
                    return ret;
                }

                Optional<Match> prefixMatch = Arrays.stream(RegExpUtility.getMatches(config.getPastRegex(), beforeStr)).findFirst();
                if (prefixMatch.isPresent()) {
                    getModAndDateResult = getModAndDate(beginDate, endDate, referenceDate, durationResult.getTimex(), false);
                    beginDate = getModAndDateResult.beginDate;

                } else {
                    Optional<Match> suffixMatch = Arrays.stream(RegExpUtility.getMatches(config.getPastRegex(), afterStr)).findFirst();
                    if (suffixMatch.isPresent())
                    {
                        getModAndDateResult = getModAndDate(beginDate, endDate, referenceDate, durationResult.getTimex(), false);
                        beginDate = getModAndDateResult.beginDate;
                    }
                }

                // Handle the "within two weeks" case which means from today to the end of next two weeks
                // Cases like "within 3 days before/after today" is not handled here (4th condition)
                prefixMatch = Arrays.stream(RegExpUtility.getMatches(config.getWithinNextPrefixRegex(),beforeStr)).findFirst();
                if (prefixMatch.isPresent() && prefixMatch.get().length == beforeStr.length() &&
                    DurationParsingUtil.isDateDuration(durationResult.getTimex()) && StringUtility.isNullOrEmpty(afterStr)) {
                    getModAndDateResult = getModAndDate(beginDate, endDate, referenceDate, durationResult.getTimex(), true);
                    beginDate = getModAndDateResult.beginDate;
                    endDate = getModAndDateResult.endDate;

                    // In GetModAndDate, this "future" resolution will add one day to beginDate/endDate, but for the "within" case it should start from the current day.
                    beginDate = beginDate.minusDays(1);
                    endDate = endDate.minusDays(1);
                }

                prefixMatch = Arrays.stream(RegExpUtility.getMatches(config.getFutureRegex(),beforeStr)).findFirst();
                if (prefixMatch.isPresent() && prefixMatch.get().length == beforeStr.length()) {
                    getModAndDateResult = getModAndDate(beginDate, endDate, referenceDate, durationResult.getTimex(), true);
                    beginDate = getModAndDateResult.beginDate;
                    endDate = getModAndDateResult.endDate;
                } else {
                    Optional<Match> suffixMatch = Arrays.stream(RegExpUtility.getMatches(config.getFutureRegex(),afterStr)).findFirst();
                    if (suffixMatch.isPresent()) {
                        getModAndDateResult = getModAndDate(beginDate, endDate, referenceDate, durationResult.getTimex(), true);
                        beginDate = getModAndDateResult.beginDate;
                        endDate = getModAndDateResult.endDate;
                    }
                }

                Optional<Match> futureSuffixMatch = Arrays.stream(RegExpUtility.getMatches(config.getFutureSuffixRegex(),afterStr)).findFirst();
                if (futureSuffixMatch.isPresent()) {
                    getModAndDateResult = getModAndDate(beginDate, endDate, referenceDate, durationResult.getTimex(), true);
                    beginDate = getModAndDateResult.beginDate;
                    endDate = getModAndDateResult.endDate;
                }

                // Handle the "in two weeks" case which means the second week
                prefixMatch = Arrays.stream(RegExpUtility.getMatches(config.getInConnectorRegex(),beforeStr)).findFirst();
                if (prefixMatch.isPresent() && prefixMatch.get().length == beforeStr.length() &&
                        !DurationParsingUtil.isMultipleDuration(durationResult.getTimex())) {
                    getModAndDateResult = getModAndDate(beginDate, endDate, referenceDate, durationResult.getTimex(), true);
                    beginDate = getModAndDateResult.beginDate;
                    endDate = getModAndDateResult.endDate;

                    // Change the duration value and the beginDate
                    String unit = durationResult.getTimex().substring(durationResult.getTimex().length() - 1);

                    durationResult.setTimex(String.format("P1%s",unit));
                    beginDate = DurationParsingUtil.shiftDateTime(durationResult.getTimex(), endDate, false);
                }

                if (!StringUtility.isNullOrEmpty(getModAndDateResult.mod)) {
                    ((DateTimeResolutionResult)pr.value).setMod(getModAndDateResult.mod);
                }

                timex = durationResult.getTimex();

                List<Object> subDateTimeEntities = new ArrayList<>();
                subDateTimeEntities.add(pr);
                ret.setSubDateTimeEntities(subDateTimeEntities);
            }
        }

        // Parse "rest of"
        Optional<Match> match = Arrays.stream(RegExpUtility.getMatches(this.config.getRestOfDateRegex(),text)).findFirst();
        if (match.isPresent()) {
            String durationStr = match.get().getGroup("duration").value;
            String durationUnit = this.config.getUnitMap().get(durationStr);
            switch (durationUnit) {
                case "W":
                    int diff = 7 - ((beginDate.getDayOfWeek().getValue()) == 0 ? 7 : beginDate.getDayOfWeek().getValue());
                    endDate = beginDate.plusDays(diff);
                    timex = String.format("P%sD", diff);
                    if (diff == 0) {
                        restNowSunday = true;
                    }
                    break;

                case "MON":
                    endDate = DateUtil.safeCreateFromMinValue(beginDate.getYear(), beginDate.getMonthValue(), 1);
                    endDate = endDate.plusMonths(1).minusDays(1);
                    diff = (int)ChronoUnit.DAYS.between(endDate, beginDate) + 1;
                    timex = String.format("P%sD",diff);
                    break;

                case "Y":
                    endDate = DateUtil.safeCreateFromMinValue(beginDate.getYear(), 12, 1);
                    endDate = endDate.plusMonths(1).minusDays(1);
                    diff = (int)ChronoUnit.DAYS.between(endDate,beginDate) + 1;
                    timex = String.format("P%sD",diff);
                    break;
            }
        }

        if (!beginDate.equals(endDate) || restNowSunday) {
            endDate = inclusiveEndPeriod ? endDate.minusDays(1) : endDate;

            ret.setTimex(String.format("(%s,%s,%s)",FormatUtil.luisDate(beginDate),FormatUtil.luisDate(endDate),timex));
            ret.setFutureValue(new Pair<>(beginDate, endDate));
            ret.setPastValue(new Pair<>(beginDate, endDate));
            ret.setSuccess(true);

            return ret;
        }

        return ret;
    }

    private GetModAndDateResult getModAndDate(LocalDateTime beginDate, LocalDateTime endDate, LocalDateTime referenceDate, String timex, boolean future) {
        if (future) {
            String mod = Constants.AFTER_MOD;

            // For future the beginDate should add 1 first
            LocalDateTime beginDateResult = referenceDate.plusDays(1);
            LocalDateTime endDateResult = DurationParsingUtil.shiftDateTime(timex, beginDateResult, true);
            return new GetModAndDateResult(beginDateResult,endDateResult,mod);
        } else {
            String mod = Constants.BEFORE_MOD;
            LocalDateTime beginDateResult = DurationParsingUtil.shiftDateTime(timex, endDate, false);
            return new GetModAndDateResult(beginDateResult,endDate,mod);
        }
    }

    @Override
    public List<DateTimeParseResult> filterResults(String query, List<DateTimeParseResult> candidateResults) {
        throw new UnsupportedOperationException();
    }
}

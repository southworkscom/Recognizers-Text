package com.microsoft.recognizers.text.datetime.parsers.config;

import com.google.common.collect.ImmutableMap;
import com.microsoft.recognizers.text.IExtractor;
import com.microsoft.recognizers.text.IParser;
import com.microsoft.recognizers.text.datetime.DateTimeOptions;
import com.microsoft.recognizers.text.datetime.config.BaseOptionsConfiguration;
import com.microsoft.recognizers.text.datetime.config.IOptionsConfiguration;
import com.microsoft.recognizers.text.datetime.extractors.IDateExtractor;
import com.microsoft.recognizers.text.datetime.extractors.IDateTimeExtractor;
import com.microsoft.recognizers.text.datetime.parsers.IDateTimeParser;
import com.microsoft.recognizers.text.datetime.resources.EnglishDateTime;
import com.microsoft.recognizers.text.utilities.Match;
import com.microsoft.recognizers.text.utilities.RegExpUtility;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public abstract class DatePeriodParserConfiguration extends BaseOptionsConfiguration implements IOptionsConfiguration {

    public DatePeriodParserConfiguration(DateTimeOptions options) {
        super(options);
    }

    public abstract List<String> getGenericYearTerms();

    public abstract List<String> getMonthTerms();

    public abstract List<String> getMonthToDateTerms();

    public abstract List<String> getWeekendTerms();

    public abstract List<String> getWeekTerms();

    public abstract List<String> getYearTerms();

    public abstract List<String> getYearToDateTerms();

    public abstract String getTokenBeforeDate();

    public abstract IDateExtractor getDateExtractor();

    public abstract IExtractor getCardinalExtractor();

    public abstract IExtractor getOrdinalExtractor();

    public abstract IExtractor getIntegerExtractor();

    public abstract IParser getNumberParser();

    public abstract IDateTimeExtractor getDurationExtractor();

    public abstract IDateTimeParser getDurationParser();

    public abstract IDateTimeParser getDateParser();

    public abstract Pattern getMonthFrontBetweenRegex();

    public abstract Pattern getBetweenRegex();

    public abstract Pattern getMonthFrontSimpleCasesRegex();

    public abstract Pattern getSimpleCasesRegex();

    public abstract Pattern getOneWordPeriodRegex();

    public abstract Pattern getMonthWithYear();

    public abstract Pattern getMonthNumWithYear();

    public abstract Pattern getYearRegex();

    public abstract Pattern getPastRegex();

    public abstract Pattern getFutureRegex();

    public abstract Pattern getFutureSuffixRegex();

    public abstract Pattern getNumberCombinedWithUnit();

    public abstract Pattern getWeekOfMonthRegex();

    public abstract Pattern getWeekOfYearRegex();

    public abstract Pattern getQuarterRegex();

    public abstract Pattern getQuarterRegexYearFront();

    public abstract Pattern getAllHalfYearRegex();

    public abstract Pattern getSeasonRegex();

    public abstract Pattern getWhichWeekRegex();

    public abstract Pattern getWeekOfRegex();

    public abstract Pattern getMonthOfRegex();

    public abstract Pattern getInConnectorRegex();

    public abstract Pattern getWithinNextPrefixRegex();

    public abstract Pattern getNextPrefixRegex();

    public abstract Pattern getPastPrefixRegex();

    public abstract Pattern getThisPrefixRegex();

    public abstract Pattern getRestOfDateRegex();

    public abstract Pattern getLaterEarlyPeriodRegex();

    public abstract Pattern getWeekWithWeekDayRangeRegex();

    public abstract Pattern getYearPlusNumberRegex();

    public abstract Pattern getDecadeWithCenturyRegex();

    public abstract Pattern getYearPeriodRegex();

    public abstract Pattern getComplexDatePeriodRegex();

    public abstract Pattern getRelativeDecadeRegex();

    public abstract Pattern getReferenceDatePeriodRegex();

    public abstract Pattern getAgoRegex();

    public abstract Pattern getLaterRegex();

    public abstract Pattern getLessThanRegex();

    public abstract Pattern getMoreThanRegex();

    public abstract Pattern getCenturySuffixRegex();

    public abstract Pattern getRelativeRegex();

    public abstract Pattern getUnspecificEndOfRangeRegex();

    public abstract ImmutableMap<String, String> getUnitMap();

    public abstract ImmutableMap<String, Integer> getCardinalMap();

    public abstract ImmutableMap<String, Integer> getDayOfMonth();

    public abstract ImmutableMap<String, Integer> getMonthOfYear();

    public abstract ImmutableMap<String, String> getSeasonMap();

    public abstract ImmutableMap<String, Integer> getWrittenDecades();

    public abstract ImmutableMap<String, Integer> getNumbers();

    public abstract ImmutableMap<String, Integer> getSpecialDecadeCases();

    public abstract Pattern getAfterNextSuffixRegex();

    public abstract List<String> getLastCardinalTerms();

    public abstract List<String> getFutureTerms();

    public boolean isFuture(String text) {
        return (startsWithKeyword(getFutureTerms(), trimmedText(text)));
    }

    public boolean isLastCardinal(String text) {
        return equalsKeyword(getLastCardinalTerms(), trimmedText(text));
    }

    public boolean isMonthOnly(String text) {
        return isTimePeriodOnly(text, getMonthTerms());
    }

    public boolean isWeekOnly(String text) {
        return !isWeekend(trimmedText(text)) && isTimePeriodOnly(text, getWeekTerms());
    }

    public boolean isWeekend(String text) {
        return isTimePeriodOnly(text, getWeekendTerms());
    }

    public boolean isYearOnly(String text) {
        return  endsWithKeyword(getYearTerms(), trimmedText(text)) ||
            containsKeyword(getYearTerms(), trimmedText(text)) && matchAfterNext(trimmedText(text)).isPresent() ||
            endsWithKeyword(getGenericYearTerms(), trimmedText(text)) && RegExpUtility.getMatches(getUnspecificEndOfRangeRegex(), trimmedText(text)).length > 0;
    }

    private boolean isTimePeriodOnly(String text, List<String> timePeriod) {
        return endsWithKeyword(timePeriod, trimmedText(text)) ||
            containsKeyword(timePeriod, trimmedText(text)) && matchAfterNext(trimmedText(text)).isPresent();
    }

    // isToDate

    public boolean isYearToDate(String text) {
        return equalsKeyword(getYearToDateTerms(), trimmedText(text));
    }

    public boolean isMonthToDate(String text) {
        return equalsKeyword(getMonthToDateTerms(), trimmedText(text));
    }

    // Helper methods
    protected Optional<Match> matchAfterNext(String trimmedText) {
        return Arrays.stream(RegExpUtility.getMatches(getAfterNextSuffixRegex(), trimmedText)).findFirst();
    }

    protected static String trimmedText(String text) {
        return text.trim().toLowerCase();
    }

    protected static Boolean containsKeyword(List<String> term, String trimmedText) {
        return term.stream().anyMatch(o -> trimmedText.contains(o));
    }

    protected static Boolean startsWithKeyword(List<String> term, String trimmedText) {
        return term.stream().anyMatch(o ->  trimmedText.startsWith(o));
    }

    protected static Boolean endsWithKeyword(List<String> term, String trimmedText) {
        return term.stream().anyMatch(o -> trimmedText.endsWith(o));
    }

    protected static Boolean equalsKeyword(List<String> term, String trimmedText) {
        return term.stream().anyMatch(o ->  trimmedText.equals(o));
    }

    public abstract int getSwiftYear(String text);

    public abstract int getSwiftDayOrMonth(String text);

}

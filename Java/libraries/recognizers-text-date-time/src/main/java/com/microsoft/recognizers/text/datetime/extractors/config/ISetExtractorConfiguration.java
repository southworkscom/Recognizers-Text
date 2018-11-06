package com.microsoft.recognizers.text.datetime.extractors.config;

import com.microsoft.recognizers.text.IExtractor;
import com.microsoft.recognizers.text.datetime.config.IOptionsConfiguration;
import com.microsoft.recognizers.text.datetime.extractors.IDateTimeExtractor;
import com.microsoft.recognizers.text.datetime.extractors.IDateTimeListExtractor;
import com.microsoft.recognizers.text.datetime.extractors.IDateTimeZoneExtractor;
import com.microsoft.recognizers.text.matcher.StringMatcher;

import java.util.regex.Pattern;

public interface ISetExtractorConfiguration extends IOptionsConfiguration {
    Pattern getLastRegex();
    Pattern getEachPrefixRegex();
    Pattern getPeriodicRegex();
    Pattern getEachUnitRegex();
    Pattern getEachDayRegex();
    Pattern getBeforeEachDayRegex();
    Pattern getSetWeekDayRegex();
    Pattern getSetEachRegex();
    IDateTimeExtractor getDurationExtractor();
    IDateTimeExtractor getTimeExtractor();
    IDateTimeExtractor getDateExtractor();
    IDateTimeExtractor getDateTimeExtractor();
    IDateTimeExtractor getDatePeriodExtractor();
    IDateTimeExtractor getTimePeriodExtractor();
    IDateTimeExtractor getDateTimePeriodExtractor();
}

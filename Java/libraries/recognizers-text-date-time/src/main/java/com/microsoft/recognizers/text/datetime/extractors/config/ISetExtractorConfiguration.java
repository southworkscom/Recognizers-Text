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
    Pattern getEachDayRegex();
    Pattern getSetEachRegex();
    Pattern getPeriodicRegex();
    Pattern getEachUnitRegex();
    Pattern getEachPrefixRegex();
    Pattern getSetWeekDayRegex();
    Pattern getBeforeEachDayRegex();
    IDateTimeExtractor getTimeExtractor();
    IDateTimeExtractor getDateExtractor();
    IDateTimeExtractor getDateTimeExtractor();
    IDateTimeExtractor getDurationExtractor();
    IDateTimeExtractor getDatePeriodExtractor();
    IDateTimeExtractor getTimePeriodExtractor();
    IDateTimeExtractor getDateTimePeriodExtractor();
}

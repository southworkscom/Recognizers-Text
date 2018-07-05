package com.microsoft.recognizers.text.datetime.extractors.config;

import com.microsoft.recognizers.text.IExtractor;
import com.microsoft.recognizers.text.datetime.config.IOptionsConfiguration;
import com.microsoft.recognizers.text.datetime.extractors.IDateTimeExtractor;

import java.util.regex.Pattern;

public interface IDateTimePeriodExtractorConfiguration extends IOptionsConfiguration {
    String TokenBeforeDate();
    Iterable<Pattern> SimpleCasesRegex();
    Pattern getPrepositionRegex();
    Pattern getTillRegex();
    Pattern getSpecificTimeOfDayRegex();
    Pattern getTimeOfDayRegex();
    Pattern getFollowedUnit();
    Pattern getNumberCombinedWithUnit();
    Pattern getTimeUnitRegex();
    Pattern getPastPrefixRegex();
    Pattern getNextPrefixRegex();
    Pattern getFutureSuffixRegex();
    Pattern getWeekDayRegex();
    Pattern getPeriodTimeOfDayWithDateRegex();
    Pattern getRelativeTimeUnitRegex();
    Pattern getRestOfDateTimeRegex();
    Pattern getGeneralEndingRegex();
    Pattern getMiddlePauseRegex();
    Pattern getAmDescRegex();
    Pattern getPmDescRegex();
    Pattern getWithinNextPrefixRegex();
    Pattern getDateUnitRegex();
    Pattern getPrefixDayRegex();
    Pattern getSuffixRegex();
    Pattern getBeforeRegex();
    Pattern getAfterRegex();
    IExtractor CardinalExtractor();
    IDateTimeExtractor SingleDateExtractor();
    IDateTimeExtractor SingleTimeExtractor();
    IDateTimeExtractor SingleDateTimeExtractor();
    IDateTimeExtractor DurationExtractor();
    IDateTimeExtractor TimePeriodExtractor();
    ResultIndex GetFromTokenIndex(String text);
    boolean HasConnectorToken(String text);
    ResultIndex GetBetweenTokenIndex(String text);
}

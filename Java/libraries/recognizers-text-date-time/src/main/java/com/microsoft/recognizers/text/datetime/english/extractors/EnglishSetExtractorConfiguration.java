package com.microsoft.recognizers.text.datetime.english.extractors;

import com.microsoft.recognizers.text.datetime.DateTimeOptions;
import com.microsoft.recognizers.text.datetime.config.BaseOptionsConfiguration;
import com.microsoft.recognizers.text.datetime.extractors.BaseDateExtractor;
import com.microsoft.recognizers.text.datetime.extractors.BaseDatePeriodExtractor;
import com.microsoft.recognizers.text.datetime.extractors.BaseDateTimeExtractor;
import com.microsoft.recognizers.text.datetime.extractors.BaseDateTimePeriodExtractor;
import com.microsoft.recognizers.text.datetime.extractors.BaseDurationExtractor;
import com.microsoft.recognizers.text.datetime.extractors.BaseTimeExtractor;
import com.microsoft.recognizers.text.datetime.extractors.BaseTimePeriodExtractor;
import com.microsoft.recognizers.text.datetime.extractors.IDateTimeExtractor;
import com.microsoft.recognizers.text.datetime.extractors.config.ISetExtractorConfiguration;
import com.microsoft.recognizers.text.datetime.resources.EnglishDateTime;
import com.microsoft.recognizers.text.utilities.RegExpUtility;

import java.util.regex.Pattern;

public class EnglishSetExtractorConfiguration extends BaseOptionsConfiguration implements ISetExtractorConfiguration {

    public static final Pattern SetLastRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.SetLastRegex, Pattern.CASE_INSENSITIVE);
    public static final Pattern EachDayRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.EachDayRegex, Pattern.CASE_INSENSITIVE);
    public static final Pattern SetEachRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.SetEachRegex, Pattern.CASE_INSENSITIVE);
    public static final Pattern PeriodicRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.PeriodicRegex, Pattern.CASE_INSENSITIVE);
    public static final Pattern EachUnitRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.EachUnitRegex, Pattern.CASE_INSENSITIVE);
    public static final Pattern SetUnitRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.DurationUnitRegex, Pattern.CASE_INSENSITIVE);
    public static final Pattern EachPrefixRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.EachPrefixRegex, Pattern.CASE_INSENSITIVE);
    public static final Pattern SetWeekDayRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.SetWeekDayRegex, Pattern.CASE_INSENSITIVE);

    private IDateTimeExtractor TimeExtractor;
    private IDateTimeExtractor DateExtractor;
    private IDateTimeExtractor DurationExtractor;
    private IDateTimeExtractor DateTimeExtractor;
    private IDateTimeExtractor DatePeriodExtractor;
    private IDateTimeExtractor TimePeriodExtractor;
    private IDateTimeExtractor DateTimePeriodExtractor;

    public EnglishSetExtractorConfiguration() {

        super(DateTimeOptions.None);

        TimeExtractor = new BaseTimeExtractor(new EnglishTimeExtractorConfiguration());
        DateExtractor = new BaseDateExtractor(new EnglishDateExtractorConfiguration());
        DurationExtractor = new BaseDurationExtractor(new EnglishDurationExtractorConfiguration());
        DateTimeExtractor = new BaseDateTimeExtractor(new EnglishDateTimeExtractorConfiguration());
        DatePeriodExtractor = new BaseDatePeriodExtractor(new EnglishDatePeriodExtractorConfiguration());
        TimePeriodExtractor = new BaseTimePeriodExtractor(new EnglishTimePeriodExtractorConfiguration());
        DateTimePeriodExtractor = new BaseDateTimePeriodExtractor(new EnglishDateTimePeriodExtractorConfiguration());
    }


    public final IDateTimeExtractor getTimeExtractor() {
        return TimeExtractor;
    }

    public final IDateTimeExtractor getDateExtractor() {
        return DateExtractor;
    }

    public final IDateTimeExtractor getDurationExtractor() {
        return DurationExtractor;
    }

    public final IDateTimeExtractor getDateTimeExtractor() {
        return DateTimeExtractor;
    }

    public final IDateTimeExtractor getDatePeriodExtractor() {
        return DatePeriodExtractor;
    }

    public final IDateTimeExtractor getTimePeriodExtractor() {
        return TimePeriodExtractor;
    }

    public final IDateTimeExtractor getDateTimePeriodExtractor() {
        return DateTimePeriodExtractor;
    }

    public final Pattern getLastRegex() {
        return SetLastRegex;
    }

    public final Pattern getBeforeEachDayRegex() {
        return null;
    }

    public final Pattern getEachDayRegex() {
        return EachDayRegex;
    }

    public final Pattern getSetEachRegex() {
        return SetEachRegex;
    }

    public final Pattern getPeriodicRegex() {
        return PeriodicRegex;
    }

    public final Pattern getEachUnitRegex() {
        return EachUnitRegex;
    }

    public final Pattern getSetWeekDayRegex() {
        return SetWeekDayRegex;
    }

    public final Pattern getEachPrefixRegex() {
        return EachPrefixRegex;
    }
}

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

    private IDateTimeExtractor timeExtractor;
    private IDateTimeExtractor dateExtractor;
    private IDateTimeExtractor durationExtractor;
    private IDateTimeExtractor dateTimeExtractor;
    private IDateTimeExtractor datePeriodExtractor;
    private IDateTimeExtractor timePeriodExtractor;
    private IDateTimeExtractor dateTimePeriodExtractor;

    public EnglishSetExtractorConfiguration() {

        super(DateTimeOptions.None);

        timeExtractor = new BaseTimeExtractor(new EnglishTimeExtractorConfiguration());
        dateExtractor = new BaseDateExtractor(new EnglishDateExtractorConfiguration());
        durationExtractor = new BaseDurationExtractor(new EnglishDurationExtractorConfiguration());
        dateTimeExtractor = new BaseDateTimeExtractor(new EnglishDateTimeExtractorConfiguration());
        datePeriodExtractor = new BaseDatePeriodExtractor(new EnglishDatePeriodExtractorConfiguration());
        timePeriodExtractor = new BaseTimePeriodExtractor(new EnglishTimePeriodExtractorConfiguration());
        dateTimePeriodExtractor = new BaseDateTimePeriodExtractor(new EnglishDateTimePeriodExtractorConfiguration());
    }


    public final IDateTimeExtractor getTimeExtractor() {
        return timeExtractor;
    }

    public final IDateTimeExtractor getDateExtractor() {
        return dateExtractor;
    }

    public final IDateTimeExtractor getDurationExtractor() {
        return durationExtractor;
    }

    public final IDateTimeExtractor getDateTimeExtractor() {
        return dateTimeExtractor;
    }

    public final IDateTimeExtractor getDatePeriodExtractor() {
        return datePeriodExtractor;
    }

    public final IDateTimeExtractor getTimePeriodExtractor() {
        return timePeriodExtractor;
    }

    public final IDateTimeExtractor getDateTimePeriodExtractor() {
        return dateTimePeriodExtractor;
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

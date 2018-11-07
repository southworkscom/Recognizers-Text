package com.microsoft.recognizers.text.datetime.english.extractors;

import com.microsoft.recognizers.text.IExtractor;
import com.microsoft.recognizers.text.datetime.DateTimeOptions;
import com.microsoft.recognizers.text.datetime.config.BaseOptionsConfiguration;
import com.microsoft.recognizers.text.datetime.extractors.*;
import com.microsoft.recognizers.text.datetime.extractors.config.IMergedExtractorConfiguration;
import com.microsoft.recognizers.text.datetime.resources.EnglishDateTime;
import com.microsoft.recognizers.text.matcher.StringMatcher;
import com.microsoft.recognizers.text.utilities.RegExpUtility;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class EnglishMergedExtractorConfiguration implements IMergedExtractorConfiguration {
    public static final Pattern AfterRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.AfterRegex, Pattern.CASE_INSENSITIVE);
    public static final Pattern SinceRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.SinceRegex, Pattern.CASE_INSENSITIVE);
    public static final Pattern BeforeRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.BeforeRegex, Pattern.CASE_INSENSITIVE);
    public static final Pattern FromToRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.FromToRegex, Pattern.CASE_INSENSITIVE);
    public static final Pattern YearAfterRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.YearAfterRegex, Pattern.CASE_INSENSITIVE);
    public static final Pattern NumberEndingPattern = RegExpUtility.getSafeRegExp(EnglishDateTime.NumberEndingPattern, Pattern.CASE_INSENSITIVE);
    public static final Pattern PrepositionSuffixRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.PrepositionSuffixRegex, Pattern.CASE_INSENSITIVE);
    public static final Pattern SingleAmbiguousMonthRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.SingleAmbiguousMonthRegex, Pattern.CASE_INSENSITIVE);
    public static final Pattern UnspecificDatePeriodRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.UnspecificDatePeriodRegex, Pattern.CASE_INSENSITIVE);

    public final Iterable<Pattern> getFilterWordRegexList()
    {
        return new ArrayList<Pattern>() {
            {
                // one on one
                add(RegExpUtility.getSafeRegExp(EnglishDateTime.OneOnOneRegex, Pattern.CASE_INSENSITIVE));

                // (the)? (day|week|month|year)
                add(RegExpUtility.getSafeRegExp(EnglishDateTime.OneOnOneRegex, Pattern.CASE_INSENSITIVE));
            }
        };
    }

    public final StringMatcher getSuperfluousWordMatcher() { return new StringMatcher(); }

    private DateTimeOptions Options;
    public final DateTimeOptions getOptions() { return Options; }

    private IDateTimeExtractor SetExtractor;
    public final IDateTimeExtractor getSetExtractor() { return SetExtractor; }

    private IExtractor IntegerExtractor;
    public final IExtractor getIntegerExtractor() { return IntegerExtractor; }

    private IDateTimeExtractor DateExtractor;
    public final IDateTimeExtractor getDateExtractor() { return DateExtractor; }

    private IDateTimeExtractor TimeExtractor;
    public final IDateTimeExtractor getTimeExtractor() { return TimeExtractor; }

    private IDateTimeExtractor HolidayExtractor;
    public final IDateTimeExtractor getHolidayExtractor() { return HolidayExtractor; }

    private IDateTimeExtractor DateTimeExtractor;
    public final IDateTimeExtractor getDateTimeExtractor() { return DateTimeExtractor; }

    private IDateTimeExtractor DurationExtractor;
    public final IDateTimeExtractor getDurationExtractor() { return DurationExtractor; }

    private IDateTimeExtractor DatePeriodExtractor;
    public final IDateTimeExtractor getDatePeriodExtractor() { return DatePeriodExtractor; }

    private IDateTimeExtractor TimePeriodExtractor;
    public final IDateTimeExtractor getTimePeriodExtractor() { return TimePeriodExtractor; }

    private IDateTimeZoneExtractor TimeZoneExtractor;
    public final IDateTimeZoneExtractor getTimeZoneExtractor() { return TimeZoneExtractor; }

    private IDateTimeListExtractor DateTimeAltExtractor;
    public final IDateTimeListExtractor getDateTimeAltExtractor() { return DateTimeAltExtractor; }

    private IDateTimeExtractor DateTimePeriodExtractor;
    public final IDateTimeExtractor getDateTimePeriodExtractor() { return DateTimePeriodExtractor; }

    public EnglishMergedExtractorConfiguration(DateTimeOptions options)
    {
        Options = options;

        SetExtractor = new BaseSetExtractor(new EnglishSetExtractorConfiguration());
        DateExtractor = new BaseDateExtractor(new EnglishDateExtractorConfiguration());
        TimeExtractor = new BaseTimeExtractor(new EnglishTimeExtractorConfiguration(options));
        HolidayExtractor = new BaseHolidayExtractor(new EnglishHolidayExtractorConfiguration());
        DatePeriodExtractor = new BaseDatePeriodExtractor(new EnglishDatePeriodExtractorConfiguration());
        DateTimeExtractor = new BaseDateTimeExtractor(new EnglishDateTimeExtractorConfiguration(options));
        DurationExtractor = new BaseDurationExtractor(new EnglishDurationExtractorConfiguration(options));
        TimeZoneExtractor = new BaseTimeZoneExtractor(new EnglishTimeZoneExtractorConfiguration(options));
        DateTimeAltExtractor = new BaseDateTimeAltExtractor(new EnglishDateTimeAltExtractorConfiguration());
        TimePeriodExtractor = new BaseTimePeriodExtractor(new EnglishTimePeriodExtractorConfiguration(options));
        IntegerExtractor = com.microsoft.recognizers.text.number.english.extractors.IntegerExtractor.getInstance();
        DateTimePeriodExtractor = new BaseDateTimePeriodExtractor(new EnglishDateTimePeriodExtractorConfiguration(options));

        if (options != null & options != DateTimeOptions.EnablePreview)
        {
            getSuperfluousWordMatcher().init(EnglishDateTime.SuperfluousWordList);
        }
    }

    public final Pattern getAfterRegex() { return AfterRegex; }
    public final Pattern getSinceRegex() { return SinceRegex; }
    public final Pattern getBeforeRegex() { return BeforeRegex; }
    public final Pattern getFromToRegex() { return FromToRegex; }
    public final Pattern getYearAfterRegex() { return YearAfterRegex; }
    public final Pattern getNumberEndingPattern() { return NumberEndingPattern; }
    public final Pattern getPrepositionSuffixRegex() { return PrepositionSuffixRegex; }
    public final Pattern getSingleAmbiguousMonthRegex() { return SingleAmbiguousMonthRegex; }
    public final Pattern getUnspecificDatePeriodRegex() { return UnspecificDatePeriodRegex; }
}

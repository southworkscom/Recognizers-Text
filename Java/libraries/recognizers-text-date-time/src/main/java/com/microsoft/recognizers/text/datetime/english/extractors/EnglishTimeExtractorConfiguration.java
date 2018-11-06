package com.microsoft.recognizers.text.datetime.english.extractors;

import com.microsoft.recognizers.text.datetime.DateTimeOptions;
import com.microsoft.recognizers.text.datetime.config.BaseOptionsConfiguration;
import com.microsoft.recognizers.text.datetime.extractors.BaseDurationExtractor;
import com.microsoft.recognizers.text.datetime.extractors.BaseTimeExtractor;
import com.microsoft.recognizers.text.datetime.extractors.BaseTimeZoneExtractor;
import com.microsoft.recognizers.text.datetime.extractors.IDateTimeExtractor;
import com.microsoft.recognizers.text.datetime.extractors.config.ITimeExtractorConfiguration;
import com.microsoft.recognizers.text.datetime.resources.EnglishDateTime;
import com.microsoft.recognizers.text.utilities.RegExpUtility;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class EnglishTimeExtractorConfiguration extends BaseOptionsConfiguration implements ITimeExtractorConfiguration {
    // part 1: smallest component
    // --------------------------------------
    public static final Pattern DescRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.DescRegex, Pattern.CASE_INSENSITIVE);
    public static final Pattern HourNumRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.HourNumRegex, Pattern.CASE_INSENSITIVE);
    public static final Pattern MinuteNumRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.MinuteNumRegex, Pattern.CASE_INSENSITIVE);

    // part 2: middle level component
    // --------------------------------------
    // handle "... o'clock"
    public static final Pattern OclockRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.OclockRegex, Pattern.CASE_INSENSITIVE);

    // handle "... afternoon"
    public static final Pattern PmRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.PmRegex, Pattern.CASE_INSENSITIVE);

    // handle "... in the morning"
    public static final Pattern AmRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.AmRegex, Pattern.CASE_INSENSITIVE);

    // handle "half past ..." "a quarter to ..."
    // rename 'min' group to 'deltamin'
    public static final Pattern LessThanOneHour = RegExpUtility.getSafeRegExp(EnglishDateTime.LessThanOneHour, Pattern.CASE_INSENSITIVE);

    // handle "six thirty", "six twenty one"
    public static final Pattern BasicTime = RegExpUtility.getSafeRegExp(EnglishDateTime.BasicTime, Pattern.CASE_INSENSITIVE);
    public static final Pattern TimePrefix = RegExpUtility.getSafeRegExp(EnglishDateTime.TimePrefix, Pattern.CASE_INSENSITIVE);
    public static final Pattern TimeSuffix = RegExpUtility.getSafeRegExp(EnglishDateTime.TimeSuffix, Pattern.CASE_INSENSITIVE);
    public static final Pattern WrittenTimeRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.WrittenTimeRegex, Pattern.CASE_INSENSITIVE);

    // handle special time such as 'at midnight', 'midnight', 'midday'
    public static final Pattern MiddayRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.MiddayRegex, Pattern.CASE_INSENSITIVE);
    public static final Pattern MidTimeRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.MidTimeRegex, Pattern.CASE_INSENSITIVE);
    public static final Pattern MidnightRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.MidnightRegex, Pattern.CASE_INSENSITIVE);
    public static final Pattern MidmorningRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.MidmorningRegex, Pattern.CASE_INSENSITIVE);
    public static final Pattern MidafternoonRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.MidafternoonRegex, Pattern.CASE_INSENSITIVE);

    // part 3: regex for time
    // --------------------------------------
    // handle "at four" "at 3"
    public static final Pattern AtRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.AtRegex, Pattern.CASE_INSENSITIVE);
    public static final Pattern IshRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.IshRegex, Pattern.CASE_INSENSITIVE);
    public static final Pattern TimeUnitRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.TimeUnitRegex, Pattern.CASE_INSENSITIVE);
    public static final Pattern ConnectNumRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.ConnectNumRegex, Pattern.CASE_INSENSITIVE);
    public static final Pattern TimeBeforeAfterRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.TimeBeforeAfterRegex, Pattern.CASE_INSENSITIVE);

    public static final Iterable<Pattern> TimeRegexList = new ArrayList<Pattern>()
    {
        // (three min past)? seven|7|(senven thirty) pm
        {add(RegExpUtility.getSafeRegExp(EnglishDateTime.TimeRegex1, Pattern.CASE_INSENSITIVE));

        // (three min past)? 3:00(:00)? (pm)?
        add(RegExpUtility.getSafeRegExp(EnglishDateTime.TimeRegex2, Pattern.CASE_INSENSITIVE));

        // (three min past)? 3.00 (pm)
        add(RegExpUtility.getSafeRegExp(EnglishDateTime.TimeRegex3, Pattern.CASE_INSENSITIVE));

        // (three min past) (five thirty|seven|7|7:00(:00)?) (pm)? (in the night)
        add(RegExpUtility.getSafeRegExp(EnglishDateTime.TimeRegex4, Pattern.CASE_INSENSITIVE));

        // (three min past) (five thirty|seven|7|7:00(:00)?) (pm)?
        add(RegExpUtility.getSafeRegExp(EnglishDateTime.TimeRegex5, Pattern.CASE_INSENSITIVE));

        // (five thirty|seven|7|7:00(:00)?) (pm)? (in the night)
        add(RegExpUtility.getSafeRegExp(EnglishDateTime.TimeRegex6, Pattern.CASE_INSENSITIVE));

        // (in the night) at (five thirty|seven|7|7:00(:00)?) (pm)?
        add(RegExpUtility.getSafeRegExp(EnglishDateTime.TimeRegex7, Pattern.CASE_INSENSITIVE));

        // (in the night) (five thirty|seven|7|7:00(:00)?) (pm)?
        add(RegExpUtility.getSafeRegExp(EnglishDateTime.TimeRegex8, Pattern.CASE_INSENSITIVE));

        add(RegExpUtility.getSafeRegExp(EnglishDateTime.TimeRegex9, Pattern.CASE_INSENSITIVE));

        // (three min past)? 3h00 (pm)?
        add(RegExpUtility.getSafeRegExp(EnglishDateTime.TimeRegex10, Pattern.CASE_INSENSITIVE));

        // 340pm
        add(ConnectNumRegex);}
    };

    public final Iterable<Pattern> getTimeRegexList()
    {
        return TimeRegexList;
    }

    public final Pattern getAtRegex()
    {
        return AtRegex;
    }

    public final Pattern getIshRegex()
    {
        return IshRegex;
    }

    public final Pattern getTimeBeforeAfterRegex()
    {
        return TimeBeforeAfterRegex;
    }

    private IDateTimeExtractor DurationExtractor;
    public final IDateTimeExtractor getDurationExtractor()
    {
        return DurationExtractor;
    }

    private IDateTimeExtractor TimeZoneExtractor;
    public final IDateTimeExtractor getTimeZoneExtractor()
    {
        return TimeZoneExtractor;
    }


    public EnglishTimeExtractorConfiguration()
    {
        this(DateTimeOptions.None);
    }

    //C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
    //ORIGINAL LINE: public EnglishTimeExtractorConfiguration(DateTimeOptions options = DateTimeOptions.None)
    public EnglishTimeExtractorConfiguration(DateTimeOptions options)
    {
        super(options);
        DurationExtractor = new BaseDurationExtractor(new EnglishDurationExtractorConfiguration());
        //TimeZoneExtractor = new BaseTimeZoneExtractor(new EnglishTimeZoneExtractorConfiguration(options));
    }
}

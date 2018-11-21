package com.microsoft.recognizers.text.datetime.english.parsers;

import com.google.common.collect.ImmutableMap;
import com.microsoft.recognizers.text.datetime.config.BaseOptionsConfiguration;
import com.microsoft.recognizers.text.datetime.english.extractors.EnglishSetExtractorConfiguration;
import com.microsoft.recognizers.text.datetime.extractors.IDateTimeExtractor;
import com.microsoft.recognizers.text.datetime.parsers.IDateTimeParser;
import com.microsoft.recognizers.text.datetime.parsers.config.ICommonDateTimeParserConfiguration;
import com.microsoft.recognizers.text.datetime.parsers.config.ISetParserConfiguration;
import com.microsoft.recognizers.text.datetime.utilities.MatchedTimexResult;

import java.util.Locale;
import java.util.regex.Pattern;

public class EnglishSetParserConfiguration extends BaseOptionsConfiguration implements ISetParserConfiguration {
    private IDateTimeParser TimeParser;
    public final IDateTimeParser getTimeParser() { return TimeParser; }

    private IDateTimeParser DateParser;
    public final IDateTimeParser getDateParser() { return DateParser; }

    private ImmutableMap<String, String> UnitMap;
    public final ImmutableMap<String, String> getUnitMap() { return UnitMap; }

    private IDateTimeParser DateTimeParser;
    public final IDateTimeParser getDateTimeParser() { return DateTimeParser; }

    private IDateTimeParser DurationParser;
    public final IDateTimeParser getDurationParser() { return DurationParser; }

    private IDateTimeExtractor TimeExtractor;
    public final IDateTimeExtractor getTimeExtractor() { return TimeExtractor; }

    private IDateTimeExtractor DateExtractor;
    public final IDateTimeExtractor getDateExtractor() { return DateExtractor; }

    private IDateTimeParser DatePeriodParser;
    public final IDateTimeParser getDatePeriodParser() { return DatePeriodParser; }

    private IDateTimeParser TimePeriodParser;
    public final IDateTimeParser getTimePeriodParser() { return TimePeriodParser; }

    private IDateTimeExtractor DurationExtractor;
    public final IDateTimeExtractor getDurationExtractor() { return DurationExtractor; }

    private IDateTimeExtractor DateTimeExtractor;
    public final IDateTimeExtractor getDateTimeExtractor() { return DateTimeExtractor; }

    private IDateTimeParser DateTimePeriodParser;
    public final IDateTimeParser getDateTimePeriodParser() { return DateTimePeriodParser; }

    private IDateTimeExtractor DatePeriodExtractor;
    public final IDateTimeExtractor getDatePeriodExtractor() { return DatePeriodExtractor; }

    private IDateTimeExtractor TimePeriodExtractor;
    public final IDateTimeExtractor getTimePeriodExtractor() { return TimePeriodExtractor; }

    private IDateTimeExtractor DateTimePeriodExtractor;
    public final IDateTimeExtractor getDateTimePeriodExtractor() { return DateTimePeriodExtractor; }

    private Pattern EachDayRegex;
    public final Pattern getEachDayRegex() { return EachDayRegex; }

    private Pattern SetEachRegex;
    public final Pattern getSetEachRegex() { return SetEachRegex; }

    private Pattern PeriodicRegex;
    public final Pattern getPeriodicRegex() { return PeriodicRegex; }

    private Pattern EachUnitRegex;
    public final Pattern getEachUnitRegex() { return EachUnitRegex; }

    private Pattern SetWeekDayRegex;
    public final Pattern getSetWeekDayRegex() { return SetWeekDayRegex; }

    private Pattern EachPrefixRegex;
    public final Pattern getEachPrefixRegex() { return EachPrefixRegex; }

    public EnglishSetParserConfiguration(ICommonDateTimeParserConfiguration config)
    {
        super(config.getOptions());

        TimeExtractor = config.getTimeExtractor();
        DateExtractor = config.getDateExtractor();
        DateTimeExtractor = config.getDateTimeExtractor();
        DurationExtractor = config.getDurationExtractor();
        DatePeriodExtractor = config.getDatePeriodExtractor();
        TimePeriodExtractor = config.getTimePeriodExtractor();
        DateTimePeriodExtractor = config.getDateTimeExtractor();

        UnitMap = config.getUnitMap();
        TimeParser = config.getTimeParser();
        DateParser = config.getDateParser();
        DateTimeParser = config.getDateTimeParser();
        DurationParser = config.getDurationParser();
        DatePeriodParser = config.getDatePeriodParser();
        TimePeriodParser = config.getTimePeriodParser();
        DateTimePeriodParser = config.getDateTimePeriodParser();

        EachDayRegex = EnglishSetExtractorConfiguration.EachDayRegex;
        SetEachRegex = EnglishSetExtractorConfiguration.SetEachRegex;
        EachUnitRegex = EnglishSetExtractorConfiguration.EachUnitRegex;
        PeriodicRegex = EnglishSetExtractorConfiguration.PeriodicRegex;
        EachPrefixRegex = EnglishSetExtractorConfiguration.EachPrefixRegex;
        SetWeekDayRegex = EnglishSetExtractorConfiguration.SetWeekDayRegex;
    }

    public MatchedTimexResult getMatchedDailyTimex(String text) {
        MatchedTimexResult result = new MatchedTimexResult();
        String trimmedText = text.trim().toLowerCase(Locale.ROOT);

        if (trimmedText.equals("daily")) result = result.WithTimex("P1D");
        else if (trimmedText.equals("weekly")) result = result.WithTimex("P1W");
        else if (trimmedText.equals("biweekly")) result = result.WithTimex("P2W");
        else if (trimmedText.equals("monthly")) result = result.WithTimex("P1M");
        else if (trimmedText.equals("yearly") || trimmedText.equals("annually") || trimmedText.equals("annual")) result = result.WithTimex("P1Y");

        if (result.timex != "") result = result.WithResult(true);

        return result;
    }

    public MatchedTimexResult getMatchedUnitTimex(String text) {
        MatchedTimexResult result = new MatchedTimexResult();
        String trimmedText = text.trim().toLowerCase(Locale.ROOT);

        if (trimmedText.equals("day")) result = result.WithTimex("P1D");
        else if (trimmedText.equals("week")) result = result.WithTimex("P1W");
        else if (trimmedText.equals("month")) result = result.WithTimex("P1M");
        else if (trimmedText.equals("year")) result = result.WithTimex("P1Y");

        if (result.timex != "") result = result.WithResult(true);

        return result;
    }
}

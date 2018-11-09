package com.microsoft.recognizers.text.datetime.english.extractors;

import com.microsoft.recognizers.text.IExtractor;
import com.microsoft.recognizers.text.datetime.extractors.IDateTimeExtractor;
import com.microsoft.recognizers.text.datetime.extractors.config.ResultIndex;
import com.microsoft.recognizers.text.datetime.utilities.IDateTimeUtilityConfiguration;
import com.microsoft.recognizers.text.number.english.extractors.IntegerExtractor;
import com.microsoft.recognizers.text.utilities.RegExpUtility;
import com.microsoft.recognizers.text.datetime.DateTimeOptions;
import com.microsoft.recognizers.text.datetime.resources.EnglishDateTime;
import com.microsoft.recognizers.text.datetime.extractors.BaseTimeExtractor;
import com.microsoft.recognizers.text.datetime.config.BaseOptionsConfiguration;
import com.microsoft.recognizers.text.datetime.extractors.config.ITimePeriodExtractorConfiguration;
import com.microsoft.recognizers.text.datetime.english.parsers.EnglishDatetimeUtilityConfiguration;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static com.microsoft.recognizers.text.datetime.resources.EnglishDateTime.TokenBeforeDate;

public class EnglishTimePeriodExtractorConfiguration extends BaseOptionsConfiguration implements ITimePeriodExtractorConfiguration {
    private String TokenBeforeDate;
    public final String getTokenBeforeDate() { return TokenBeforeDate; }

    public static final Pattern AmRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.AmRegex, Pattern.CASE_INSENSITIVE);
    public static final Pattern PmRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.PmRegex, Pattern.CASE_INSENSITIVE);
    public static final Pattern HourRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.HourRegex, Pattern.CASE_INSENSITIVE);
    public static final Pattern TillRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.TillRegex, Pattern.CASE_INSENSITIVE);
    public static final Pattern PeriodDescRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.DescRegex, Pattern.CASE_INSENSITIVE);
    public static final Pattern PureNumFromTo = RegExpUtility.getSafeRegExp(EnglishDateTime.PureNumFromTo, Pattern.CASE_INSENSITIVE);
    public static final Pattern TimeUnitRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.TimeUnitRegex, Pattern.CASE_INSENSITIVE);
    public static final Pattern TimeOfDayRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.TimeOfDayRegex, Pattern.CASE_INSENSITIVE);
    public static final Pattern PrepositionRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.PrepositionRegex, Pattern.CASE_INSENSITIVE);
    public static final Pattern TimeFollowedUnit = RegExpUtility.getSafeRegExp(EnglishDateTime.TimeFollowedUnit, Pattern.CASE_INSENSITIVE);
    public static final Pattern PureNumBetweenAnd = RegExpUtility.getSafeRegExp(EnglishDateTime.PureNumBetweenAnd, Pattern.CASE_INSENSITIVE);
    public static final Pattern GeneralEndingRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.GeneralEndingRegex, Pattern.CASE_INSENSITIVE);
    public static final Pattern PeriodHourNumRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.PeriodHourNumRegex, Pattern.CASE_INSENSITIVE);
    public static final Pattern SpecificTimeFromTo = RegExpUtility.getSafeRegExp(EnglishDateTime.SpecificTimeFromTo, Pattern.CASE_INSENSITIVE);
    public static final Pattern SpecificTimeBetweenAnd = RegExpUtility.getSafeRegExp(EnglishDateTime.SpecificTimeBetweenAnd, Pattern.CASE_INSENSITIVE);
    public static final Pattern SpecificTimeOfDayRegex = RegExpUtility.getSafeRegExp(EnglishDateTime.SpecificTimeOfDayRegex, Pattern.CASE_INSENSITIVE);
    public static final Pattern TimeNumberCombinedWithUnit = RegExpUtility.getSafeRegExp(EnglishDateTime.TimeNumberCombinedWithUnit, Pattern.CASE_INSENSITIVE);

    public EnglishTimePeriodExtractorConfiguration()
    {
        this(DateTimeOptions.None);
    }

    //C# TO JAVA CONVERTER WARNING: The following constructor is declared outside of its associated class:
    //ORIGINAL LINE: public EnglishTimePeriodExtractorConfiguration(DateTimeOptions options = DateTimeOptions.None)
    //C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
    public EnglishTimePeriodExtractorConfiguration(DateTimeOptions options)
    {
        super(options);
        TokenBeforeDate = EnglishDateTime.TokenBeforeDate;
        SingleTimeExtractor = new BaseTimeExtractor(new EnglishTimeExtractorConfiguration(options));
        UtilityConfiguration = new EnglishDatetimeUtilityConfiguration();
        IntegerExtractor = com.microsoft.recognizers.text.number.english.extractors.IntegerExtractor.getInstance();
    }

    private IDateTimeUtilityConfiguration UtilityConfiguration;
    public final IDateTimeUtilityConfiguration getUtilityConfiguration() { return UtilityConfiguration; }

    private IDateTimeExtractor SingleTimeExtractor;
    public final IDateTimeExtractor getSingleTimeExtractor() { return SingleTimeExtractor; }

    private IExtractor IntegerExtractor;
    public final IExtractor getIntegerExtractor() { return IntegerExtractor; }

    public Iterable<Pattern> getSimpleCasesRegex() {
        return getSimpleCasesRegex;
    }
    public final Iterable<Pattern> getSimpleCasesRegex = new ArrayList<Pattern>() {
        {add(PureNumFromTo);
        add(PureNumBetweenAnd);
        add(SpecificTimeFromTo);
        add(SpecificTimeBetweenAnd);}
    };

    public final Pattern getTillRegex() { return TillRegex; }
    public final Pattern getTimeOfDayRegex() { return TimeOfDayRegex; }
    public final Pattern getGeneralEndingRegex() { return GeneralEndingRegex; }

    public final ResultIndex GetFromTokenIndex(String input)
    {
        ResultIndex result = new ResultIndex(false, -1);
        if (input.endsWith("from"))
        {
            result = result.withIndex( input.lastIndexOf("from" ) );
            result = result.withResult(true);
        }

        return result;
    }

    public final ResultIndex GetBetweenTokenIndex(String input)
    {
        ResultIndex result = new ResultIndex(false, -1);
        if (input.endsWith("between"))
        {
            result = result.withIndex( input.lastIndexOf("between" ) );
            result = result.withResult(true);
        }

        return result;
    }

    public final boolean HasConnectorToken(String input)
    {
        return input.equals("and");
    }
}

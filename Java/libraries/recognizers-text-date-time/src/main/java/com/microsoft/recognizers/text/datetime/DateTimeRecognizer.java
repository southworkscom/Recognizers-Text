package com.microsoft.recognizers.text.datetime;

import com.google.common.collect.ImmutableMap;
import com.microsoft.recognizers.text.*;

import java.util.List;
import java.util.function.Function;

public class DateTimeRecognizer extends Recognizer<DateTimeOptions> {

    public DateTimeRecognizer() {
        this(null, DateTimeOptions.None, true);
    }

    public DateTimeRecognizer(String culture) {
        this(culture, DateTimeOptions.None, false);
    }

    public DateTimeRecognizer(DateTimeOptions options) {
        this(null, options, true);
    }

    public DateTimeRecognizer(DateTimeOptions options, boolean lazyInitialization) {
        this(null, options, lazyInitialization);
    }

    public DateTimeRecognizer(String culture, DateTimeOptions options, boolean lazyInitialization) {
        super(culture, options, lazyInitialization);
    }
/*
    public CurrencyModel getCurrencyModel() {
        return getCurrencyModel(null, true);
    }

    public CurrencyModel getCurrencyModel(String culture, boolean fallbackToDefaultCulture) {
        return getModel(CurrencyModel.class, culture, fallbackToDefaultCulture);
    }

    //region Helper methods for less verbosity
    public static List<ModelResult> recognizeCurrency(String query, String culture) {
        return recognizeByModel(recognizer -> recognizer.getCurrencyModel(culture, true), query, DateTimeOptions.None);
    }

    public static List<ModelResult> recognizeCurrency(String query, String culture, DateTimeOptions options) {
        return recognizeByModel(recognizer -> recognizer.getCurrencyModel(culture, true), query, options);
    }

    public static List<ModelResult> recognizeCurrency(String query, String culture, DateTimeOptions options, boolean fallbackToDefaultCulture) {
        return recognizeByModel(recognizer -> recognizer.getCurrencyModel(culture, fallbackToDefaultCulture), query, options);
    }
*/
    //endregion

    private static List<ModelResult> recognizeByModel(Function<DateTimeRecognizer, IModel> getModelFun, String query, DateTimeOptions options) {
        DateTimeRecognizer recognizer = new DateTimeRecognizer(options);
        IModel model = getModelFun.apply(recognizer);
        return model.parse(query);
    }

    @Override
    protected void initializeConfiguration() {
/*
        //region English
        registerModel(CurrencyModel.class, Culture.English, (options) ->
                new CurrencyModel(ImmutableMap.of(
                        new BaseMergedUnitExtractor(new com.microsoft.recognizers.text.numberwithunit.english.extractors.CurrencyExtractorConfiguration()),
                        new BaseMergedUnitParser(new com.microsoft.recognizers.text.numberwithunit.english.parsers.CurrencyParserConfiguration()))));
        registerModel(TemperatureModel.class, Culture.English, (options) ->
                new TemperatureModel(ImmutableMap.of(
                        new NumberWithUnitExtractor(new com.microsoft.recognizers.text.numberwithunit.english.extractors.TemperatureExtractorConfiguration()),
                        new NumberWithUnitParser(new com.microsoft.recognizers.text.numberwithunit.english.parsers.TemperatureParserConfiguration()))));
        registerModel(DimensionModel.class, Culture.English, (options) ->
                new DimensionModel(ImmutableMap.of(
                        new NumberWithUnitExtractor(new com.microsoft.recognizers.text.numberwithunit.english.extractors.DimensionExtractorConfiguration()),
                        new NumberWithUnitParser(new com.microsoft.recognizers.text.numberwithunit.english.parsers.DimensionParserConfiguration()))));
        registerModel(AgeModel.class, Culture.English, (options) ->
                new AgeModel(ImmutableMap.of(
                        new NumberWithUnitExtractor(new com.microsoft.recognizers.text.numberwithunit.english.extractors.AgeExtractorConfiguration()),
                        new NumberWithUnitParser(new com.microsoft.recognizers.text.numberwithunit.english.parsers.AgeParserConfiguration()))));
        //endregion
*/
    }
}

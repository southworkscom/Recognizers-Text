package com.microsoft.recognizers.text.number;

import com.microsoft.recognizers.text.Culture;
import com.microsoft.recognizers.text.IModel;
import com.microsoft.recognizers.text.ModelResult;
import com.microsoft.recognizers.text.Recognizer;
import com.microsoft.recognizers.text.number.english.parsers.EnglishNumberParserConfiguration;
import com.microsoft.recognizers.text.number.english.parsers.EnglishNumberRangeParserConfiguration;
import com.microsoft.recognizers.text.number.models.NumberModel;
import com.microsoft.recognizers.text.number.models.NumberRangeModel;
import com.microsoft.recognizers.text.number.models.OrdinalModel;
import com.microsoft.recognizers.text.number.models.PercentModel;
import com.microsoft.recognizers.text.number.parsers.*;
import com.microsoft.recognizers.text.number.spanish.parsers.SpanishNumberParserConfiguration;

import java.util.List;
import java.util.function.Function;

public class NumberRecognizer extends Recognizer<NumberOptions> {

    public NumberRecognizer() {
        this(null, NumberOptions.None, true);
    }

    public NumberRecognizer(String culture) {
        this(culture, NumberOptions.None, false);
    }

    public NumberRecognizer(NumberOptions numberOptions) {
        this(null, numberOptions, true);
    }

    public NumberRecognizer(NumberOptions numberOptions, boolean lazyInitialization) {
        this(null, numberOptions, lazyInitialization);
    }

    protected NumberRecognizer(String culture, NumberOptions numberOptions, boolean lazyInitialization) {
        super(culture, numberOptions, lazyInitialization);
    }

    public NumberModel getNumberModel(String culture, boolean fallbackToDefaultCulture) {
        return getModel(NumberModel.class, culture, fallbackToDefaultCulture);
    }

    public OrdinalModel getOrdinalModel(String culture, boolean fallbackToDefaultCulture) {
        return getModel(OrdinalModel.class, culture, fallbackToDefaultCulture);
    }

    public PercentModel getPercentageModel(String culture, boolean fallbackToDefaultCulture) {
        return getModel(PercentModel.class, culture, fallbackToDefaultCulture);
    }

    public NumberRangeModel getNumberRangeModel(String culture, boolean fallbackToDefaultCulture) {
        return getModel(NumberRangeModel.class, culture, fallbackToDefaultCulture);
    }

    public static List<ModelResult> recognizeNumber(String query, String culture, NumberOptions options, boolean fallbackToDefaultCulture) {
        return RecognizeByModel((NumberRecognizer recognizer) -> recognizer.getNumberModel(culture, fallbackToDefaultCulture), query, options);
    }

    public static List<ModelResult> recognizeOrdinal(String query, String culture, NumberOptions options, boolean fallbackToDefaultCulture) {
        return RecognizeByModel((NumberRecognizer recognizer) -> recognizer.getOrdinalModel(culture, fallbackToDefaultCulture), query, options);
    }

    public static List<ModelResult> recognizePercentage(String query, String culture, NumberOptions options, boolean fallbackToDefaultCulture) {
        return RecognizeByModel((NumberRecognizer recognizer) -> recognizer.getPercentageModel(culture, fallbackToDefaultCulture), query, options);
    }

    public static List<ModelResult> recognizeNumberRange(String query, String culture, NumberOptions options, boolean fallbackToDefaultCulture) {
        return RecognizeByModel((NumberRecognizer recognizer) -> recognizer.getNumberRangeModel(culture, fallbackToDefaultCulture), query, options);
    }

    private static List<ModelResult> RecognizeByModel(Function<NumberRecognizer, IModel> getModelFun, String query, NumberOptions options) {
        NumberRecognizer recognizer = new NumberRecognizer(options);
        IModel model = getModelFun.apply(recognizer);
        return model.parse(query);
    }

    @Override
    protected void initializeConfiguration() {
        //region English

        registerModel(NumberModel.class, Culture.English, (options) -> new NumberModel(
                AgnosticNumberParserFactory.getParser(AgnosticNumberParserType.Number, new EnglishNumberParserConfiguration(options)),
                com.microsoft.recognizers.text.number.english.extractors.NumberExtractor.getInstance(NumberMode.PureNumber, options)));
        registerModel(OrdinalModel.class, Culture.English, (options) -> new OrdinalModel(
                AgnosticNumberParserFactory.getParser(AgnosticNumberParserType.Ordinal, new EnglishNumberParserConfiguration(options)),
                com.microsoft.recognizers.text.number.english.extractors.OrdinalExtractor.getInstance()));
        registerModel(PercentModel.class, Culture.English, (options) -> new PercentModel(
                AgnosticNumberParserFactory.getParser(AgnosticNumberParserType.Percentage, new EnglishNumberParserConfiguration(options)),
                new com.microsoft.recognizers.text.number.english.extractors.PercentageExtractor(options)));
        registerModel(NumberRangeModel.class, Culture.English, (options) -> new NumberRangeModel(
                new BaseNumberRangeParser(new EnglishNumberRangeParserConfiguration()),
                new com.microsoft.recognizers.text.number.english.extractors.NumberRangeExtractor()));

        //endregion

        //region Spanish
        registerModel(NumberModel.class, Culture.Spanish, (options) -> new NumberModel(
                AgnosticNumberParserFactory.getParser(AgnosticNumberParserType.Number, new SpanishNumberParserConfiguration()),
                new com.microsoft.recognizers.text.number.spanish.extractors.NumberExtractor(NumberMode.PureNumber)));
        registerModel(OrdinalModel.class, Culture.Spanish, (options) -> new OrdinalModel(
                AgnosticNumberParserFactory.getParser(AgnosticNumberParserType.Ordinal, new SpanishNumberParserConfiguration()),
                new com.microsoft.recognizers.text.number.spanish.extractors.OrdinalExtractor()));
        registerModel(PercentModel.class, Culture.Spanish, (options) -> new PercentModel(
                AgnosticNumberParserFactory.getParser(AgnosticNumberParserType.Percentage, new SpanishNumberParserConfiguration()),
                new com.microsoft.recognizers.text.number.spanish.extractors.PercentageExtractor()));
        //endregion
    }
}

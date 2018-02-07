import { IModel, ModelResult, Recognizer } from "@microsoft/recognizers-text";
import { Culture } from "@microsoft/recognizers-text-number";
import { IDateTimeModel, DateTimeModel } from "./models";
import { BaseMergedParser, BaseMergedExtractor } from "./baseMerged";
import { EnglishCommonDateTimeParserConfiguration } from "./english/baseConfiguration";
import { EnglishMergedExtractorConfiguration, EnglishMergedParserConfiguration } from "./english/mergedConfiguration";
import { SpanishMergedParserConfiguration, SpanishMergedExtractorConfiguration } from "./spanish/mergedConfiguration";
import { FrenchMergedParserConfiguration, FrenchMergedExtractorConfiguration } from "./french/mergedConfiguration";
import { ChineseMergedExtractor, ChineseMergedParser, ChineseFullMergedParser } from "./chinese/mergedConfiguration";

export enum DateTimeOptions {
    None = 0, SkipFromToMerge = 1, SplitDateAndTime = 2, Calendar = 4 
}

export default class DateTimeRecognizer extends Recognizer<DateTimeOptions> {
    constructor(culture: string, options: DateTimeOptions) {
        super(culture, options);
    }

    protected InitializeConfiguration() {
        // English models
        this.registerModel("DateTimeModel", Culture.English, new DateTimeModel(
            new BaseMergedParser(new EnglishMergedParserConfiguration(new EnglishCommonDateTimeParserConfiguration()), this.RecognizerOptions),
            new BaseMergedExtractor(new EnglishMergedExtractorConfiguration(), this.RecognizerOptions)
        ));

        // Spanish models
        this.registerModel("DateTimeModel", Culture.Spanish, new DateTimeModel(
            new BaseMergedParser(new SpanishMergedParserConfiguration(), this.RecognizerOptions),
            new BaseMergedExtractor(new SpanishMergedExtractorConfiguration(), this.RecognizerOptions)
        ));

        // Chinese models
        this.registerModel("DateTimeModel", Culture.Chinese, new DateTimeModel(
            new ChineseFullMergedParser(),
            new ChineseMergedExtractor(this.RecognizerOptions)
        ));

        // French models
        this.registerModel("DateTimeModel", Culture.French, new DateTimeModel(
            new BaseMergedParser(new FrenchMergedParserConfiguration(), this.RecognizerOptions),
            new BaseMergedExtractor(new FrenchMergedExtractorConfiguration(), this.RecognizerOptions)
        ));
    }

    getDateTimeModel(culture: string = ""): IDateTimeModel {
        return this.getModel("DateTimeModel", culture);
    }

    public static getSingleCultureInstance(cultureCode: string, options: DateTimeOptions = DateTimeOptions.None): DateTimeRecognizer {
        return new DateTimeRecognizer(cultureCode, options);
    }
}
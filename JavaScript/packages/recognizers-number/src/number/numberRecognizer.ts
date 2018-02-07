import { IModel } from "@microsoft/recognizers-text";
import { Recognizer } from "@microsoft/recognizers-text";
import { Culture } from "../culture";
import { NumberMode, NumberModel, OrdinalModel, PercentModel } from "./models";
import { AgnosticNumberParserType, AgnosticNumberParserFactory } from "./agnosticNumberParser";
import { EnglishNumberParserConfiguration } from "./english/parserConfiguration";
import { SpanishNumberParserConfiguration } from "./spanish/parserConfiguration";
import { PortugueseNumberParserConfiguration } from "./portuguese/parserConfiguration";
import { FrenchNumberParserConfiguration } from "./french/parserConfiguration";
import { ChineseNumberParserConfiguration } from "./chinese/parserConfiguration";
import { EnglishNumberExtractor, EnglishOrdinalExtractor, EnglishPercentageExtractor } from "./english/extractors";
import { SpanishNumberExtractor, SpanishOrdinalExtractor, SpanishPercentageExtractor } from "./spanish/extractors";
import { PortugueseNumberExtractor, PortugueseOrdinalExtractor, PortuguesePercentageExtractor } from "./portuguese/extractors";
import { FrenchNumberExtractor, FrenchOrdinalExtractor, FrenchPercentageExtractor } from "./french/extractors";
import { ChineseNumberExtractor, ChineseOrdinalExtractor, ChinesePercentageExtractor } from "./chinese/extractors";

export enum NumberOptions {
    None = 0,
}

export default class NumberRecognizer extends Recognizer<NumberOptions> {

    private constructor(culture: string, options: NumberOptions) {
        super(culture, options);
    }

    protected InitializeConfiguration() {
        // English models
        this.registerModel("NumberModel", Culture.English, new NumberModel(
            AgnosticNumberParserFactory.getParser(AgnosticNumberParserType.Number, new EnglishNumberParserConfiguration()),
            new EnglishNumberExtractor(NumberMode.PureNumber)));
        this.registerModel("OrdinalModel", Culture.English, new OrdinalModel(
            AgnosticNumberParserFactory.getParser(AgnosticNumberParserType.Ordinal, new EnglishNumberParserConfiguration()),
            new EnglishOrdinalExtractor()));
        this.registerModel("PercentModel", Culture.English, new PercentModel(
            AgnosticNumberParserFactory.getParser(AgnosticNumberParserType.Percentage, new EnglishNumberParserConfiguration()),
            new EnglishPercentageExtractor()));

        // Spanish models
        this.registerModel("NumberModel", Culture.Spanish, new NumberModel(
            AgnosticNumberParserFactory.getParser(AgnosticNumberParserType.Number, new SpanishNumberParserConfiguration()),
            new SpanishNumberExtractor(NumberMode.PureNumber)));
        this.registerModel("OrdinalModel", Culture.Spanish, new OrdinalModel(
            AgnosticNumberParserFactory.getParser(AgnosticNumberParserType.Ordinal, new SpanishNumberParserConfiguration()),
            new SpanishOrdinalExtractor()));
        this.registerModel("PercentModel", Culture.Spanish, new PercentModel(
            AgnosticNumberParserFactory.getParser(AgnosticNumberParserType.Percentage, new SpanishNumberParserConfiguration()),
            new SpanishPercentageExtractor()));

        // Portuguese models
        this.registerModel("NumberModel", Culture.Portuguese, new NumberModel(
            AgnosticNumberParserFactory.getParser(AgnosticNumberParserType.Number, new PortugueseNumberParserConfiguration()),
            new PortugueseNumberExtractor(NumberMode.PureNumber)));
        this.registerModel("OrdinalModel", Culture.Portuguese, new OrdinalModel(
            AgnosticNumberParserFactory.getParser(AgnosticNumberParserType.Ordinal, new PortugueseNumberParserConfiguration()),
            new PortugueseOrdinalExtractor()));
        this.registerModel("PercentModel", Culture.Portuguese, new PercentModel(
            AgnosticNumberParserFactory.getParser(AgnosticNumberParserType.Percentage, new PortugueseNumberParserConfiguration()),
            new PortuguesePercentageExtractor()));

        // Chinese models
        this.registerModel("NumberModel", Culture.Chinese, new NumberModel(
            AgnosticNumberParserFactory.getParser(AgnosticNumberParserType.Number, new ChineseNumberParserConfiguration()),
            new ChineseNumberExtractor()));
        this.registerModel("OrdinalModel", Culture.Chinese, new OrdinalModel(
            AgnosticNumberParserFactory.getParser(AgnosticNumberParserType.Ordinal, new ChineseNumberParserConfiguration()),
            new ChineseOrdinalExtractor()));
        this.registerModel("PercentModel", Culture.Chinese, new PercentModel(
            AgnosticNumberParserFactory.getParser(AgnosticNumberParserType.Percentage, new ChineseNumberParserConfiguration()),
            new ChinesePercentageExtractor()));

        // French models
        this.registerModel("NumberModel", Culture.French, new NumberModel(
            AgnosticNumberParserFactory.getParser(AgnosticNumberParserType.Number, new FrenchNumberParserConfiguration()),
            new FrenchNumberExtractor(NumberMode.PureNumber)));
        this.registerModel("OrdinalModel", Culture.French, new OrdinalModel(
            AgnosticNumberParserFactory.getParser(AgnosticNumberParserType.Ordinal, new FrenchNumberParserConfiguration()),
            new FrenchOrdinalExtractor()));
        this.registerModel("PercentModel", Culture.French, new PercentModel(
            AgnosticNumberParserFactory.getParser(AgnosticNumberParserType.Percentage, new FrenchNumberParserConfiguration()),
            new FrenchPercentageExtractor()));
    }

    getNumberModel(culture: string): IModel {
        return this.getModel("NumberModel", culture);
    }

    getOrdinalModel(culture: string): IModel {
        return this.getModel("OrdinalModel", culture);
    }

    getPercentageModel(culture: string): IModel {
        return this.getModel("PercentModel", culture);
    }
}
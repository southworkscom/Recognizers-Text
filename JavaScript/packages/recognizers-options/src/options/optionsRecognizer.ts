import { Recognizer, IModel, Culture } from "@microsoft/recognizers-text";
import { BooleanModel } from "./models";
import { BooleanExtractor } from "./extractors";
import { BooleanParser } from "./parsers";
import { EnglishBooleanExtractorConfiguration } from "./english/boolean";

export enum OptionsOptions {
    None = 0,
}

export default class OptionsRecognizer extends Recognizer<OptionsOptions> {
    private constructor() {
        super();

        // English models
        this.registerModel("BooleanModel", Culture.English, new BooleanModel(
            new BooleanParser(),
            new BooleanExtractor(new EnglishBooleanExtractorConfiguration())
        ));
    }

    getBooleanModel(culture: string): IModel {
        return this.getModel("BooleanModel", culture);
    }
}
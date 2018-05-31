package com.microsoft.recognizers.text;

import org.apache.commons.lang3.ObjectUtils;
import org.javatuples.Pair;

import java.util.function.Function;

public abstract class BaseRecognizer<TRecognizerOptions extends Enum<TRecognizerOptions>> {

    public final String targetCulture;
    public final TRecognizerOptions options;

    private final ModelFactory<TRecognizerOptions> factory;

    protected BaseRecognizer(String targetCulture, TRecognizerOptions options, boolean lazyInitialization) {
        if (!this.isValidOptions(options)) {
            throw new IllegalArgumentException(String.format("%s is not a valid options value.", options));
        }

        this.targetCulture = targetCulture;
        this.options = options;

        this.factory = new ModelFactory<>();
        this.initializeConfiguration();

        if (!lazyInitialization) {
            this.initializeModels(targetCulture, options);
        }
    }

    public <T extends IModel> T getModel(Class<T> modelType, String culture, boolean fallbackToDefaultCulture){
        return this.factory.getModel(
                modelType,
                ObjectUtils.firstNonNull(culture, targetCulture),
                fallbackToDefaultCulture,
                options);
    }

    public <T extends IModel> void registerModel(Class<T> modelType, String culture, Function<TRecognizerOptions, IModel> modelCreator) {
        this.factory.put(new Pair<>(culture, modelType), modelCreator);
    }

    private void initializeModels(String targetCulture, TRecognizerOptions options) {
        this.factory.initializeModels(targetCulture, options);
    }

    protected abstract boolean isValidOptions(TRecognizerOptions options);

    protected abstract void initializeConfiguration();
}

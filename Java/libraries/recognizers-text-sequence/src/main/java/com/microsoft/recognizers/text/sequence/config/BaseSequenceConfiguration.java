package com.microsoft.recognizers.text.sequence.config;

import com.microsoft.recognizers.text.sequence.SequenceOptions;

public class BaseSequenceConfiguration implements ISequenceConfiguration {
    private SequenceOptions options;

    public BaseSequenceConfiguration(SequenceOptions options) {
        this.options = options != null ? options : SequenceOptions.None;
    }

    public SequenceOptions getOptions() {
        return this.options;
    }
}
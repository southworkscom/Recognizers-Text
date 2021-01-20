package com.microsoft.recognizers.text.sequence.config;

import com.microsoft.recognizers.text.sequence.SequenceOptions;

import java.util.regex.Pattern;

public class PhoneNumberConfiguration implements ISequenceConfiguration {
    private SequenceOptions options;
    private Pattern FalsePositivePrefixRegex;

    public PhoneNumberConfiguration(SequenceOptions options) {
        this.options = options != null ? options : SequenceOptions.None;
    }

    @Override
    public SequenceOptions getOptions() {
        return this.options;
    }

    public Pattern getFalsePositivePrefixRegex() {
        return FalsePositivePrefixRegex;
    }

    public void setFalsePositivePrefixRegex(Pattern falsePositivePrefixRegex) {
        FalsePositivePrefixRegex = falsePositivePrefixRegex;
    }
}

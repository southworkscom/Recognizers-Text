package com.microsoft.recognizers.text.sequence.spanish;

import com.microsoft.recognizers.text.sequence.SequenceOptions;
import com.microsoft.recognizers.text.sequence.extractors.BasePhoneNumberExtractorConfiguration;

public class SpanishPhoneNumberExtractorConfiguration extends BasePhoneNumberExtractorConfiguration {
    public SpanishPhoneNumberExtractorConfiguration(SequenceOptions options) {
        super(options);

        super.setFalsePositivePrefixRegex(null);
    }
}

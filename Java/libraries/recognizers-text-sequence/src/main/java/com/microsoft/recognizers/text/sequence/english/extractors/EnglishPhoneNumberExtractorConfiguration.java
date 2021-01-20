package com.microsoft.recognizers.text.sequence.english.extractors;

import com.microsoft.recognizers.text.sequence.SequenceOptions;
import com.microsoft.recognizers.text.sequence.extractors.BasePhoneNumberExtractorConfiguration;

public class EnglishPhoneNumberExtractorConfiguration extends BasePhoneNumberExtractorConfiguration {
    public EnglishPhoneNumberExtractorConfiguration(SequenceOptions options) {
        super(options);

        super.setFalsePositivePrefixRegex(Pattern(PhoneNumbersDefinitions.FalsePositivePrefixRegex));
    }
}

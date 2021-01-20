package com.microsoft.recognizers.text.sequence.english.extractors;

import com.microsoft.recognizers.text.sequence.SequenceOptions;

import java.util.regex.Pattern;

public class EnglishPhoneNumberExtractorConfiguration extends BasePhoneNumberExtractorConfiguration {
    public EnglishPhoneNumberExtractorConfiguration(SequenceOptions options) {
        super(options);

        super.setFalsePositivePrefixRegex(Pattern(PhoneNumbersDefinitions.FalsePositivePrefixRegex));
    }
}

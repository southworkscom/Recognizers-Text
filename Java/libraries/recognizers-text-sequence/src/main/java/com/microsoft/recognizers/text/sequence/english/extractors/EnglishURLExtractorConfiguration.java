package com.microsoft.recognizers.text.sequence.english.extractors;

import com.microsoft.recognizers.text.sequence.SequenceOptions;
import com.microsoft.recognizers.text.sequence.config.URLConfiguration;

import java.util.regex.Pattern;

public class EnglishURLExtractorConfiguration extends URLConfiguration {
    public EnglishURLExtractorConfiguration(SequenceOptions options) {
        super(options);

        super.setIpUrlRegex(Pattern.compile(BaseURL.IpUrlRegex));
        super.setUrlRegex(Pattern.compile(BaseURL.UrlRegex));
    }
}

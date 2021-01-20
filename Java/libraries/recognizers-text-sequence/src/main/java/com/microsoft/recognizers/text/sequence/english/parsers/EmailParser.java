package com.microsoft.recognizers.text.sequence.english.parsers;

import com.microsoft.recognizers.text.sequence.config.BaseSequenceConfiguration;
import com.microsoft.recognizers.text.sequence.parsers.BaseSequenceParser;

public class EmailParser extends BaseSequenceParser {
    private BaseSequenceConfiguration config;

    public EmailParser(BaseSequenceConfiguration config) {
        this.config = config;
    }
}

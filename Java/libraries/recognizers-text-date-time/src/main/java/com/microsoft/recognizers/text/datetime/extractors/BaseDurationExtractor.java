package com.microsoft.recognizers.text.datetime.extractors;

import com.microsoft.recognizers.text.ExtractResult;
import com.microsoft.recognizers.text.datetime.Constants;
import com.microsoft.recognizers.text.datetime.extractors.config.IDurationExtractorConfiguration;

import java.time.LocalDateTime;
import java.util.List;

public class BaseDurationExtractor implements IDateTimeExtractor {

    private final IDurationExtractorConfiguration config;

    @Override
    public String getExtractorName() {
        return Constants.SYS_DATETIME_DURATION;
    }

    public BaseDurationExtractor(IDurationExtractorConfiguration config) {
        this.config = config;
    }

    @Override
    public List<ExtractResult> extract(String input, LocalDateTime reference) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<ExtractResult> extract(String input) {
        return this.extract(input, LocalDateTime.now());
    }
}

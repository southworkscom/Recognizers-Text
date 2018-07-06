package com.microsoft.recognizers.text.datetime.extractors;

import com.microsoft.recognizers.text.ExtractResult;
import com.microsoft.recognizers.text.datetime.Constants;
import com.microsoft.recognizers.text.datetime.extractors.config.IDateTimeAltExtractorConfiguration;

import java.time.LocalDateTime;
import java.util.List;

public class BaseDateTimeAltExtractor implements IDateTimeListExtractor {

    private final IDateTimeAltExtractorConfiguration config;

    @Override
    public String getExtractorName() {
        return Constants.SYS_DATETIME_DATETIMEALT;
    }

    public BaseDateTimeAltExtractor(IDateTimeAltExtractorConfiguration config) {
        this.config = config;
    }

    public List<ExtractResult> extract(List<ExtractResult> extractResults, String text) {
        return this.extract(extractResults, text, LocalDateTime.now());
    }

    @Override
    public List<ExtractResult> extract(List<ExtractResult> extractResults, String text, LocalDateTime reference) {
        throw new UnsupportedOperationException();
    }
}

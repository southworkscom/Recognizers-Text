package com.microsoft.recognizers.text.datetime.extractors;

import com.microsoft.recognizers.text.ExtractResult;
import com.microsoft.recognizers.text.datetime.Constants;
import com.microsoft.recognizers.text.datetime.extractors.config.IDatePeriodExtractorConfiguration;

import java.time.LocalDateTime;
import java.util.List;

public class BaseDatePeriodExtractor implements IDateTimeExtractor {

    private final IDatePeriodExtractorConfiguration config;

    @Override
    public String getExtractorName() {
        return Constants.SYS_DATETIME_DATEPERIOD;
    }

    public BaseDatePeriodExtractor(IDatePeriodExtractorConfiguration config) {
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

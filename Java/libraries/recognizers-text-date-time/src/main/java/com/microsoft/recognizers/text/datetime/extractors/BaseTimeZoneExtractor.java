package com.microsoft.recognizers.text.datetime.extractors;

import com.microsoft.recognizers.text.ExtractResult;
import com.microsoft.recognizers.text.datetime.Constants;
import com.microsoft.recognizers.text.datetime.extractors.config.ITimeZoneExtractorConfiguration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class BaseTimeZoneExtractor implements IDateTimeZoneExtractor {

    private final ITimeZoneExtractorConfiguration config;

    @Override
    public String getExtractorName() {
        return Constants.SYS_DATETIME_TIMEZONE;
    }

    public BaseTimeZoneExtractor(ITimeZoneExtractorConfiguration config) {
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

    @Override
    public List<ExtractResult> removeAmbiguousTimezone(List<ExtractResult> extractResults) {
        return extractResults.stream().filter(o -> !config.getAmbiguousTimezoneList().contains(o.text.toLowerCase())).collect(Collectors.toList());
    }
}

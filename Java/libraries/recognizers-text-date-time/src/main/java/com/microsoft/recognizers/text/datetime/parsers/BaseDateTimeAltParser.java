package com.microsoft.recognizers.text.datetime.parsers;

import com.microsoft.recognizers.text.ExtractResult;
import com.microsoft.recognizers.text.ParseResult;
import com.microsoft.recognizers.text.datetime.Constants;
import com.microsoft.recognizers.text.datetime.parsers.config.IDateTimeAltParserConfiguration;

import java.time.LocalDateTime;
import java.util.List;

public class BaseDateTimeAltParser implements IDateTimeParser {

    private final IDateTimeAltParserConfiguration config;

    @Override
    public String getParserName() {
        return Constants.SYS_DATETIME_DATETIMEALT;
    }

    public BaseDateTimeAltParser(IDateTimeAltParserConfiguration config) {
        this.config = config;
    }

    @Override
    public DateTimeParseResult parse(ExtractResult er, LocalDateTime reference) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<DateTimeParseResult> filterResults(String query, List<DateTimeParseResult> candidateResults) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ParseResult parse(ExtractResult extractResult) {
        return this.parse(extractResult, LocalDateTime.now());
    }
}

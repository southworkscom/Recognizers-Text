package com.microsoft.recognizers.text.datetime.parsers;

import com.microsoft.recognizers.text.ExtractResult;
import com.microsoft.recognizers.text.ParseResult;
import com.microsoft.recognizers.text.datetime.Constants;
import com.microsoft.recognizers.text.datetime.parsers.config.IMergedParserConfiguration;

import java.time.LocalDateTime;
import java.util.List;

public class BaseMergedDateTimeParser implements IDateTimeParser {

    private final IMergedParserConfiguration config;

    public BaseMergedDateTimeParser(IMergedParserConfiguration config) {
        this.config = config;
    }

    @Override
    public String getParserName() {
        return "datetimeV2";
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

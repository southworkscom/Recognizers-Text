package com.microsoft.recognizers.text.datetime.parsers;

import com.microsoft.recognizers.text.ExtractResult;
import com.microsoft.recognizers.text.ParseResult;
import com.microsoft.recognizers.text.datetime.Constants;
import com.microsoft.recognizers.text.datetime.parsers.config.IHolidayParserConfiguration;

import java.time.LocalDateTime;
import java.util.List;

public class BaseHolidayParser implements IDateTimeParser {

    private final IHolidayParserConfiguration config;

    public BaseHolidayParser(IHolidayParserConfiguration config) {
        this.config = config;
    }

    @Override
    public String getParserName() {
        return Constants.SYS_DATETIME_DATE;
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

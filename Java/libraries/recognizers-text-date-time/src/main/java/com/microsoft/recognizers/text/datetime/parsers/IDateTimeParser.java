package com.microsoft.recognizers.text.datetime.parsers;

import com.microsoft.recognizers.text.ExtractResult;
import com.microsoft.recognizers.text.IParser;

import java.util.Date;
import java.util.List;

public interface IDateTimeParser extends IParser {
    String getParserName();
    DateTimeParseResult parse(ExtractResult er, Date reference);
    List<DateTimeParseResult> filterResults(String query, List<DateTimeParseResult> candidateResults);
}

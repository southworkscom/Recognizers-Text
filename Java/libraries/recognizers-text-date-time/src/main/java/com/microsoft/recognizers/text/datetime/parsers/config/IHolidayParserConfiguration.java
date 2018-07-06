package com.microsoft.recognizers.text.datetime.parsers.config;

import com.google.common.collect.ImmutableMap;
import com.microsoft.recognizers.text.datetime.config.IOptionsConfiguration;

import java.time.LocalDate;
import java.util.function.Function;
import java.util.regex.Pattern;

public interface IHolidayParserConfiguration extends IOptionsConfiguration {
    ImmutableMap<String, String> getVariableHolidaysTimexDictionary();
    ImmutableMap<String, Function<Integer, LocalDate>> getHolidayFuncDictionary();
    ImmutableMap<String, Iterable<String>> getHolidayNames();
    Iterable<Pattern> getHolidayRegexList();
    int getSwiftYear(String text);
    String sanitizeHolidayToken(String holiday);
}

package com.microsoft.recognizers.text.datetime.extractors;

import com.microsoft.recognizers.text.datetime.config.IOptionsConfiguration;
import java.util.regex.Pattern;

public interface IHolidayExtractorConfiguration extends IOptionsConfiguration {
    Pattern[] getHolidayRegexes();
}
package com.microsoft.recognizers.text.datetime.utilities;

import java.util.regex.Pattern;

public interface IDateTimeUtilityConfiguration {
    Pattern AgoRegex();
    Pattern LaterRegex();
    Pattern InConnectorRegex();
    Pattern WithinNextPrefixRegex();
    Pattern RangeUnitRegex();
    Pattern TimeUnitRegex();
    Pattern DateUnitRegex();
    Pattern AmDescRegex();
    Pattern PmDescRegex();
    Pattern AmPmDescRegex();
    Pattern CommonDatePrefixRegex();
}

// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.recognizers.datatypes.timex.expression;

import java.time.LocalDateTime;

public class TimexValue {
    public static String dateValue(TimexProperty timexProperty) {
        if (timexProperty.getYear() != null && timexProperty.getMonth() != null && timexProperty.getDayOfMonth() != null) {
            return String.format("%1$s-%2$s-%3$s", TimexDateHelpers.fixedFormatNumber(timexProperty.getYear(), 4),
                    TimexDateHelpers.fixedFormatNumber(timexProperty.getMonth(), 2),
                    TimexDateHelpers.fixedFormatNumber(timexProperty.getDayOfMonth(), 2));
        }

        return new String();
    }

    public static String timeValue(TimexProperty timexProperty, LocalDateTime date) {
        if (timexProperty.getHour() != null && timexProperty.getMinute() != null && timexProperty.getSecond() != null) {
            return String.format("%1$s:%2$s:%3$s", TimexDateHelpers.fixedFormatNumber(timexProperty.getHour(), 2),
                    TimexDateHelpers.fixedFormatNumber(timexProperty.getMinute(), 2),
                    TimexDateHelpers.fixedFormatNumber(timexProperty.getSecond(), 2));
        }

        return new String();
    }

    public static String datetimeValue(TimexProperty timexProperty, LocalDateTime date) {
        return String.format("%1$s %2$s", TimexValue.dateValue(timexProperty),
                TimexValue.timeValue(timexProperty, date));
    }

    public static String durationValue(TimexProperty timexProperty) {
        if (timexProperty.getYears() != null) {
            return String.valueOf(31536000 * timexProperty.getYears().doubleValue());
        }

        if (timexProperty.getMonths() != null) {
            return String.valueOf(2592000 * timexProperty.getYears().doubleValue());
        }

        if (timexProperty.getWeeks() != null) {
            return String.valueOf(604800 * timexProperty.getWeeks().doubleValue());
        }

        if (timexProperty.getDays() != null) {
            return String.valueOf(86400 * timexProperty.getDays().doubleValue());
        }

        if (timexProperty.getHours() != null) {
            return String.valueOf(3600 * timexProperty.getHours().doubleValue());
        }

        if (timexProperty.getMinutes() != null) {
            return String.valueOf(60 * timexProperty.getMinutes().doubleValue());
        }

        if (timexProperty.getSeconds() != null) {
            return String.valueOf(timexProperty.getSeconds());
        }

        return new String();
    }
}

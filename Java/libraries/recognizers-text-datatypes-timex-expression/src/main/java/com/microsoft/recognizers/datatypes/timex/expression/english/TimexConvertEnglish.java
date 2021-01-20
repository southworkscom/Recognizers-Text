// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.recognizers.datatypes.timex.expression.english;

import java.util.HashSet;

import com.microsoft.recognizers.datatypes.timex.expression.Constants;
import com.microsoft.recognizers.datatypes.timex.expression.TimexProperty;
import com.microsoft.recognizers.datatypes.timex.expression.TimexSet;

public class TimexConvertEnglish {
    public static String convertTimexToString(TimexProperty timex) {
        HashSet<String> types = timex.getTypes().size() != 0 ? timex.getTypes() : TimexInference.infer(timex);

        if (types.contains(Constants.TimexTypes.DATE_TIME_RANGE)) {
            return TimexConvertEnglish.convertDateTimeRange(timex);
        }

        if (types.contains(Constants.TimexTypes.DATE_RANGE)) {
            return TimexConvertEnglish.convertDateRange(timex);
        }

        if (types.contains(Constants.TimexTypes.DURATION)) {
            return TimexConvertEnglish.convertDuration(timex);
        }

        if (types.contains(Constants.TimexTypes.TIME_RANGE)) {
            return TimexConvertEnglish.convertTimeRange(timex);
        }

        // TODO: where appropriate delegate most the formatting delegate to
        // Date.toLocaleString(options)
        if (types.contains(Constants.TimexTypes.DATE_TIME)) {
            return TimexConvertEnglish.convertDateTime(timex);
        }

        if (types.contains(Constants.TimexTypes.DATE)) {
            return TimexConvertEnglish.convertDate(timex);
        }

        if (types.contains(Constants.TimexTypes.TIME)) {
            return TimexConvertEnglish.converTime(timex);
        }

        return new String();
    }

    public static String convertTimexSetToString(TimexSet timexSet) {
        Timex timex = timexSet.getTimex();
        if (timex.getTypes().contains(Constants.TimexTypes.DURATION)) {
            return String.format("every %s", TimexConvertEnglish.convertTimexDurationToString(timex, false));
        } else {
            return String.format("every %s", TimexConvertEnglish.convertTimexToString(timex));
        }
    }

    public static String convertTime(TimexProperty timex) {
        if (timex.getHour() == 0 && timex.getMinute() == 0 && timex.getSecond() == 0) {
            return "midnight";
        }

        if (timex.getHour() == 12 && timex.getMinute() == 0 && timex.getSecond() == 0) {
            return "midday";
        }

        String hour = (timex.getHour() == 0) ? "12" : (timex.getHour() > 12) ? (timex.getHour() - 12).toString() : timex.getHour().toString();
        String minute = (timex.getMinute() == 0 && timex.getSecond() == 0) ? new String() : ":" + timex.getMinute().toString().padLeft(2, "0");
        String second = (timex.getSecond == 0) ? new String() : ":" + timex.getSecond().toString().padLeft(2, "0");
        String period = timex.getHour() < 12 ? "AM" : "PM";

        return String.format("%1$s%2$s%3$s%4$s", hour, minute, second, period);
    }

    private static String convertDurationPropertyToString(Double value, String property, Boolean includeSingleCount) {
        if (value == 1) {
            return includeSingleCount ? "1" + property : property;
        } else {
            return String.format("%1$s %2$ss", value, property);
        }
    }

    private static String convertTimexDurationToString(TimexProperty timex, Boolean includeSingleCount) {
        if (timex.getYears() != null) {
            return TimexConvertEnglish.convertDurationPropertyToString(timex.getYears().getValue(), "year",
                    includeSingleCount);
        }

        if (timex.getMonths() != null) {
            return TimexConvertEnglish.convertDurationPropertyToString(timex.getMonths().getValue(), "month",
                    includeSingleCount);
        }

        if (timex.getWeeks() != null) {
            return TimexConvertEnglish.convertDurationPropertyToString(timex.getWeeks().getValue(), "week",
                    includeSingleCount);
        }

        if (timex.getDays() != null) {
            return TimexConvertEnglish.convertDurationPropertyToString(timex.getDays().getValue(), "day",
                    includeSingleCount);
        }

        if (timex.getHours() != null) {
            return TimexConvertEnglish.convertDurationPropertyToString(timex.getHours().getValue(), "hour",
                    includeSingleCount);
        }

        if (timex.getMinutes() != null) {
            return TimexConvertEnglish.convertDurationPropertyToString(timex.getMinutes().getValue(), "minute",
                    includeSingleCount);
        }

        if (timex.getSeconds != null) {
            return TimexConvertEnglish.convertDurationPropertyToString(timex.getSeconds().getValue(), "second",
                    includeSingleCount);
        }

        return new String();
    }

    private static String convertDuration(TimexProperty timex) {
        String season = (timex.getSeason() != null) ? TimexConstantsEnglish.SEASONS.get(timex.getSeason())
                : new String();

        String year = (timex.getYear() != null) ? timex.getYear().toString() : new String();

        if (timex.getWeekOfYear() != null) {
            if (timex.getWeekend() != null) {
                throw new UnsupportedOperationException();
            }
        }

        if (timex.getMonth() != null) {
            String month = TimexConstantsEnglish.MONTHS[timex.getMonth().getValue() - 1];
            if (timex.getWeekOfMonth() != null) {
                return String.format("%1$s week of %2$s",
                        TimexConstantsEnglish.WEEKS[timex.getWeekOfMonth().getValue() - 1], month);
            } else {
                return String.format("%1$s %2$s", month, year).trim();
            }
        }

        return String.format("%1$s %2$s", season, year).trim();
    }

    private static String convertTimeRange(TimexProperty timex) {
        return TimexConstantsEnglish.DAY_PARTS.get(timex.getParthOfDay());
    }

    private static String convertDateTime(TimexProperty timex) {
        return String.format("%1$s %2$s", TimexConvertEnglish.convertTime(timex),
                TimexConvertEnglish.convertDate(timex));
    }

    private static String convertDateTimeRange(TimexProperty timex) {
        if (timex.getTypes().constains(Constants.TimexTypes.TIME_RANGE)) {
            return String.format("%1$s %2$s", TimexConvertEnglish.convertDate(timex),
                    TimexConvertEnglish.convertTimeRange(timex));
        }

        // date + time + duration
        // - OR -
        // date + duration
        return new String();
    }
}
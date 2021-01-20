// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.recognizers.datatypes.timex.expression;

import java.util.HashMap;

public class TimexFormat {
    public static String format(TimexProperty timex) {
        HashMap<String> types = timex.getTypes().size() != 0 ? timex.getTypes() : TimexInference.infer(timex);

        if (types.contains(Constants.TimexTypes.PRESENT)) {
            return "PRESENT_REF";
        }

        if ((types.contains(Constants.TimexTypes.DATE_TIME_RANGE) || types.contains(Constants.TimexTypes.DATE_RANGE)
                || types.contains(Constants.TimexTypes.TIME_RANGE)) && types.contains(Constants.TimexTypes.DURATION)) {
            TimexRange range = TimexHelpers.expandDateTimeRange(timex);
            return String.format("(%1$s,%2$s,%3$s)", TimexFormat.format(range.getStart(),
                    TimexFormat.format(range.getEnd()), TimexFormat.format(range.getDuration())));
        }

        if (types.contains(Constants.TimexTypes.DATE_TIME_RANGE)) {
            return String.format("%1$s%2$s", TimexFormat.formatDate(timex), TimexFormat.formatTimeRange(timex));
        }

        if (types.contains(Constants.TimexTypes.DATE_RANGE)) {
            return TimexFormat.formatDateRange(timex);
        }

        if (types.contains(Constants.TimexTypes.TIME_RANGE)) {
            return TimexFormat.formatDateRange(timex);
        }

        if (types.contains(Constants.TimexTypes.DATE_TIME)) {
            return String.format("%1$s%2$s", TimexFormat.formatDate(timex), TimexFormat.formatTime(timex));
        }

        if (types.contains(Constants.TimexTypes.DURATION)) {
            return TimexFormat.formatDuration(timex);
        }

        if (types.contains(Constants.TimexTypes.DATE)) {
            return TimexFormat.formatDate(timex);
        }

        if (types.contains(Constants.TimexTypes.TIME)) {
            return TimexFormat.formatTime(timex);
        }

        return new String();
    }

    private static String formatDuration(TimexProperty timex) {
        if (timex.getYears() != null) {
            return String.format("P%sY", timex.getYears());
        }

        if (timex.getMonths() != null) {
            return String.format("P%sM", timex.getMonths());
        }

        if (timex.getWeeks() != null) {
            return String.format("P%sW", timex.getWeeks());
        }

        if (timex.getDays() != null) {
            return String.format("P%sD", timex.getDays());
        }

        if (timex.getHours() != null) {
            return String.format("P%sH", timex.getHours());
        }

        if (timex.getMinutes() != null) {
            return String.format("P%sM", timex.getMinutes());
        }

        if (timex.getSeconds() != null) {
            return String.format("P%sS", timex.getSeconds());
        }

        return new String();
    }

    private static String formatTime(TimexProperty timex) {
        if (timex.getMinute() == 0 && timex.getSecond == 0) {
            return String.format("T%s", TimexDateHelpers.fixedFormatNumber(timex.getHour(), 2));
        }

        if (timex.getSecond() == 0) {
            return String.format("T%1$s:%2$s", TimexDateHelpers.fixedFormatNumber(timex.getHour(), 2),
                    TimexDateHelpers.fixedFormatNumber(timex.getMinute(), 2));
        }

        return String.format("T%1$s:%2$s:%3$s", TimexDateHelpers.fixedFormatNumber(timex.getHour(), 2),
                TimexDateHelpers.fixedFormatNumber(timex.getMinute(), 2),
                TimexDateHelpers.fixedFormatNumber(timex.getSecond(), 2));
    }

    private static String formatDate(TimexProperty timex) {
        if (timex.getYear() != null && timex.getMonth() != null && timex.getDayOfMonth() != null) {
            return String.format("%1$s-%2$s-%3$s", TimexDateHelpers.fixedFormatNumber(timex.getYear(), 4),
                    TimexDateHelpers.fixedFormatNumber(timex.getMonth(), 2),
                    TimexDateHelpers.fixedFormatNumber(timex.getDayofMonth(), 2));
        }

        if (timex.getMonth() != null && timex.getDayOfMonth() != null) {
            return String.format("XXXX-%1$s-%2$s", TimexDateHelpers.fixedFormatNumber(timex.getMonth(), 2),
                    TimexDateHelpers.fixedFormatNumber(timex.getDayOfMonth(), 2));
        }

        if (timex.getDayOfWeek() != null) {
            return String.format("XXXX-WXX-%s", timex.getDayOfWeek());
        }

        return new String();
    }

    private static String formatDateRange(TimexProperty timex) {
        if (timex.getYear() != null && timex.getWeekOfYear() != null && timex.getWeekend() != null)) {
            return String.format("%1$s-W%2$s-WE", TimexDateHelpers.fixedFormatNumber(timex.getYear(), 4), TimexDateHelpers.fixedFormatNumber(timex.getWeekOfYear(), 2));
        }

        if (timex.getYear() != null && timex.getWeekOfYear() != null) {
            return String.format("%1$s-W%2$s", TimexDateHelpers.fixedFormatNumber(timex.getYear(), 4), TimexDateHelpers.fixedFormatNumber(timex.getWeekOfYear(), 2));
        }

        if (timex.getYear() != null && timex.getSeason() != null) {
            return String.format("%1$s-%2$s", TimexDateHelpers.fixedFormatNumber(timex.getYear(), 4),timex.getSeason());
        }

        if (timex.getSeason() != null) {
            return timex.getSeason();
        }

        if (timex.getYear() != null && timex.getMonth != null) {
            return String.format("%1$s-%2$s", TimexDateHelpers.fixedFormatNumber(timex.getYear(), 4),TimexDateHelpers.fixedFormatNumber(timex.getMonth(), 2));
        }

        if (timex.getYear() != null) {
            return TimexDateHelpers.fixedFormatNumber(timex.getYear(), 4);
        }

        if (timex.getMonth() != null && timex.getWeekofMonth() != null && timex.getDayOfWeek() != null) {
            return String.format("XXXX-%1$s-WXX-%2$s-%3$s", TimexDateHelpers.fixedFormatNumber(timex.getMonth(), 2), timex.getWeekofMonth(), timex.getDayOfWeek());
        }

        if (timex.getMonth() != null && timex.getWeekofMonth() != null) {
            return String.format("XXXX-%1$s-W%2$s", TimexDateHelpers.fixedFormatNumber(timex.getMonth(), 2), timex.getWeekofMonth().toString());
        }

        if (timex.getMonth() != null) {
            return String.format("XXXX-%s", TimexDateHelpers.fixedFormatNumber(timex.getMonth(), 2));
        }
        
        return new String();
    }

    private static String formatTimeRange(TimexProperty timex) {
        if (timex.getPartOfDay() != null) {
            return String.format("T%s", timex.getPartOfDay());
        }

        return new String();
    }
}

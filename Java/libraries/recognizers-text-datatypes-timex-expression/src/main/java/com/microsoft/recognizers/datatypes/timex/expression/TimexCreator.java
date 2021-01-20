// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.recognizers.datatypes.timex.expression;

import java.time.DayOfWeek;

public class TimexCreator {
    // The following constants are consistent with the Recognizer results
    public static final String MONDAY = "XXXX-WXX-1";
    public static final String TUESDAY = "XXXX-WXX-2";
    public static final String WEDNESDAY = "XXXX-WXX-3";
    public static final String THURSDAY = "XXXX-WXX-4";
    public static final String FRIDAY = "XXXX-WXX-5";
    public static final String SATURDAY = "XXXX-WXX-6";
    public static final String SUNDAY = "XXXX-WXX-7";
    public static final String MORNING = "(T08,T12,PT4H)";
    public static final String AFTERNOON = "(T12,T16,PT4H)";
    public static final String EVENING = "(T16,T20,PT4H)";
    public static final String DAYTIME = "(T08,T18,PT10H)";
    public static final String NIGHT = "(T20,T24,PT10H)";

    public static String today(DateObject date) {
        return TimexProperty.fromDate(date == null ? DateObject.now() : date).getTimexValue();
    }

    public static String tomorrow(DateObject date) {
        DateObject d = (date == null) ? DateObject.now : date;
        d = d.addDays(1);
        return TimexProperty.fromDate(d).getTimexValue();
    }

    public static String weekFromToday(DateObject date) {
        DateObject d = (date == null) ? DateObject.now : date;
        DateObject t = TimexProperty.fromDate(d);
        t.setDays(7);
        return t.getTimexValue();
    }

    public static String weekBackFromToday(DateObject date) {
        DateObject d = (date == null) ? DateObject.now : date;
        d = d.addDays(-7);
        DateObject t = TimexProperty.fromDate(d);
        t.setDays(7);
        return t.getTimexValue();
    }

    public static String thisWeek(DateObject date) {
        DateObject d = (date == null) ? DateObject.now : date;
        d = d.addDays(-7);
        DateObject start = TimexDateHelpers.dateOfNextDay(DayOfWeek.MONDAY, d);
        DateObject t = TimexProperty.fromDate(start);
        t.setDays(7);
        return t.getTimexValue();
    }

    public static String nextWeek(DateObject date) {
        DateObject d = (date == null) ? DateObject.now : date;
        DateObject start = TimexDateHelpers.dateOfNextDay(DayOfWeek.MONDAY, d);
        DateObject t = TimexProperty.fromDate(start);
        t.setDays(7);
        return t.getTimexValue();
    }

    public static String lastWeek(DateObject date) {
        DateObject d = (date == null) ? DateObject.now : date;
        DateObject start = TimexDateHelpers.dateOfNextDay(DayOfWeek.MONDAY, d);
        start = start.addDays(-7);
        DateObject t = TimexProperty.fromDate(start);
        t.setDays(7);
        return t.getTimexValue();
    }

    public static String nextWeeksFromToday(Integer n, DateObject date) {
        DateObject d = (date == null) ? DateObject.now : date;
        DateObject t = TimexProperty.fromDate(d);
        t.setDays(n * 7);
        return t.getTimexValue();
    }
}

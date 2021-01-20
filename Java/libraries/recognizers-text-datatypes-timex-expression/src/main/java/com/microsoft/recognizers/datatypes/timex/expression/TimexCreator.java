// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.recognizers.datatypes.timex.expression;

import java.time.DayOfWeek;
import java.util.Calendar;

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

    public static String today(Calendar date) {
        return TimexProperty.fromDate(date == null ? Calendar.getInstance() : date).getTimexValue();
    }

    public static String tomorrow(Calendar date) {
        Calendar d = (date == null) ? Calendar.getInstance() : date;
        d.add(Calendar.DATE, 1);
        return TimexProperty.fromDate(d).getTimexValue();
    }

    public static String yesterday(Calendar date) {
        Calendar d = (date == null) ? Calendar.getInstance() : date;
        d.add(Calendar.DATE, -1);
        return TimexProperty.fromDate(d).getTimexValue();
    }

    public static String weekFromToday(Calendar date) {
        Calendar d = (date == null) ? Calendar.getInstance() : date;
        TimexProperty t = TimexProperty.fromDate(d);
        t.setDays(7d);
        return t.getTimexValue();
    }

    public static String weekBackFromToday(Calendar date) {
        Calendar d = (date == null) ? Calendar.getInstance() : date;
        d.add(Calendar.DATE, -7);
        TimexProperty t = TimexProperty.fromDate(d);
        t.setDays(7d);
        return t.getTimexValue();
    }

    public static String thisWeek(Calendar date) {
        Calendar d = (date == null) ? Calendar.getInstance() : date;
        d.add(Calendar.DATE, -7);
        Calendar start = TimexDateHelpers.dateOfNextDay(DayOfWeek.MONDAY, d);
        TimexProperty t = TimexProperty.fromDate(start);
        t.setDays(7d);
        return t.getTimexValue();
    }

    public static String nextWeek(Calendar date) {
        Calendar d = (date == null) ? Calendar.getInstance() : date;
        Calendar start = TimexDateHelpers.dateOfNextDay(DayOfWeek.MONDAY, d);
        TimexProperty t = TimexProperty.fromDate(start);
        t.setDays(7d);
        return t.getTimexValue();
    }

    public static String lastWeek(Calendar date) {
        Calendar d = (date == null) ? Calendar.getInstance() : date;
        Calendar start = TimexDateHelpers.dateOfNextDay(DayOfWeek.MONDAY, d);
        start.add(Calendar.DATE, -7);
        TimexProperty t = TimexProperty.fromDate(start);
        t.setDays(7d);
        return t.getTimexValue();
    }

    public static String nextWeeksFromToday(Integer n, Calendar date) {
        Calendar d = (date == null) ? Calendar.getInstance() : date;
        TimexProperty t = TimexProperty.fromDate(d);
        t.setDays(n * 7d);
        return t.getTimexValue();
    }
}

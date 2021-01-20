// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.recognizers.datatypes.timex.expression;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TimexDateHelpers {
    public static Calendar tomorrow(Calendar date) {
        date.add(Calendar.DATE, 1);
        return date;
    }

    public static Calendar yesterday(Calendar date) {
        date.add(Calendar.DATE, -1);
        return date;
    }

    public static Boolean datePartEquals(Calendar dateX, Calendar dateY) {
        return (dateX.get(Calendar.YEAR) == dateY.get(Calendar.YEAR)) &&
            (dateX.get(Calendar.MONTH) == dateY.get(Calendar.MONTH)) &&
            (dateX.get(Calendar.DATE) == dateY.get(Calendar.DATE));
    }

    public static boolean isDateInWeek(Calendar date, Calendar startOfWeek) {
        Calendar d = startOfWeek;
        for (int i = 0; i < 7; i++) {
            if (TimexDateHelpers.datePartEquals(date, d)) {
                return true;
            }

            d.add(Calendar.DATE, 1);
        }

        return false;
    }

    public static Boolean isThisWeek(Calendar date, Calendar referenceDate) {
        // Note ISO 8601 week starts on a Monday
        Calendar startOfWeek = referenceDate;
        while (startOfWeek.get(Calendar.DAY_OF_WEEK) > DayOfWeek.MONDAY.getValue()) {
            startOfWeek.add(Calendar.DATE, -1);
        }

        return TimexDateHelpers.isDateInWeek(date, startOfWeek);
    }

    public static Boolean isNextWeek(Calendar date, Calendar referenceDate) {
        Calendar nextWeekDate = referenceDate;
        nextWeekDate.add(Calendar.DATE, 7);
        return TimexDateHelpers.isThisWeek(date, nextWeekDate);
    }

    public static Boolean isLastWeek(Calendar date, Calendar referenceDate) {
        Calendar nextWeekDate = referenceDate;
        nextWeekDate.add(Calendar.DATE, -7);
        return TimexDateHelpers.isThisWeek(date, nextWeekDate);
    }

    public static Integer weekOfYear(Calendar date) {
        Calendar ds = Calendar.getInstance();
        ds.set(date.get(Calendar.YEAR), 1, 1);
        Calendar de = Calendar.getInstance();
        ds.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE));
        Integer weeks = 1;

        while (ds.compareTo(de) < 0) {
            Integer dayOfWeek = ds.get(Calendar.DAY_OF_WEEK);

            Integer isoDayOfWeek = (dayOfWeek == 0) ? 7 : dayOfWeek;
            if (isoDayOfWeek == 7) {
                weeks++;
            }

            ds.add(Calendar.DATE, 1);
        }

        return weeks;
    }

    public static String fixedFormatNumber(Integer n, Integer size) {
        return String.format("%1$" + size + "s", n.toString()).replace(' ', '0');
    }

    public static Calendar dateOfLastDay(DayOfWeek day, Calendar referenceDate) {
        Calendar result = referenceDate;
        result.add(Calendar.DATE, -1);

        while (DayOfWeek.of(result.get(Calendar.DAY_OF_WEEK)) != day) {
            result.add(Calendar.DATE, -1);
        }

        return result;
    }

    public static Calendar dateOfNextDay(DayOfWeek day, Calendar referenceDate) {
        Calendar result = referenceDate;
        result.add(Calendar.DATE, 1);

        while (DayOfWeek.of(result.get(Calendar.DAY_OF_WEEK)) != day) {
            result.add(Calendar.DATE, 1);
        }

        return result;
    }

    public static List<Calendar> datesMatchingDay(DayOfWeek day, Calendar start, Calendar end) {
        List<Calendar> result = new ArrayList<Calendar>();
        Calendar d = start;

        while (!TimexDateHelpers.datePartEquals(d, end)) {
            if (DayOfWeek.of(d.get(Calendar.DAY_OF_WEEK)) == day) {
                result.add(d);
            }

            d.add(Calendar.DATE, 1);
        }

        return result;
    }
}

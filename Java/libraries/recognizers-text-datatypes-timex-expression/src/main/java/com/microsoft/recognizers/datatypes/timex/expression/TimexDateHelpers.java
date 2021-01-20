// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.recognizers.datatypes.timex.expression;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class TimexDateHelpers {
    public static DateObject tomorrow(DateObject date) {
        return date.addDays(1);
    }

    public static DateObject yesterday(DateObject date) {
        return date.addDays(-1);
    }

    public static Boolean datePartEquals(DateObject dateX, DateObject dateY) {
        return (dateX.getYear() == dateY.getYear()) && (dateX.getMonth() == dateY.getMonth())
                && (dateX.getDay() == dateY.getDay());
    }

    public static boolean isDateInWeek(DateObject date, DateObject startOfWeek) {
        DateObject d = startOfWeek;
        for (int i = 0; i < 7; i++) {
            if (TimexDateHelpers.datePartEquals(date, d)) {
                return true;
            }

            d = d.addDays(1);
        }

        return false;
    }

    public static Boolean isThisWeek(DateObject date, DateObject referenceDate) {
        // Note ISO 8601 week starts on a Monday
        DateObject startOfWeek = referenceDate;
        while (startOfWeek.dayOfWeek > DayOfWeek.MONDAY) {
            startOfWeek = startOfWeek.addDays(-1);
        }

        return TimexDateHelpers.isDateInWeek(date, startOfWeek);
    }

    public static Boolean isNextWeek(DateObject date, DateObject referenceDate) {
        DateObject nextWeekDate = referenceDate.addDays(7);
        return TimexDateHelpers.isThisWeek(date, referenceDate);
    }

    public static Boolean isLastWeek(DateObject date, DateObject referenceDate) {
        DateObject nextWeekDate = referenceDate.addDays(-7);
        return TimexDateHelpers.isThisWeek(date, referenceDate);
    }

    public static Integer weekOfYear(DateObject date) {
        DateObject ds = new DateObject(date.getYear(), 1, 1);
        DateObject de = new DateObject(date.getYear(), date.getMonth(), date.getDay());
        Integer weeks = 1;

        while (ds < de) {
            DateObject dayOfWeek = ds.getDayOfWeek();

            Integer isoDayOfWeek = (dayOfWeek == 0) ? 7 : (Integer) dayOfWeek;
            if (isoDayOfWeek == 7) {
                weeks++;
            }

            ds = ds.addDays(1);
        }

        return weeks;
    }

    public static String fixedFormatNumber(Integer n, Integer size) {
        return n.toString().padLeft(size, "0");
    }

    public static DateObject dateOfLastDay(DayOfWeek day, DateObject referenceDate) {
        DateObject result = referenceDate;
        result = result.addDays(-1);

        while (result.getDayOfWeek() != day) {
            result = result.addDays(-1);
        }

        return result;
    }

    public static DateObject dateOfNextDay(DayOfWeek day, DateObject referenceDate) {
        DateObject result = referenceDate;
        result = result.addDays(1);

        while (result.getDayOfWeek() != day) {
            result = result.addDays(1);
        }

        return result;
    }

    public static List<DateObject> datesMatchingDay(DayOfWeek day, DateObject start, DateObject end) {
        List<DateObject> result = new ArrayList<DateObject>();
        DateObject d = start;

        while (!TimexDateHelpers.datePartEquals(d, end)) {
            if (d.getDayOfWeek == day) {
                result.add(d);
            }

            d = d.addDays(1);
        }

        return result;
    }
}

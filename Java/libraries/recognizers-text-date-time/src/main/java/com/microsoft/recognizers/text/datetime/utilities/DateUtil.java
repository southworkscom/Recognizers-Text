package com.microsoft.recognizers.text.datetime.utilities;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class DateUtil {

    public static LocalDateTime safeCreateFromValue(LocalDateTime datetime, int year, int month, int day) {
        if (isValidDate(year, month, day)) {
            datetime = datetime.plusYears(year - datetime.getYear());
            datetime = datetime.plusMonths((month - datetime.getMonthValue()));
            datetime = datetime.plusDays(day - datetime.getDayOfMonth());
        }

        return datetime;
    }

    public static Boolean isValidDate(int year, int month, int day) {
        if (year < 1 || year > 9999) {
            return false;
        }

        Integer[] validDays = {
                31,
                year %4 == 0 && year%100 != 0 || year%400 == 0 ? 29 : 28,
                31,
                30,
                31,
                30,
                31,
                31,
                30,
                31,
                30,
                31
        };

        return month >= 1 && month <= 12 && day >= 1 && day <= validDays[month - 1];
    }

    public static LocalDateTime tryParse(String date) {
        try {
           return LocalDateTime.parse(date);
        } catch(DateTimeParseException ex) {
            return null;
        }
    }

    public static LocalDateTime next(LocalDateTime from, int dayOfWeek) {
        int start = from.getDayOfWeek().getValue();

        if (start == 0)
        {
            start = 7;
        }

        if (dayOfWeek == 0)
        {
            dayOfWeek = 7;
        }

        return from.plusDays(dayOfWeek - start + 7);
    }

    public static LocalDateTime thisDate(LocalDateTime from, int dayOfWeek) {
        int start = from.getDayOfWeek().getValue();

        if (start == 0)
        {
            start = 7;
        }

        if (dayOfWeek == 0)
        {
            dayOfWeek = 7;
        }

        return from.plusDays(dayOfWeek - start);
    }

    public static LocalDateTime last(LocalDateTime from, int dayOfWeek) {
        int start = from.getDayOfWeek().getValue();

        if (start == 0)
        {
            start = 7;
        }

        if (dayOfWeek == 0)
        {
            dayOfWeek = 7;
        }

        return from.plusDays(dayOfWeek - start - 7);
    }
}

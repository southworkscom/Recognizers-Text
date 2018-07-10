package com.microsoft.recognizers.text.datetime.utilities;

import java.time.LocalDateTime;

public class FormatUtil {

    public static String luisDate(Integer year, Integer month, Integer day)
    {
        if (year == -1)
        {
            if (month == -1)
            {
                if (day == -1)
                {
                    return String.join("-", "XXXX", "XX", "XX");
                }

                return String.join("-", "XXXX", "XX", String.format("D2", day.toString()));
            }

            return String.join("-", "XXXX", String.format("D2", month.toString()), String.format("D2", day.toString()));
        }

        return String.join("-", String.format("D4", year.toString()), String.format("D2", month.toString()), String.format("D2", day.toString()));
    }

    public static String luisDate(LocalDateTime date) {
        return luisDate(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
    }

    public static String luisDateTime(LocalDateTime time) {
        return luisDate(time) + "T" + luisTime(time.getHour(), time.getMinute(), time.getSecond());
    }

    public static String luisTime(int hour, int min, int second) {
        return String.join(":", String.format("D2", hour), String.format("D2", min), String.format("D2", second));
    }

    public static String formatDate(LocalDateTime date)
    {
        return String.join("-", String.format("D4", date.getYear()),  String.format("D2", date.getMonth()),  String.format("D2", date.getDayOfMonth()));
    }
}

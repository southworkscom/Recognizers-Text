package com.microsoft.recognizers.text.datetime.utilities;

public class FormatUtil {

    public static String LuisDate(Integer year, Integer month, Integer day)
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
}

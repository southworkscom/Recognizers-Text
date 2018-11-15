package com.microsoft.recognizers.text.datetime.utilities;

import com.microsoft.recognizers.text.datetime.DatePeriodTimexType;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TimexUtility {

    public static String generateDatePeriodTimex(LocalDateTime begin, LocalDateTime end, DatePeriodTimexType timexType) {
        String datePeriodTimex;

        if (timexType == DatePeriodTimexType.ByDay)
        {
            datePeriodTimex = "P" + ChronoUnit.DAYS.between(end,begin) + "D";
        }
        else if (timexType == DatePeriodTimexType.ByWeek)
        {
            datePeriodTimex = "P" + (ChronoUnit.WEEKS.between(end,begin) / 7) + "W";
        }
        else if (timexType == DatePeriodTimexType.ByMonth)
        {
            datePeriodTimex =  "P" + ChronoUnit.MONTHS.between(end,begin) + "M"; // ((end.getYear() - begin.getYear()) * 12) + (end.getMonthValue() - begin.getMonthValue());
        }
        else
        {
            double yearDiff = (end.getYear() - begin.getYear()) + (end.getMonthValue() - begin.getMonthValue()) / 12.0;
            datePeriodTimex = "P" + yearDiff + "Y";
        }

        return "(" + FormatUtil.luisDate(begin) + "," + FormatUtil.luisDate(end) + "," + datePeriodTimex + ")";
    }

    public static String generateWeekTimex() {
        return "XXXX-WXX";
    }

    public static String generateWeekTimex(LocalDateTime monday) {
        return FormatUtil.toIsoWeekTimex(monday);
    }

    public static String generateWeekendTimex() {
        return "XXXX-WXX-WE";
    }

    public static String generateWeekendTimex(LocalDateTime date) {
        return String.format("D4", date.getYear()) + "-W" +
                Cal.GetWeekOfYear(date,
                        CalendarWeekRule.FirstFourDayWeek,
                        DayOfWeek.MONDAY)
                        .ToString("D2") + "-WE";
    }
    public static String generateMonthTimex() {
        return "XXXX-XX";
    }

    public static String generateMonthTimex(LocalDateTime date) {
        return String.format("D4",date.getYear()) + "-" +
                String.format("D2",date.getMonthValue());
    }
    public static String generateYearTimex() {
        return "XXXX";
    }

    public static String generateYearTimex(LocalDateTime date) {
        return String.format("D4", date.getYear());
    }
}

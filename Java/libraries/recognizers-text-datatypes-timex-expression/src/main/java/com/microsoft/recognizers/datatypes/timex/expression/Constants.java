// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.recognizers.datatypes.timex.expression;

public class Constants {

    // Timex
    public static final String TimexYear = "Y";
    public static final String TimexMonth = "M";
    public static final String TimexMonthFull = "MON";
    public static final String TimexWeek = "W";
    public static final String TimexDay = "D";
    public static final String TimexBusinessDay = "BD";
    public static final String TimexWeekend = "WE";
    public static final String TimexHour = "H";
    public static final String TimexMinute = "M";
    public static final String TimexSecond = "S";
    public static final String TimexNight = "NI";
    public static final char TimexFuzzy = 'X';
    public static final String TimexFuzzyYear = "XXXX";
    public static final String TimexFuzzyMonth = "XX";
    public static final String TimexFuzzyWeek = "WXX";
    public static final String TimexFuzzyDay = "XX";
    public static final String DateTimexConnector = "-";
    public static final String TimeTimexConnector = ":";
    public static final String GeneralPeriodPrefix = "P";
    public static final String TimeTimexPrefix = "T";
    public static final String YearUnit = "year";
    public static final String MonthUnit = "month";
    public static final String WeekUnit = "week";
    public static final String DayUnit = "day";
    public static final String HourUnit = "hour";
    public static final String MinuteUnit = "minute";
    public static final String SecondUnit = "second";
    public static final String TimeDurationUnit = "s";
    public static final String AM = "AM";
    public static final String PM = "PM";
    public static final int InvalidValue = -1;

    public static class TimexTypes {
        public static final String PRESENT = "present";
        public static final String DEFINITE = "definite";
        public static final String DATE = "date";
        public static final String DATE_TIME = "datetime";
        public static final String DATE_RANGE = "daterange";
        public static final String DURATION = "duration";
        public static final String TIME = "time";
        public static final String TIME_RANGE = "timerange";
        public static final String DATE_TIME_RANGE = "datetimerange";
    }
}

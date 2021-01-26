// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.recognizers.datatypes.timex.expression;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

public class TimexProperty {
    private Time time;

    private String timexValue;

    private HashSet<String> types;

    private Boolean now;

    private BigDecimal years;

    private BigDecimal months;

    private BigDecimal weeks;

    private BigDecimal days;

    private BigDecimal hours;

    private BigDecimal minutes;

    private BigDecimal seconds;

    private Integer year;

    private Integer month;

    private Integer dayOfMonth;

    private Integer dayOfWeek;

    private String season;

    private Integer weekOfYear;

    private Boolean weekend;

    public Integer weekOfMonth;

    private Integer hour;

    private Integer minute;

    private Integer second;

    private String partOfDay;

    public TimexProperty() {

    }

    public TimexProperty(String timex) {
        TimexParsing.parseString(timex, this);
    }

    public String getTimexValue() {
        return TimexFormat.format(this);
    }

    public void setTimexValue(String withTimexValue) {
        this.timexValue = withTimexValue;
    }

    public HashSet<String> getTypes() {
        return TimexInference.infer(this);
    }

    public void setTypes(HashSet<String> withTypes) {
        this.types = withTypes;
    }

    public Boolean getNow() {
        return now;
    }

    public void setNow(Boolean withNow) {
        this.now = withNow;
    }

    public BigDecimal getYears() {
        return years;
    }

    public void setYears(BigDecimal withYears) {
        this.years = withYears;
    }

    public BigDecimal getMonths() {
        return months;
    }

    public void setMonths(BigDecimal withMonths) {
        this.months = withMonths;
    }

    public BigDecimal getWeeks() {
        return weeks;
    }

    public void setWeeks(BigDecimal withWeeks) {
        this.weeks = withWeeks;
    }

    public BigDecimal getDays() {
        return days;
    }

    public void setDays(BigDecimal withDays) {
        this.days = withDays;
    }

    public BigDecimal getHours() {
        return hours;
    }

    public void setHours(BigDecimal withHours) {
        this.hours = withHours;
    }

    public BigDecimal getMinutes() {
        return minutes;
    }

    public void setMinutes(BigDecimal withMinutes) {
        this.minutes = withMinutes;
    }

    public BigDecimal getSeconds() {
        return seconds;
    }

    public void setSeconds(BigDecimal withSeconds) {
        this.seconds = withSeconds;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer withYear) {
        this.year = withYear;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer withMonth) {
        this.month = withMonth;
    }

    public Integer getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(Integer withDayOfMonth) {
        this.dayOfMonth = withDayOfMonth;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer withDayOfWeek) {
        this.dayOfWeek = withDayOfWeek;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String withSeason) {
        this.season = withSeason;
    }

    public Integer getWeekOfYear() {
        return weekOfYear;
    }

    public void setWeekOfYear(Integer withWeekOfYear) {
        this.weekOfYear = withWeekOfYear;
    }

    public Boolean getWeekend() {
        return weekend;
    }

    public void setWeekend(Boolean withWeekend) {
        this.weekend = withWeekend;
    }

    public Integer getWeekOfMonth() {
        return weekOfMonth;
    }

    public void setWeekOfMonth(Integer withWeekOfMonth) {
        this.weekOfMonth = withWeekOfMonth;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer withHour) {
        if (withHour != null) {
            if (this.time == null) {
                this.time = new Time(withHour, 0, 0);
            } else {
                this.time.setHour(withHour);
            }
        } else {
            this.time = null;
        }

        this.hour = withHour;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer withMinute) {
        if (withMinute != null) {
            if (this.time == null) {
                time = new Time(0, withMinute, 0);
            } else {
                time.setMinute(withMinute);
            }
        } else {
            this.time = null;
        }

        this.minute = withMinute;
    }

    public Integer getSecond() {
        return second;
    }

    public void setSecond(Integer withSecond) {
        if (withSecond != null) {
            if (this.time == null) {
                this.time = new Time(0, 0, withSecond);
            } else {
                this.time.setSecond(withSecond);
            }
        } else {
            this.time = null;
        }

        this.second = withSecond;
    }

    public String getPartOfDay() {
        return partOfDay;
    }

    public void setPartOfDay(String wthPartOfDay) {
        this.partOfDay = wthPartOfDay;
    }

    public static TimexProperty fromDate(Calendar date) {
        TimexProperty timex = new TimexProperty() {
            {
                setYear(date.get(Calendar.YEAR));
                // We are adding +1 as Calendar starts from 0 (JANUARY)
                setMonth(date.get(Calendar.MONTH) + 1);
                setDayOfMonth(date.get(Calendar.DATE));
            }
        };
        return timex;
    }

    public static TimexProperty fromDateTime(Calendar datetime) {
        TimexProperty timex = TimexProperty.fromDate(datetime);
        timex.setHour(datetime.get(Calendar.HOUR_OF_DAY));
        timex.setMinute(datetime.get(Calendar.MINUTE));
        timex.setSecond(datetime.get(Calendar.SECOND));
        return timex;
    }

    public static TimexProperty fromTime(Time time) {
        return new TimexProperty() {
            {
                setHour(time.getHour());
                setMinute(time.getMinute());
                setSecond(time.getSecond());
            }
        };
    }

//    @Override
//    public String toString() {
//        return TimexConvert.convertTimexToString(this);
//    }

    public String toNaturalLanguage(Calendar referenceDate) {
        return TimexRelativeConvert.convertTimexToStringRelative(this, referenceDate);
    }

    public TimexProperty clone() {
        return new TimexProperty() {
            {
                setNow(this.getNow());
                setYears(this.getYears());
                setMonths(this.getMonths());
                setWeeks(this.getWeeks());
                setDays(this.getDays());
                setHours(this.getHours());
                setMinutes(this.getMinutes());
                setSeconds(this.getSeconds());
                setYear(this.getYear());
                setMonth(this.getMonth());
                setDayOfMonth(this.getDayOfMonth());
                setDayOfWeek(this.getDayOfWeek());
                setSeason(this.getSeason());
                setWeekOfYear(this.getWeekOfYear());
                setWeekend(this.getWeekend());
                setWeekOfMonth(this.getWeekOfMonth());
                setHour(this.getHour());
                setMinute(this.getMinute());
                setSecond(this.getSecond());
                setPartOfDay(this.getPartOfDay());
            }
        };
    }

    public void assignProperties(Map<String, String> source) {
        for (Entry<String, String> item : source.entrySet()) {
            switch (item.getKey()) {
                case "year":
                    this.year = Integer.parseInt(item.getValue());
                    break;
                case "month":
                    this.month = Integer.parseInt(item.getValue());
                    break;
                case "dayOfMonth":
                    this.dayOfMonth = Integer.parseInt(item.getValue());
                    break;
                case "dayOfWeek":
                    this.dayOfWeek = Integer.parseInt(item.getValue());
                    break;
                case "season":
                    this.season = item.getValue();
                    break;
                case "weekOfYear":
                    this.weekOfYear = Integer.parseInt(item.getValue());
                    break;
                case "weekend":
                    this.weekend = true;
                    break;
                case "weekOfMonth":
                    this.weekOfMonth = Integer.parseInt(item.getValue());
                    break;
                case "hour":
                    this.hour = Integer.parseInt(item.getValue());
                    break;
                case "minute":
                    this.minute = Integer.parseInt(item.getValue());
                    break;
                case "second":
                    this.second = Integer.parseInt(item.getValue());
                    break;
                case "partOfDay":
                    this.partOfDay = item.getValue();
                    break;
                case "dateUnit":
                    this.assignDateDuration(source);
                    break;
                case "timeUnit":
                    this.assignTimeDuration(source);
                    break;
                default:
            }
        }
    }

    private void assignDateDuration(Map<String, String> source) {
        switch (source.get("dateUnit")) {
            case "Y":
                this.years = new BigDecimal(source.get("amount"));
                break;
            case "M":
                this.months = new BigDecimal(source.get("amount"));
                break;
            case "W":
                this.weeks = new BigDecimal(source.get("amount"));
                break;
            case "D":
                this.days = new BigDecimal(source.get("amount"));
                break;
            default:
        }
    }

    private void assignTimeDuration(Map<String, String> source) {
        switch (source.get("timeUnit")) {
            case "H":
                this.hours = new BigDecimal(source.get("amount"));
                break;
            case "M":
                this.minutes = new BigDecimal(source.get("amount"));
                break;
            case "S":
                this.seconds = new BigDecimal(source.get("amount"));
                break;
            default:
        }
    }
}

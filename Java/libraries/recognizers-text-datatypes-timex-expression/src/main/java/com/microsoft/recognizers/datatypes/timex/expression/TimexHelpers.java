// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.recognizers.datatypes.timex.expression;

import java.util.Calendar;
import java.util.HashSet;

public class TimexHelpers {
    public static TimexRange expandDateTimeRange(TimexProperty timex) {
        HashSet<String> types = timex.getTypes().size() != 0 ? timex.getTypes() : TimexInference.infer(timex);

        if (types.contains(Constants.TimexTypes.DURATION)) {
            TimexProperty start = TimexHelpers.cloneDateTime(timex);
            TimexProperty duration = TimexHelpers.cloneDuration(timex);
            return new TimexRange() {
                {
                    setStart(start);
                    setEnd(TimexHelpers.timexDateTimeAdd(start, duration));
                    setDuration(duration);
                }
            };
        } else {
            if (timex.getYear() != null) {
                TimexRange range = new TimexRange() {
                    {
                        setStart(new TimexProperty() {
                            {
                                setYear(timex.getYear());
                            }
                        });
                        setEnd(new TimexProperty());
                    }
                };
                if (timex.getMonth() != null) {
                    range.getStart().setMonth(timex.getMonth());
                    range.getStart().setDayOfMonth(1);
                    range.getEnd().setYear(timex.getYear());
                    range.getEnd().setMonth(timex.getMonth() + 1);
                    range.getEnd().setDayOfMonth(1);
                } else {
                    range.getStart().setMonth(1);
                    range.getStart().setDayOfMonth(1);
                    range.getEnd().setYear(timex.getYear() + 1);
                    range.getEnd().setMonth(1);
                    range.getEnd().setDayOfMonth(1);
                }

                return range;
            }
        }

        return new TimexRange() {
            {
                setStart(new TimexProperty());
                setEnd(new TimexProperty());
            }
        };
    }

    public static TimexRange expandTimeRange(TimexProperty timex) {
        if (!timex.getTypes().contains(Constants.TimexTypes.TIME_RANGE)) {
            throw new IllegalArgumentException("argument must be a timerange: timex");
        }

        if (timex.getPartOfDay() != null) {
            switch (timex.getPartOfDay()) {
                case "DT":
                    timex = new TimexProperty(TimexCreator.DAYTIME);
                    break;
                case "MO":
                    timex = new TimexProperty(TimexCreator.MORNING);
                    break;
                case "AF":
                    timex = new TimexProperty(TimexCreator.AFTERNOON);
                    break;
                case "EV":
                    timex = new TimexProperty(TimexCreator.EVENING);
                    break;
                case "NI":
                    timex = new TimexProperty(TimexCreator.NIGHT);
                    break;
                default:
                    throw new IllegalArgumentException("unrecognized part of day timerange: timex");
            }
        }

        Integer hour = timex.getHour();
        Integer minute = timex.getMinute();
        Integer second = timex.getSecond();
        TimexProperty start = new TimexProperty() {
            {
                setHour(hour);
                setMinute(minute);
                setSecond(second);
            }
        };
        TimexProperty duration = TimexHelpers.cloneDuration(timex);

        return new TimexRange() {
            {
                setStart(start);
                setEnd(TimexHelpers.timeAdd(start, duration));
                setDuration(duration);
            }
        };
    }

    public static TimexProperty timexDateAdd(TimexProperty start, TimexProperty duration) {
        if (start.getDayOfWeek() != null) {
            TimexProperty end = start.clone();
            if (duration.getDays() != null) {
                end.setDayOfWeek(end.getDayOfWeek() + (int)Math.round(duration.getDays()));
            }

            return end;
        }

        if (start.getMonth() != null && start.getDayOfMonth() != null) {
            Double durationDays = duration.getDays();

            if (durationDays == null && duration.getWeeks() != null) {
                durationDays = 7 * duration.getWeeks();
            }

            if (durationDays != null) {
                if (start.getYear() != null) {
                    Calendar d = Calendar.getInstance();
                    d.set(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0, 0, 0);
                    d.add(Calendar.DATE, (int)Math.round(durationDays));
                    return new TimexProperty() {
                        {
                            setYear(d.get(Calendar.YEAR));
                            setMonth(d.get(Calendar.MONTH));
                            setDayOfMonth(d.get(Calendar.DATE));
                        }
                    };
                } else {
                    Calendar d = Calendar.getInstance();
                    d.set(2001, start.getMonth(), start.getDayOfMonth(), 0, 0, 0);
                    d.add(Calendar.DATE, (int)Math.round(durationDays));
                    return new TimexProperty() {
                        {
                            setMonth(d.get(Calendar.MONTH));
                            setDayOfMonth(d.get(Calendar.DATE));
                        }
                    };
                }
            }

            if (duration.getYears() != null) {
                if (start.getYear() != null) {
                    return new TimexProperty() {
                        {
                            setYear(start.getYear() + (int)Math.round(duration.getYears()));
                            setMonth(start.getMonth());
                            setDayOfMonth(start.getDayOfMonth());
                        }
                    };
                }
            }

            if (duration.getMonths() != null) {
                if (start.getMonth() != null) {
                    return new TimexProperty() {
                        {
                            setYear(start.getYear());
                            setMonth(start.getMonth() + (int)Math.round(duration.getMonths()));
                            setDayOfMonth(start.getDayOfMonth());
                        }
                    };
                }
            }
        }

        return start;
    }

    public static TimexProperty timexTimeAdd(TimexProperty start, TimexProperty duration) {
        if (duration.getHours() != null) {
            TimexProperty result = start.clone();
            result.setHour(result.getHour() + (int)Math.round(duration.getHours()));
            if (result.getHour() > 23) {
                Double days = Math.floor(result.getHour() / 24d);
                Integer hour = result.getHour() % 24;
                result.setHour(hour);

                if (result.getYear() != null && result.getMonth() != null && result.getDayOfMonth() != null) {
                    Calendar d = Calendar.getInstance();
                    d.set(result.getYear(), result.getMonth(), result.getDayOfMonth(), 0, 0, 0);
                    d.add(Calendar.DATE, (int)Math.round(days));

                    result.setYear(d.get(Calendar.YEAR));
                    result.setMonth(d.get(Calendar.MONTH));
                    result.setDayOfMonth(d.get(Calendar.DATE));

                    return result;
                }

                if (result.getDayOfWeek() != null) {
                    result.setDayOfWeek(result.getDayOfWeek() + (int)Math.round(days));
                    return result;
                }
            }

            return result;
        }

        if (duration.getMinutes() != null) {
            TimexProperty result = start.clone();
            result.setMinute(result.getMinute() + (int)Math.round(duration.getMinutes()));

            if (result.getMinute() > 59) {
                result.setHour(result.getHour() + 1);
                result.setMinute(0);
            }

            return result;
        }

        return start;
    }

    public static TimexProperty timexDateTimeAdd(TimexProperty start, TimexProperty duration) {
        return TimexHelpers.timexDateAdd(TimexHelpers.timexDateAdd(start, duration), duration);
    }

    public static Calendar dateFromTimex(TimexProperty timex) {
        Integer year = timex.getYear() != null ? timex.getYear() : 2001;
        Integer month = timex.getMonth() != null ? timex.getMonth() : 1;
        Integer day = timex.getDayOfMonth() != null ? timex.getDayOfMonth() : 1;
        Integer hour = timex.getHour() != null ? timex.getHour() : 0;
        Integer minute = timex.getMinute() != null ? timex.getMinute() : 0;
        Integer second = timex.getSecond() != null ? timex.getSecond() : 0;
        Calendar date = Calendar.getInstance();
        date.set(year, month, day, hour, minute, second);

        return date;
    }

    public static Time timeFromTimex(TimexProperty timex) {
        Integer hour = timex.getHour() != null ? timex.getHour() : 0;
        Integer minute = timex.getMinute() != null ? timex.getMinute() : 0;
        Integer second = timex.getSecond() != null ? timex.getSecond() : 0;
        return new Time(hour, minute, second);
    }

    public static DateRange dateRangeFromTimex(TimexProperty timex) {
        TimexRange expanded = TimexHelpers.expandDateTimeRange(timex);
        return new DateRange() {
            {
                setStart(TimexHelpers.dateFromTimex(expanded.getStart()));
                setEnd(TimexHelpers.dateFromTimex(expanded.getEnd()));
            }
        };
    }

    public static TimeRange timeRangeFromTimex(TimexProperty timex) {
        TimexRange expanded = TimexHelpers.expandTimeRange(timex);
        return new TimeRange() {
            {
                setStart(TimexHelpers.timeFromTimex(expanded.getStart()));
                setEnd(TimexHelpers.timeFromTimex(expanded.getEnd()));
            }
        };
    }

    private static TimexProperty timeAdd(TimexProperty start, TimexProperty duration) {
        Integer hourPreSet = start.getHour() + (int)Math.round(start.getHours());
        Integer hour = hourPreSet != null ? hourPreSet : 0;
        Integer minutePreSet = start.getMinute() + (int)Math.round(duration.getMinutes());
        Integer minute = minutePreSet != null ? minutePreSet : 0;
        Integer secondPreSet = start.getSecond() + (int)Math.round(duration.getSeconds());
        Integer second = secondPreSet != null ? secondPreSet : 0;
        return new TimexProperty() {
            {
                setHour(hour);
                setMinute(minute);
                setSecond(second);
            }
        };
    }

    private static TimexProperty cloneDateTime(TimexProperty timex) {
        TimexProperty result = timex.clone();
        result.setYears(null);
        result.setMonths(null);
        result.setWeeks(null);
        result.setDays(null);
        result.setHours(null);
        result.setMinutes(null);
        result.setSeconds(null);
        return result;
    }

    private static TimexProperty cloneDuration(TimexProperty timex) {
        TimexProperty result = timex.clone();
        result.setYear(null);
        result.setMonth(null);
        result.setDayOfMonth(null);
        result.setDayOfWeek(null);
        result.setWeekOfYear(null);
        result.setWeekOfMonth(null);
        result.setSeason(null);
        result.setHour(null);
        result.setMinute(null);
        result.setSecond(null);
        result.setWeekend(null);
        result.setPartOfDay(null);
        return result;
    }
}

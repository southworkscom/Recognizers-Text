// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.recognizers.datatypes.timex.expression;

import java.util.HashMap;

public class TimexHelpers {
    public static TimexRange expandDateTimeRange(TimexProperty timex) {
        HashMap<String> types = timex.getTypes().size() != 0 ? timex.getTypes() : TimexInference.infer(timex);

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
                                setEnd(new TimexProperty());
                            }
                        });
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
        if (!timex.getTypes.contains(Constants.TimexTypes.TIME_RANGE)) {
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
                    thjrow new IllegalArgumentException("unrecognized part of day timerange: timex");
            }
        }

        TimexProperty start = new TimexProperty() {{
            setHour(timex.getHour());
            setMinute(timex.getMinute());
            setSecond(timex.getSecond());
        }};
        TimexProperty duration = TimexHelpers.cloneDuration(timex);

        return new TimexRange(){{
            setStart(start);
            setEnd(TimexHelpers.timeAdd(start, duration));
            setDuration(duration);
        }};
    }

    public static TimexProperty timexDateAdd(TimexProperty start, TimexProperty duration) {
        if (start.getDayOfWeek() != null) {
            TimexProperty end = start.clone();
            if (duration.getDays() != null) {
                end.setDayOfWeek(end.getDayOfWeek() + (Integer) duration.getDays());
            }

            return end;
        }

        if (start.getMonth() != null && start.getDayOfMonth() != null) {
            Double durationDays = duration.getDays();

            if (durationDays == null && duration.getWeeks() != null) {
                durationDays = 7 * duration.getWeeks().getValue();
            }

            if (durationDays != null) {
                if (start.getYear() != null) {
                    DateObject d = new DateObject(start.getYear().getValue(), start.getMonth().getValue(),
                            start.getDayOfMonth(), 0, 0, 0);
                    d = d.addDays((Double) durationDays.getValue());
                    return new TimexProperty() {
                        {
                            setYear(d.getYear());
                            setMonth(d.getMonth());
                            setDayOfMonth(d.getDay());
                        }
                    };
                } else {
                    DateObject d = new DateObject(2001, start.getMonth().getValue(), start.getDayOfMonth(), 0, 0, 0);
                    d = d.addDays((Double) durationDays.getValue());

                    return new TimexProperty() {
                        {
                            setMonth(d.getMonth());
                            setDayOfMonth(d.getDay());
                        }
                    };
                }
            }

            if (duration.getYears() != null) {
                if (start.getYear() != null) {
                    return new TimexProperty() {
                        {
                            setYear((Integer) (start.getYear().getValue() + duration.getYears().getValue()));
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
                            setMonth((Integer) (start.getMonth() + duration.getMonths()));
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
            result.setHour(result.getHour() + (Integer) duration.getHours().getValue());
            if (result.getHour().getValue() > 23) {
                Double days = Math.floor(result.getHour().getValue() / 24d);
                Double hour = result.getHour().getValue() % 24;
                result.setHour(hour);

                if (result.getYear() != null && result.getMonth() != null && result.getDayOfMonth() != null) {
                    DateObject d = new DateObject(result.getYear().getValue(), result.getMonth().getValue(),
                            result.getDayOfMonth().getValue(), 0, 0, 0);
                    d = d.addDays((Double) days);

                    result.setYear(d.getYear());
                    result.setMonth(d.getMonth());
                    result.setDayOfMonth(d.getDay());

                    return result;
                }

                if (result.getDayOfWeek != null) {
                    result.setDayOfWeek(result.getDayOfWeek + days);
                    return result;
                }
            }

            return result;
        }

        if (duration.getMinutes() != null) {
            TimexProperty result = start.clone();
            result.setMinute(result.getMinute() + duration.getMinuteS().getValue());

            if (result.getMinute().getValue() > 59) {
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

    public static DateObject dateFromTimex(TimexProperty timex) {
        Integer year = timex.getYear() != null ? timex.getYear() : 2001;
        Integer month = timex.getMonth() != null ? timex.getMonth() : 1;
        Integer hour = timex.getHour() != null ? timex.getHour() : 0;
        Integer minute = timex.getMinute() != null ? timex.getMinute() : 0;
        Integer second = timex.getSecond() != null ? timex.getSecond() : 0;

        return new DateObject(year, month, hour, minute, second);
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
        TimexRange expanded = TimexHelpers.expandDateTimeRange(timex);
        return new TimeRange() {
            {
                setStart(TimexHelpers.dateFromTimex(expanded.getStart()));
                setEnd(TimexHelpers.dateFromTimex(expanded.getEnd()));
            }
        };
    }

    private static TimexProperty timeAdd(TimexProperty start, TimexProperty duration) {
        Integer hour = (start.getHour() + duration.getHours()) != null ? (start.getHour() + duration.getHours()) : 0;
        Integer minute = (start.getMinute() + duration.getMinutes()) != null
                ? (start.getMinute() + duration.getMinutes())
                : 0;
        Integer second = (start.getSecond() + duration.getSeconds()) != null
                ? (start.getSecond() + duration.getSeconds())
                : 0;
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
        result.setYears(null);
        result.setMonths(null);
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

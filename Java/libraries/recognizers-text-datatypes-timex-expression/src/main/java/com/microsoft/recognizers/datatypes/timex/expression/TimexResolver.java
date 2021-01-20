// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.recognizers.datatypes.timex.expression;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

public class TimexResolver {
    public static Resolution resolve(String[] timexArray, DateTime date) {
        Resolution resolution = new Resolution();
        for (String timex : timexArray) {
            TimexProperty t = new TimexProperty(timex);
            List<Resolution.Entry> r = TimexResolver.resolveTimex(t, date);
            resolution.getValues().addAll(r);
        }

        return resolution;
    }

    private static List<Resolution.Entry> resolveTimex(TimexProperty timex, DateTime date) {
        HashSet<String> types = timex.getTypes().size() != 0 ? timex.getTypes() : TimexInference.infer(timex);

        if (types.contains(Constants.TimexTypes.DATE_TIME_RANGE)) {
            return TimexResolver.resolveDateTimeRange(timex, date);
        }

        if (types.contains(Constants.TimexTypes.DEFINITE) && types.contains(Constants.TimexTypes.TIME)) {
            return TimexResolver.resolveDefiniteTime(timex, date);
        }

        if (types.contains(Constants.TimexTypes.DATE_RANGE)) {
            return TimexResolver.resolveDateRange(timex, date);
        }

        if (types.contains(Constants.TimexTypes.DEFINITE)) {
            return TimexResolver.resolveDefinite(timex);
        }

        if (types.contains(Constants.TimexTypes.TIME_RANGE)) {
            return TimexResolver.resolveTimeRange(timex, date);
        }

        if (types.contains(Constants.TimexTypes.DATE_TIME)) {
            return TimexResolver.resolveDateTime(timex, date);
        }

        if (types.contains(Constants.TimexTypes.DURATION)) {
            return TimexResolver.resolveDuration(timex);
        }

        if (types.contains(Constants.TimexTypes.DATE)) {
            return TimexResolver.resolveDate(timex, date);
        }

        if (types.contains(Constants.TimexTypes.TIME)) {
            return TimexResolver.resolveTime(timex, date);
        }

        return new ArrayList<Resolution.Entry>();
    }

    private static List<Resolution.Entry> resolveDefiniteTime(TimexProperty timex, DateTime date) {
        return new ArrayList<Resolution.Entry>() {
            {
                add(new Resolution.Entry() {
                    {
                        setTimex(timex.getTimexValue());
                        setType("datetime");
                        setValue(String.format("%1$s %2$s", TimexValue.dateValue(timex),
                                TimexValue.timeValue(timex, date)));
                    }
                });
            }
        };
    }

    private static List<Resolution.Entry> resolveDefinite(TimexProperty timex) {
        return new ArrayList<Resolution.Entry>() {
            {
                add(new Resolution.Entry() {
                    {
                        setTimex(timex.getTimexValue());
                        setType("datetime");
                        setValue(TimexValue.dateValue(timex));
                    }
                });
            }
        };
    }

    private static List<Resolution.Entry> resolveDefiniteDateRange(TimexProperty timex, DateTime date) {
        TimexRange range = TimexHelpers.expandDateTimeRange(timex);
        return new ArrayList<Resolution.Entry>() {
            {
                add(new Resolution.Entry() {
                    {
                        setTimex(timex.getTimexValue());
                        setType("datetime");
                        setStart(TimexValue.dateValue(range.getStart()));
                        setStart(TimexValue.dateValue(range.getEnd()));
                    }
                });
            }
        };
    }

    private static List<Resolution.Entry> resolveDate(TimexProperty timex, DateTime date) {
        return new ArrayList<Resolution.Entry>() {
            {
                add(new Resolution.Entry() {
                    {
                        setTimex(timex.getTimexValue());
                        setType("date");
                        setValue(TimexResolver.lastDateValue(timex, date));
                    }
                });
                add(new Resolution.Entry() {
                    {
                        setTimex(timex.getTimexValue());
                        setType("date");
                        setValue(TimexResolver.nextDateValue(timex, date));
                    }
                });
            }
        };
    }

    private static String lastDateValue(TimexProperty timex, DateTime date) {
        if (timex.getMonth() != null && timex.getDayOfMonth() != null) {
            return TimexValue.dateValue(new TimexProperty() {
                {
                    setYear(date.getYear() - 1);
                    setMonth(timex.getMonth());
                    setDayOfMonth(timex.getDayOfMonth());
                }
            });
        }

        if (timex.getDayOfWeek() != null) {
            DayOfWeek day = timex.getDayOfWeek() == 7 ? DayOfWeek.SUNDAY : DayOfWeek.of(timex.getDayOfWeek());
            DateTime result = TimexDateHelpers.dateOfNextDay(day, date);
            return TimexValue.dateValue(new TimexProperty() {
                {
                    setYear(result.getYear());
                    setMonth(result.getMonth());
                    setDayOfMonth(result.getDay());
                }
            });
        }

        return new String();
    }

    private static String nextDateValue(TimexProperty timex, DateTime date) {
        if (timex.getMonth() != null && timex.getDayOfMonth() != null) {
            return TimexValue.dateValue(new TimexProperty() {
                {
                    setYear(date.getYear());
                    setMonth(timex.getMonth());
                    setDayOfMonth(timex.getDayOfMonth());
                }
            });
        }

        if (timex.getDayOfWeek() != null) {
            DayOfWeek day = timex.getDayOfWeek() == 7 ? DayOfWeek.SUNDAY : DayOfWeek.of(timex.getDayOfWeek());
            DateTime result = TimexDateHelpers.dateOfNextDay(day, date);
            return TimexValue.dateValue(new TimexProperty() {
                {
                    setYear(result.getYear());
                    setMonth(result.getMonth());
                    setDayOfMonth(result.getDay());
                }
            });
        }

        return new String();
    }

    private static List<Resolution.Entry> resolveTime(TimexProperty timex, DateTime date) {
        return new ArrayList<Resolution.Entry>() {
            {
                add(new Resolution.Entry() {
                    {
                        setTimex(timex.getTimexValue());
                        setType("time");
                        setValue(TimexValue.timeValue(timex, date));
                    }
                });
            }
        };
    }

    private static List<Resolution.Entry> resolveDuration(TimexProperty timex) {
        return new ArrayList<Resolution.Entry>() {
            {
                add(new Resolution.Entry() {
                    {
                        setTimex(timex.getTimexValue());
                        setType("duration");
                        setValue(TimexValue.durationValue(timex));
                    }
                });
            }
        };
    }

    // TODO: research about Pair
    private static Pair<String, String> yearDateRange(Integer year) {

    }

    // TODO: research about Pair
    private static Pair<String, String> monthDateRange(Integer year, Integer month) {

    }

    // TODO: research about Pair
    private static Pair<String, String> yearWeekDateRange(Integer year, Integer weekOfYear, Boolean isWeekend) {

    }

    // this is based on
    // https://stackoverflow.com/questions/19901666/get-date-of-first-and-last-day-of-week-knowing-week-number/34727270
    private static DateTime firstDateOfWeek(Integer year, Integer weekOfYear, CultureInfo cultureInfo) {
        // ISO uses FirstFourDayWeek, and Monday as first day of week, according to
        // https://en.wikipedia.org/wiki/ISO_8601
        DateTime jan1 = new DateTime(year, 1, 1);
        Integer daysOffset = DayOfWeek.MONDAY - jan1.getDayOfWeek;
        DateTime firstWeekDay = jan1.addDays(daysOffset);

        int firstWeek = cultureInfo.Calendar.GetWeekOfYear(jan1, System.Globalization.CalendarWeekRule.FirstFourDayWeek,
                DayOfWeek.Monday);
        if ((firstWeek <= 1 || firstWeek >= 52) && daysOffset >= -3) {
            weekOfYear -= 1;
        }

        return firstWeekDay.addDays(weekOfYear * 7);
    }

    // TODO: research about Pair
    private static Pair<String, String> monthWeekDateRange(Integer year, Integer month, Integer weekOfYear) {

    }

    private static List<Resolution.Entry> resolveDateRange(TimexProperty timex, DateTime date) {
        if (timex.getSeason() != null) {
            return new ArrayList<Resolution.Entry>() {
                {
                    add(new Resolution.Entry() {
                        {
                            setTimex(timex.getTimexValue());
                            setType("daterange");
                            setValue("not resolved");
                        }
                    });
                }
            };
        } else {
            if (timex.getYear() != null && timex.getMonth() != null) {
                Pair<String, String> dateRange = TimexResolver.monthDateRange(timex.getYear(), timex.getMonth());
                return new ArrayList<Resolution.Entry>() {
                    {
                        add(new Resolution.Entry() {
                            {
                                setTimex(timex.getTimexValue());
                                setType("daterange");
                                setStart(dateRange.getLeft());
                                setEnd(dateRange.getRight());
                            }
                        });
                    }
                };
            }

            if (timex.getYear() != null && timex.getWeekOfYear() != null) {
                Pair<String, String> dateRange = TimexResolver.yearWeekDateRange(timex.getYear(), timex.getWeekOfYear(),
                        timex.getWeekend());

                return new ArrayList<Resolution.Entry>() {
                    {
                        add(new Resolution.Entry() {
                            {
                                setTimex(timex.getTimexValue());
                                setType("daterange");
                                setStart(dateRange.getLeft());
                                setEnd(dateRange.getRight());
                            }
                        });
                    }
                };
            }

            if (timex.getMonth() != null && timex.getWeekOfMonth() != null) {
                Pair<String, String> lastYearDateRange = TimexResolver.monthWeekDateRange(date.getYear() - 1,
                        timex.getMonth(), timex.getWeekOfMonth());
                Pair<String, String> thisYearDateRange = TimexResolver.monthWeekDateRange(date.getYear(),
                        timex.getMonth(), timex.getWeekOfMonth());

                return new ArrayList<Resolution.Entry>() {
                    {
                        add(new Resolution.Entry() {
                            {
                                setTimex(timex.getTimexValue());
                                setType("daterange");
                                setStart(lastYearDateRange.getLeft());
                                setEnd(lastYearDateRange.getRight());
                            }
                        });
                        add(new Resolution.Entry() {
                            {
                                setTimex(timex.getTimexValue());
                                setType("daterange");
                                setStart(thisYearDateRange.getLeft());
                                setEnd(thisYearDateRange.getRight());
                            }
                        });
                    }
                };
            }

            if (timex.getMonth() != null) {
                Integer y = date.getYear();
                Pair<String, String> lastYearDateRange = TimexResolver.monthDateRange(y - 1, timex.getMonth());
                Pair<String, String> thisYearDateRange = TimexResolver.monthDateRange(y, timex.getMonth());

                return new ArrayList<Resolution.Entry>() {
                    {
                        add(new Resolution.Entry() {
                            {
                                setTimex(timex.getTimexValue());
                                setType("daterange");
                                setStart(lastYearDateRange.getLeft());
                                setEnd(lastYearDateRange.getRight());
                            }
                        });
                        add(new Resolution.Entry() {
                            {
                                setTimex(timex.getTimexValue());
                                setType("daterange");
                                setStart(thisYearDateRange.getLeft());
                                setEnd(thisYearDateRange.getRight());
                            }
                        });
                    }
                };
            }

            if (timex.getYear() != null) {
                Pair<String, String> dateRange = TimexResolver.yearDateRange(timex.getYear());

                return new ArrayList<Resolution.Entry>() {
                    {
                        add(new Resolution.Entry() {
                            {
                                setTimex(timex.getTimexValue());
                                setType("daterange");
                                setStart(dateRange.getLeft());
                                setEnd(dateRange.getRight());
                            }
                        });
                    }
                };
            }

            return new ArrayList<Resolution.Entry>();
        }
    }

    // TODO: research about Pair
    private static Pair<String, String> partOfDayTimeRange(TimexProperty timex) {

    }

    private static List<Resolution.Entry> resolveTimeRange(TimexProperty timex, DateTime date) {
        if (timex.getPartOfDay() != null) {
            Pair<String, String> range = TimexResolver.partOfDayTimeRange(timex);
            return new ArrayList<Resolution.Entry>() {
                {
                    add(new Resolution.Entry() {
                        {
                            setTimex(timex.getTimexValue());
                            setType("timerange");
                            setStart(range.getLeft());
                            setEnd(range.getRight());
                        }
                    });
                }
            };
        } else {
            TimexRange range = TimexHelpers.expandTimeRange(timex);
            return new ArrayList<Resolution.Entry>() {
                {
                    add(new Resolution.Entry() {
                        {
                            setTimex(timex.getTimexValue());
                            setType("timerange");
                            setStart(TimexValue.timeValue(range.getStart(), date));
                            setEnd(TimexValue.timeValue(range.getEnd(), date));
                        }
                    });
                }
            };
        }
    }

    private static List<Resolution.Entry> resolveDateTime(TimexProperty timex, DateTime date) {
        List<Resolution.Entry> resolvedDates = TimexResolver.resolveDate(timex, date);
        for (Resolution.Entry resolved : resolvedDates) {
            resolved.setType("datetime");
            resolved.setValue(String.format("%1$s %2$s", resolved.getValue(), TimexValue.timeValue(timex, date)));
        }

        return resolvedDates;
    }

    private static List<Resolution.Entry> resolveDateTimeRange(TimexProperty timex, DateTime date) {
        if (timex.getPartOfDay() != null) {
            String dateValue = TimexValue.dateValue(timex);
            Pair<String, String> timeRange = TimexResolver.partOfDayTimeRange(timex);
            return new ArrayList<Resolution.Entry>() {
                {
                    add(new Resolution.Entry() {
                        {
                            setTimex(timex.getTimexValue());
                            setType("datetimerange");
                            setStart(String.format("%1$s %2$s", dateValue, timeRange.getLeft()));
                            setEnd(String.format("%1$s %2$s", dateValue, timeRange.getRight()));
                        }
                    });
                }
            };
        } else {
            TimexRange range = TimexHelpers.expandDateTimeRange(timex);
            return new ArrayList<Resolution.Entry>() {
                {
                    add(new Resolution.Entry() {
                        {
                            setTimex(timex.getTimexValue());
                            setType("datetimerange");
                            setStart(String.format("%1$s %2$s", TimexValue.dateValue(range.getStart()),
                                    TimexValue.timeValue(range.getStart())));
                            setEnd(String.format("%1$s %2$s", TimexValue.dateValue(range.getEnd()),
                                    TimexValue.timeValue(range.getEnd())));
                        }
                    });
                }
            };
        }
    }
}

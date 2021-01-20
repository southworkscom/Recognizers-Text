// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.recognizers.datatypes.timex.expression;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TimexRangeResolver {
    public static List<TimexProperty> evaluate(List<String> candidates, List<String> constraints) {
        List<TimexProperty> timexConstraints = constraints.stream().map(x -> {
            return new TimexProperty(x);
        }).collect(Collectors.toList());
        List<String> candidatesWithDurationsResolved = TimexRangeResolver.resolveDurations(candidates,
                timexConstraints);
        List<String> candidatesAccordingToDate = TimexRangeResolver
                .resolveByDateRangeConstraints(candidatesWithDurationsResolved, timexConstraints);
        List<String> candidatesWithAddedTime = TimexRangeResolver.resolveByTimeConstraints(candidatesAccordingToDate,
                timexConstraints);
        List<String> candidatesFilteredByTime = TimexRangeResolver
                .resolveByTimerangeConstraitns(candidatesWithAddedTime, timexConstraints);

        List<TimexProperty> timexResults = candidatesFilteredByTime.stream().map(x -> {
            return new TimexProperty(x);
        }).collect(Collectors.toList());

        return timexResults;
    }

    public static List<String> resolveDurations(List<String> candidates, List<TimexProperty> constraints) {
        List<String> results = new ArrayList<String>();
        for (String candidate : candidates) {
            TimexProperty timex = new TimexProperty(candidate);
            if (timex.getTypes().contains(Constants.TimexTypes.DURATION)) {
                List<TimexProperty> r = TimexRangeResolver.resolveDuration(timex, constraints);
                for (TimexProperty resolved : r) {
                    results.add(resolved.getTimexValue());
                }
            } else {
                results.add(candidate);
            }
        }

        return results;
    }

    private static List<TimexProperty> resolveDuration(TimexProperty candidate, List<TimexProperty> constraints) {
        List<TimexProperty> results = new ArrayList<TimexProperty>();
        for (TimexProperty constraint : constraints) {
            if (constraint.getTypes().contains(Constants.TimexTypes.DATE_TIME)) {
                results.add(TimexHelpers.timexDateTimeAdd(constraint, candidate));
            } else if (constraint.getTypes().contains(Constants.TimexTypes.TIME)) {
                results.add(TimexHelpers.timexTimeAdd(constraint, candidate));
            }
        }

        return results;
    }

    private static List<String> resolveByDateRangeConstraints(List<String> candidates,
            List<TimexProperty> timexConstraints) {
        List<DateRange> dateRangeconstraints = timexConstraints.stream().filter(timex -> {
            return timex.getTypes().contains(Constants.TimexTypes.DATE_RANGE);
        }).map(timex -> {
            return TimexHelpers.dateRangeFromTimex(timex);
        }).collect(Collectors.toList());

        List<DateRange> collapseDateRanges = TimexConstraintsHelper.collapse(dateRangeconstraints);

        if (collapseDateRanges.isEmpty()) {
            return candidates;
        }

        List<String> resolution = new ArrayList<String>();
        for (String timex : candidates) {
            List<String> r = TimexRangeResolver.resolveDate(new TimexProperty(timex), collapseDateRanges);
            resolution.addAll(r);
        }

        return TimexRangeResolver.removeDuplicates(resolution);
    }

    private static List<String> resolveDate(TimexProperty timex, List<DateRange> constraints) {
        List<String> result = new ArrayList<String>();
        for (DateRange constraint : constraints) {
            result.addAll(TimexRangeResolver.resolveDateAgainstConstraint(timex, constraint));
        }

        return result;
    }

    private static List<String> resolveByTimeRangeConstraints(List<String> candidates,
            List<TimexProperty> timexConstrainst) {
        List<TimeRange> timeRangeConstraints = timexConstrainst.stream().filter(timex -> {
            return timex.getTypes().contains(Constants.TimexTypes.TIME_RANGE);
        }).map(timex -> {
            return TimexHelpers.timeRangeFromTimex(timex);
        }).collect(Collectors.toList());

        List<TimeRange> collapsedTimeRanges = TimexConstraintsHelper.collapse(timeRangeConstraints);

        if (collapsedTimeRanges.isEmpty()) {
            return candidates;
        }

        List<String> resolution = new ArrayList<String>();
        for (String timex : candidates) {
            TimexProperty t = new TimexProperty(timex);
            if (t.getTypes().contains(Constants.TimexTypes.TIME_RANGE)) {
                List<String> r = TimexRangeResolver.resolveTimeRange(t, collapsedTimeRanges);
                resolution.addAll(r);
            } else if (t.getTypes().contains(Constants.TimexTypes.TIME)) {
                List<String> r = TimexRangeResolver.resolveTime(t, collapsedTimeRanges);
                resolution.addAll(r);
            }
        }

        return TimexRangeResolver.removeDuplicates(resolution);
    }

    private static List<String> resolveTimeRange(TimexProperty timex, List<TimeRange> constraints) {
        TimeRange candidate = TimexHelpers.timeRangeFromTimex(timex);

        List<String> result = new ArrayList<String>();
        for (TimeRange constraint : constraints) {
            if (TimexConstraintsHelper.isOverlapping(candidate, constraint)) {
                Integer start = Math.max(candidate.getStart().getTime(), constraint.getStart().getTime());
                Time time = new Time(start);

                // TODO: consider a method on TimexProperty to do this clone/overwrite pattern
                TimexProperty resolved = timex.clone();
                resolved.setPartOfDay(null);
                resolved.setSeconds(null);
                resolved.setMinutes(null);
                resolved.setHours(null);
                resolved.setSecond(time.getSecond());
                resolved.setMinute(time.getMinute());
                resolved.setHour(time.getHour());

                result.add(resolved.getTimexValue());
            }
        }

        return result;
    }

    private static List<String> resolveTime(TimexProperty timex, List<TimeRange> constraints) {
        List<String> result = new ArrayList<String>();
        for (TimeRange constraint : constraints) {
            result.addAll(TimexRangeResolver.resolveTimeAgainstConstraint(timex, constraint));
        }

        return result;
    }

    private static List<String> resolveTimeAgainstConstraint(TimexProperty timex, TimeRange constraint) {
        Time t = new Time(timex.getHour(), timex.getMinute(), timex.getSecond());
        if (t.getTime() >= constraint.getStart().getTime() && t.getTime() < constraint.getEnd().getTime()) {
            return new ArrayList<String>() {
                {
                    add(timex.getTimexValue());
                }
            };
        }

        return new ArrayList<String>();
    }

    private static Set<String> removeDuplicates(List<String> original) {
        return new HashSet<String>(original);
    }

    private static List<String> resolveDefiniteAgainstConstraint(TimexProperty timex, DateRange constraint) {
        DateObject timexDate = TimexHelpers.dateFromTimex(timex);
        if (timexDate >= constraint.getStart() && timexDate < constraint.getEnd()) {
            return new ArrayList<String>() {
                {
                    add(timex.getTimexValue());
                }
            };
        }

        return new ArrayList<String>();
    }

    private static List<String> resolveDateAgainstConstraint(TimexProperty timex, DateRange constraint) {
        if (timex.getMonth() != null && timex.getDayOfMonth() != null) {
            List<String> result = new ArrayList<String>();
            for (int year = constraint.getStart().getYear(); year < constraint.getEnd().getYear(); year++) {
                TimexProperty t = timex.clone();
                t.setYear(year);
                result.addAll(TimexRangeResolver.resolveDefiniteAgainstConstraint(timex, constraint));
            }

            return result;
        }

        if (timex.getDayOfWeek() != null) {
            // convert between ISO day of week and .NET day of week
            DayOfWeek day = timex.getDayOfWeek() == 7 ? DayOfWeek.SUNDAY : DayOfWeek.of(timex.getDayOfWeek());
            List<DateObject> dates = TimexDateHelpers.datesMatchingDay(day, constraint.getStart(), constraint.getEnd());
            List<String> result = new ArrayList<String>();

            for (DateObject d : dates) {
                TimexProperty t = timex.clone();
                t.setDayOfWeek(null);
                t.setYear(d.getYear());
                t.setMonth(d.getMonth());
                t.setDayOfMonth(d.getDay());
                result.add(t.getTimexValue());
            }

            return result;
        }

        if (timex.getHour() != null) {
            List<String> result = new ArrayList<String>();
            DateTime day = constraint.getStart();
            while (day <= constraint.getEnd()) {
                TimexProperty t = timex.clone();
                t.setYear(day.getYear());
                t.setMonth(day.getMonth());
                t.setDayOfMonth(day.getDayOfMonth());
                result.addAll(TimexRangeResolver.resolveDefiniteAgainstConstraint(t, constraint));
                day = day.addDays(1);
            }

            return result;
        }

        return new ArrayList<String>();
    }

    private static List<String> resolveByTimeConstraints(List<String> candidates,
            List<TimexProperty> timexConstrainst) {
        List<Time> times = timexConstrainst.stream().filter(timex -> {
            return timex.getTypes().contains(Constants.TimexTypes.TIME);
        }).map(timex -> {
            return TimexHelpers.timeFromTimex(timex);
        }).collect(Collectors.toList());

        if (times.isEmpty()) {
            return candidates;
        }

        List<String> resolution = new ArrayList<String>();
        for (TimexProperty timex : candidates.stream().map(t -> new TimexProperty(t)).collect(Collectors.toList())) {
            if (timex.getTypes().contains(Constants.TimexTypes.DATE) && !timex.getTypes().contains(Constants.TimexTypes.TIME))) {
                for (Time time : times) {
                    timex.setHour(time.getHour());
                    timex.setMinute(time.getMinute());
                    timex.setSecond(time.getSecond());
                    resolution.add(timex.getTimexValue());
                }
            } else {
                resolution.add(timex.getTimexValue());
            }
        }

        return TimexRangeResolver.removeDuplicates(resolution);
    }
}

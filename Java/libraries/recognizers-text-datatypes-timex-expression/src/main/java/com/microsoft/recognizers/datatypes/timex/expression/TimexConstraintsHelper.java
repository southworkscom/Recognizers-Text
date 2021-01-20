// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.recognizers.datatypes.timex.expression;

import java.util.ArrayList;
import java.util.List;

public class TimexConstraintsHelper {
    public static List<TimeRange> collapse(List<TimeRange> ranges) {
        List<TimeRange> r = ranges;

        while (TimexConstraintsHelper.innerCollapse(r)) {

        }

        r.sort((a, b) -> a.getStart().getTime() - b.getStart().getTime());

        return r;
    }

    public static List<DateRange> collapse(List<DateRange> ranges) {
        List<DateRange> r = ranges;

        while (TimexConstraintsHelper.innerCollapse(r)) {

        }

        r.sort((a, b) -> DateObject.compare(a.getStart(), b.getStart()));

        return r;
    }

    public static Boolean isOverlapping(TimeRange r1, TimeRange r2) {
        return (r1.getEnd().getTime() > r2.getStart().getTime() && r1.getStart().getTime() <= r2.getStart().getTime())
                || (r1.getStart().getTime() < r2.getEnd().getTime()
                        && r1.getStart().getTime() >= r2.getStart().getTime());
    }

    private static TimeRange collapseOverlapping(TimeRange r1, TimeRange r2) {
        return new TimeRange() {
            {
                setStart(new Time(Math.max(r1.getStart().getTime(), r2.getStart().getTime())));
                setEnd(new Time(Math.min(r1.getEnd().getTime(), r2.getEnd().getTime())));
            }
        };
    }

    private static Boolean innerCollapse(List<TimeRange> ranges) {
        if (ranges.size() == 1) {
            return false;
        }

        ArrayList<TimeRange> rangesList = (ArrayList<TimeRange>) ranges;
        for (int i = 0; i < ranges.size(); i++) {
            TimeRange r1 = ranges.get(i);
            for (int j = i + 1; j < ranges.size(); j++) {
                TimeRange r2 = ranges.get(j);
                if (TimexConstraintsHelper.isOverlapping(r1, r2)) {
                    ranges.subList(i, 1).clear();
                    ranges.subList(j - 1, 1).clear();
                    ranges.add(TimexConstraintsHelper.collapseOverlapping(r1, r2));
                    return true;
                }
            }
        }

        return false;
    }

    private static Boolean isOverlapping(DateRange r1, DateRange r2) {
        return (r1.getEnd() > r2.getStart() && r1.getStart() <= r2.getStart()
                || (r1.getStart() < r2.getEnd() && r1.getStart() >= r2.getStart()));
    }

    private static DateRange collapseOverlapping(DateRange r1, DateRange r2) {
        return new DateRange() {
            {
                setStart(r1.getStart() > r2.getStart() ? r1.getStart() : r2.getStart());
                setEnd(r1.getEnd() < r2.getEnd() ? r1.getEnd() : r2.getEnd());
            }
        };
    }

    private static Boolean innerCollapse(List<DateRange> ranges) {
        if (ranges.size() == 1) {
            return false;
        }

        for (int i = 0; i < ranges.size(); i++) {
            DateRange r1 = ranges.get(i);
            for (int j = i + 1; j < ranges.size(); j++) {
                DateRange r2 = ranges.get(j);
                if (TimexConstraintsHelper.isOverlapping(r1, r2)) {
                    ranges.subList(i, 1).clear();
                    ranges.subList(j - 1, i).clear();
                    ranges.add(TimexConstraintsHelper.collapseOverlapping(r1, r2));
                    return true;
                }
            }
        }

        return false;
    }
}

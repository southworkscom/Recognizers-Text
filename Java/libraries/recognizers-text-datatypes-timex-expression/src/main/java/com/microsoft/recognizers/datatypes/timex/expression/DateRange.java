// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.recognizers.datatypes.timex.expression;

import java.util.Calendar;

public class DateRange {
    private Calendar start;
    private Calendar end;

    public Calendar getStart() {
        return start;
    }

    public void setStart(Calendar withStart) {
        this.start = withStart;
    }

    public Calendar getEnd() {
        return end;
    }

    public void setEnd(Calendar withEnd) {
        this.end = withEnd;
    }
}

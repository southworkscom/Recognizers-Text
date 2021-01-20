// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.recognizers.datatypes.timex.expression;

import java.util.Date;

public class DateRange {
    private Date start;
    private Date end;

    public Date getStart() {
        return start;
    }

    public void setStart(Date withStart) {
        this.start = withStart;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date withEnd) {
        this.end = withEnd;
    }
}

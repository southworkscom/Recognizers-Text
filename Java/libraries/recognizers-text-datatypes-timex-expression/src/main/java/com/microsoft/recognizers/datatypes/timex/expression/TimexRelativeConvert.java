// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.recognizers.datatypes.timex.expression;

import com.microsoft.recognizers.datatypes.timex.expression.english.TimexRelativeConvertEnglish;

import java.util.Calendar;

public class TimexRelativeConvert {
    public static String convertTimexToStringRelative(TimexProperty timex, Calendar referenceDate) {
        return TimexRelativeConvertEnglish.convertTimexToStringRelative(timex, referenceDate);
    }
}

// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.recognizers.datatypes.timex.expression;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class TimexRegex {
    private static final String DATE_TIME_COLLECTION_NAME = "datetime";
    private static final String DATE_COLLECTION_NAME = "date";
    private static final String TIME_COLLECTION_NAME = "time";
    private static final String PERIOD_COLLECTION_NAME = "period";

    private static Map<String, Regex[]> timexRegex = new HashMap<String, Regex[]>() {
        {
            put(DATE_TIME_COLLECTION_NAME, {
                // date
                new Regex(@"^(?<year>\d\d\d\d)-(?<month>\d\d)-(?<dayOfMonth>\d\d)"),
                new Regex(@"^XXXX-WXX-(?<dayOfWeek>\d)"),
                new Regex(@"^XXXX-(?<month>\d\d)-(?<dayOfMonth>\d\d)"),

                // daterange
                new Regex(@"^(?<year>\d\d\d\d)"),
                new Regex(@"^(?<year>\d\d\d\d)-(?<month>\d\d)"),
                new Regex(@"^(?<season>SP|SU|FA|WI)"),
                new Regex(@"^(?<year>\d\d\d\d)-(?<season>SP|SU|FA|WI)"),
                new Regex(@"^(?<year>\d\d\d\d)-W(?<weekOfYear>\d\d)"),
                new Regex(@"^(?<year>\d\d\d\d)-W(?<weekOfYear>\d\d)-(?<weekend>WE)"),
                new Regex(@"^XXXX-(?<month>\d\d)"),
                new Regex(@"^XXXX-(?<month>\d\d)-W(?<weekOfMonth>\d\d)"),
                new Regex(@"^XXXX-(?<month>\d\d)-WXX-(?<weekOfMonth>\d{1,2})"),
                new Regex(@"^XXXX-(?<month>\d\d)-WXX-(?<weekOfMonth>\d)-(?<dayOfWeek>\d)"),
            });
            put(TIME_COLLECTION_NAME, {
                // time
                new Regex(@"T(?<hour>\d\d)Z?$"),
                new Regex(@"T(?<hour>\d\d):(?<minute>\d\d)Z?$"),
                new Regex(@"T(?<hour>\d\d):(?<minute>\d\d):(?<second>\d\d)Z?$"),

                // timerange
                new Regex(@"^T(?<partOfDay>DT|NI|MO|AF|EV)$"),
            });
            put(PERIOD_COLLECTION_NAME, {
                new Regex(@"^P(?<amount>\d*\.?\d+)(?<dateUnit>Y|M|W|D)$"),
                new Regex(@"^PT(?<amount>\d*\.?\d+)(?<timeUnit>H|M|S)$"),
            });
        }
    }

    private static Boolean extract(String name, String timex, Map<String, String> result) {
        String lowerName = name.toLowerCase();
        String[] nameGroup = new String[lowerName == DATE_TIME_COLLECTION_NAME ? 2 : 1];

        if (lowerName == DATE_TIME_COLLECTION_NAME) {
            nameGroup[0] = DATE_COLLECTION_NAME;
            nameGroup[1] = TIME_COLLECTION_NAME;
        } else {
            nameGroup[0] = lowerName;
        }

        Boolean anyTrue = false;
        for (String nameItem : nameGroup) {
            for (Regex[] entry : timexRegex.get(nameItem)) {
                if (TimexRegex.tryExtract(entry, timex, result)) {
                    anyTrue = true;
                }
            }
        }

        return anyTrue;
    }

    private static Boolean tryExtract(Regex regex, String timex, Map<String, String> result) {
        Regex regexResult = regex.match(timex);
        if (!regexResult.success) {
            return false;
        }

        for (String groupName : regex.getGroupNames()) {
            result[groupName] = regex.getGroups().get(groupName);
        }

        return true;
    }
}

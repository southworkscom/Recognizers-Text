package com.microsoft.recognizers.text.datetime.parsers.config;

public class MatchedTimeRangeResult {
    private final boolean matched;
    private final String timeStr;
    private final int beginHour;
    private final int endHour;
    private final int endMin;

    public MatchedTimeRangeResult(boolean matched, String timeStr, int beginHour, int endHour, int endMin) {
        this.matched = matched;
        this.timeStr = timeStr;
        this.beginHour = beginHour;
        this.endHour = endHour;
        this.endMin = endMin;
    }
}

package com.microsoft.recognizers.text.datetime.parsers.config;

public class AdjustResult {
    private final Integer hour;
    private final Integer minute;
    private final boolean hasAm;
    private final boolean hasPm;

    public AdjustResult(Integer hour, Integer minute, boolean hasAm, boolean hasPm) {
        this.hour = hour;
        this.minute = minute;
        this.hasAm = hasAm;
        this.hasPm = hasPm;
    }
}

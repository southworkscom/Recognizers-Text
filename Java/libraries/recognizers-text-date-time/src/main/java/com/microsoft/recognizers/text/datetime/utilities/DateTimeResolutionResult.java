package com.microsoft.recognizers.text.datetime.utilities;

import com.google.common.collect.ImmutableMap;

import java.util.List;

public class DateTimeResolutionResult {

    private Boolean success;
    private String timex;
    private Boolean isLunar;
    private String mod;
    private String comment;

    private ImmutableMap<String,String> futureResolution;
    private ImmutableMap<String,String> pastResolution;

    private Object futureValue;
    private Object pastValue;

    private List<Object> subDateTimeEntities;

    private TimeZoneResolutionResult timeZoneResolution;

    public DateTimeResolutionResult() {

    }

    public Boolean getSuccess() { return this.success; }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getTimex() { return this.timex; }

    public void setTimex(String timex) {
        this.timex = timex;
    }

    public Boolean getIsLunar() { return this.isLunar; }

    public void setIsLunar(Boolean isLunar) {
        this.isLunar = isLunar;
    }

    public String getMod() { return this.mod; }

    public String getComment() { return this.comment; }

    public ImmutableMap<String, String> getFutureResolution() { return this.futureResolution; }

    public ImmutableMap<String, String> getPastResolution() { return this.pastResolution; }

    public Object getFutureValue() { return this.futureValue; }

    public void setFutureValue(Object futureValue) {
        this.futureValue = futureValue;
    }

    public Object getPastValue() { return this.pastValue; }

    public void setPastValue(Object pastValue) {
        this.pastValue = futureValue;
    }

    public List<Object> getSubDateTimeEntities() { return this.subDateTimeEntities; }

    public TimeZoneResolutionResult getTimeZoneResolution() { return this.timeZoneResolution; }
}

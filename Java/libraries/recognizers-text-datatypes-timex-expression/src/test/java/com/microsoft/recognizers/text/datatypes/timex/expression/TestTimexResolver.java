// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.recognizers.text.datatypes.timex.expression;

import com.microsoft.recognizers.datatypes.timex.expression.Resolution;
import com.microsoft.recognizers.datatypes.timex.expression.TimexResolver;
import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.TimeZone;

public class TestTimexResolver {

    @Test
    public void dataTypesResolverDateDefinite()
    {
        LocalDateTime today = LocalDateTime.of(2017, 9, 26, 15, 30, 0);
        Resolution resolution = TimexResolver.resolve(new String[]{"2017-09-28"}, today);
        Assert.assertEquals(1, resolution.getValues().size());

        Assert.assertEquals("2017-09-28", resolution.getValues().get(0).getTimex());
        Assert.assertEquals("date", resolution.getValues().get(0).getType());
        Assert.assertEquals("2017-09-28", resolution.getValues().get(0).getValue());
        Assert.assertNull(resolution.getValues().get(0).getStart());
        Assert.assertNull(resolution.getValues().get(0).getEnd());
    }

    @Test
    public void dataTypesResolverDateSaturday()
    {
        LocalDateTime today = LocalDateTime.of(2017, 9, 26, 15, 30, 0);
        Resolution resolution = TimexResolver.resolve(new String[] { "XXXX-WXX-6" }, today);
        Assert.assertEquals(2, resolution.getValues().size());

        Assert.assertEquals("XXXX-WXX-6", resolution.getValues().get(0).getTimex());
        Assert.assertEquals("date", resolution.getValues().get(0).getType());
        Assert.assertEquals("2017-09-23", resolution.getValues().get(0).getValue());
        Assert.assertNull(resolution.getValues().get(0).getStart());
        Assert.assertNull(resolution.getValues().get(0).getEnd());

        Assert.assertEquals("XXXX-WXX-6", resolution.getValues().get(1).getTimex());
        Assert.assertEquals("date", resolution.getValues().get(1).getType());
        Assert.assertEquals("2017-09-30", resolution.getValues().get(1).getValue());
        Assert.assertNull(resolution.getValues().get(1).getStart());
        Assert.assertNull(resolution.getValues().get(1).getEnd());
    }

    @Test
    public void dataTypesResolverDateSunday()
    {
        LocalDateTime today = LocalDateTime.of(2019, 4, 23, 15, 30, 0);
        Resolution resolution = TimexResolver.resolve(new String[] { "XXXX-WXX-7" }, today);
        Assert.assertEquals(2, resolution.getValues().size());

        Assert.assertEquals("XXXX-WXX-7", resolution.getValues().get(0).getTimex());
        Assert.assertEquals("date", resolution.getValues().get(0).getType());
        Assert.assertEquals("2019-04-21", resolution.getValues().get(0).getValue());
        Assert.assertNull(resolution.getValues().get(0).getStart());
        Assert.assertNull(resolution.getValues().get(0).getEnd());

        Assert.assertEquals("XXXX-WXX-7", resolution.getValues().get(1).getTimex());
        Assert.assertEquals("date", resolution.getValues().get(1).getType());
        Assert.assertEquals("2019-04-28", resolution.getValues().get(1).getValue());
        Assert.assertNull(resolution.getValues().get(1).getStart());
        Assert.assertNull(resolution.getValues().get(1).getEnd());
    }

    @Test
    public void dataTypesResolverDateTimeWednesday4()
    {
        LocalDateTime today = LocalDateTime.of(2017, 9, 28, 15, 30, 0);
        Resolution resolution = TimexResolver.resolve(new String[] { "XXXX-WXX-3T04", "XXXX-WXX-3T16" }, today);
        Assert.assertEquals(4, resolution.getValues().size());

        Assert.assertEquals("XXXX-WXX-3T04", resolution.getValues().get(0).getTimex());
        Assert.assertEquals("datetime", resolution.getValues().get(0).getType());
        Assert.assertEquals("2017-09-27 04:00:00", resolution.getValues().get(0).getValue());
        Assert.assertNull(resolution.getValues().get(0).getStart());
        Assert.assertNull(resolution.getValues().get(0).getEnd());

        Assert.assertEquals("XXXX-WXX-3T04", resolution.getValues().get(1).getTimex());
        Assert.assertEquals("datetime", resolution.getValues().get(1).getType());
        Assert.assertEquals("2017-10-04 04:00:00", resolution.getValues().get(1).getValue());
        Assert.assertNull(resolution.getValues().get(1).getStart());
        Assert.assertNull(resolution.getValues().get(1).getEnd());

        Assert.assertEquals("XXXX-WXX-3T16", resolution.getValues().get(2).getTimex());
        Assert.assertEquals("datetime", resolution.getValues().get(2).getType());
        Assert.assertEquals("2017-09-27 16:00:00", resolution.getValues().get(2).getValue());
        Assert.assertNull(resolution.getValues().get(2).getStart());
        Assert.assertNull(resolution.getValues().get(2).getEnd());

        Assert.assertEquals("XXXX-WXX-3T16", resolution.getValues().get(3).getTimex());
        Assert.assertEquals("datetime", resolution.getValues().get(3).getType());
        Assert.assertEquals("2017-10-04 16:00:00", resolution.getValues().get(3).getValue());
        Assert.assertNull(resolution.getValues().get(3).getStart());
        Assert.assertNull(resolution.getValues().get(3).getEnd());
    }

    @Test
    public void dataTypesResolverDateTimeWednesday4am()
    {
        LocalDateTime today = LocalDateTime.of(2017, 9, 28, 15, 30, 0);
        Resolution resolution = TimexResolver.resolve(new String[] { "XXXX-WXX-3T04" }, today);
        Assert.assertEquals(2, resolution.getValues().size());

        Assert.assertEquals("XXXX-WXX-3T04", resolution.getValues().get(0).getTimex());
        Assert.assertEquals("datetime", resolution.getValues().get(0).getType());
        Assert.assertEquals("2017-09-27 04:00:00", resolution.getValues().get(0).getValue());
        Assert.assertNull(resolution.getValues().get(0).getStart());
        Assert.assertNull(resolution.getValues().get(0).getEnd());

        Assert.assertEquals("XXXX-WXX-3T04", resolution.getValues().get(1).getTimex());
        Assert.assertEquals("datetime", resolution.getValues().get(1).getType());
        Assert.assertEquals("2017-10-04 04:00:00", resolution.getValues().get(1).getValue());
        Assert.assertNull(resolution.getValues().get(1).getStart());
        Assert.assertNull(resolution.getValues().get(1).getEnd());
    }

    @Test
    public void dataTypesResolverDateTimeNextWednesday4am()
    {
        LocalDateTime today = LocalDateTime.of(2017, 9, 7,0,0);
        Resolution resolution = TimexResolver.resolve(new String[] { "2017-10-11T04" }, today);
        Assert.assertEquals(1, resolution.getValues().size());

        Assert.assertEquals("2017-10-11T04", resolution.getValues().get(0).getTimex());
        Assert.assertEquals("datetime", resolution.getValues().get(0).getType());
        Assert.assertEquals("2017-10-11 04:00:00", resolution.getValues().get(0).getValue());
        Assert.assertNull(resolution.getValues().get(0).getStart());
        Assert.assertNull(resolution.getValues().get(0).getEnd());
    }

    @Test
    public void dataTypesResolverDuration2years()
    {
        LocalDateTime today = LocalDateTime.now();
        Resolution resolution = TimexResolver.resolve(new String[] { "P2Y" }, today);
        Assert.assertEquals(1, resolution.getValues().size());

        Assert.assertEquals("P2Y", resolution.getValues().get(0).getTimex());
        Assert.assertEquals("duration", resolution.getValues().get(0).getType());
        Assert.assertEquals("63072000", resolution.getValues().get(0).getValue());
        Assert.assertNull(resolution.getValues().get(0).getStart());
        Assert.assertNull(resolution.getValues().get(0).getEnd());
    }

    @Test
    public void dataTypesResolverDuration6months()
    {
        LocalDateTime today = LocalDateTime.now();
        Resolution resolution = TimexResolver.resolve(new String[] { "P6M" }, today);
        Assert.assertEquals(1, resolution.getValues().size());

        Assert.assertEquals("P6M", resolution.getValues().get(0).getTimex());
        Assert.assertEquals("duration", resolution.getValues().get(0).getType());
        Assert.assertEquals("15552000", resolution.getValues().get(0).getValue());
        Assert.assertNull(resolution.getValues().get(0).getStart());
        Assert.assertNull(resolution.getValues().get(0).getEnd());
    }

    @Test
    public void dataTypesResolverDuration3weeks()
    {
        LocalDateTime today = LocalDateTime.now();
        Resolution resolution = TimexResolver.resolve(new String[] { "P3W" }, today);
        Assert.assertEquals(1, resolution.getValues().size());

        Assert.assertEquals("P3W", resolution.getValues().get(0).getTimex());
        Assert.assertEquals("duration", resolution.getValues().get(0).getType());
        Assert.assertEquals("1814400", resolution.getValues().get(0).getValue());
        Assert.assertNull(resolution.getValues().get(0).getStart());
        Assert.assertNull(resolution.getValues().get(0).getEnd());
    }

    @Test
    public void dataTypesResolverDuration5days()
    {
        LocalDateTime today = LocalDateTime.now();
        Resolution resolution = TimexResolver.resolve(new String[] { "P5D" }, today);
        Assert.assertEquals(1, resolution.getValues().size());

        Assert.assertEquals("P5D", resolution.getValues().get(0).getTimex());
        Assert.assertEquals("duration", resolution.getValues().get(0).getType());
        Assert.assertEquals("432000", resolution.getValues().get(0).getValue());
        Assert.assertNull(resolution.getValues().get(0).getStart());
        Assert.assertNull(resolution.getValues().get(0).getEnd());
    }

    @Test
    public void dataTypesResolverDuration8hours()
    {
        LocalDateTime today = LocalDateTime.now();
        Resolution resolution = TimexResolver.resolve(new String[] { "PT8H" }, today);
        Assert.assertEquals(1, resolution.getValues().size());

        Assert.assertEquals("PT8H", resolution.getValues().get(0).getTimex());
        Assert.assertEquals("duration", resolution.getValues().get(0).getType());
        Assert.assertEquals("28800", resolution.getValues().get(0).getValue());
        Assert.assertNull(resolution.getValues().get(0).getStart());
        Assert.assertNull(resolution.getValues().get(0).getEnd());
    }

    @Test
    public void dataTypesResolverDuration15minutes()
    {
        LocalDateTime today = LocalDateTime.now();
        Resolution resolution = TimexResolver.resolve(new String[] { "PT15M" }, today);
        Assert.assertEquals(1, resolution.getValues().size());

        Assert.assertEquals("PT15M", resolution.getValues().get(0).getTimex());
        Assert.assertEquals("duration", resolution.getValues().get(0).getType());
        Assert.assertEquals("900", resolution.getValues().get(0).getValue());
        Assert.assertNull(resolution.getValues().get(0).getStart());
        Assert.assertNull(resolution.getValues().get(0).getEnd());
    }

    @Test
    public void dataTypesResolverDuration10seconds()
    {
        LocalDateTime today = LocalDateTime.now();
        Resolution resolution = TimexResolver.resolve(new String[] { "PT10S" }, today);
        Assert.assertEquals(1, resolution.getValues().size());

        Assert.assertEquals("PT10S", resolution.getValues().get(0).getTimex());
        Assert.assertEquals("duration", resolution.getValues().get(0).getType());
        Assert.assertEquals("10", resolution.getValues().get(0).getValue());
        Assert.assertNull(resolution.getValues().get(0).getStart());
        Assert.assertNull(resolution.getValues().get(0).getEnd());
    }

    @Test
    public void dataTypesResolverDateRangeSeptember()
    {
        LocalDateTime today = LocalDateTime.of(2017, 9, 28,0,0);
        Resolution resolution = TimexResolver.resolve(new String[] { "XXXX-09" }, today);
        Assert.assertEquals(2, resolution.getValues().size());

        Assert.assertEquals("XXXX-09", resolution.getValues().get(0).getTimex());
        Assert.assertEquals("daterange", resolution.getValues().get(0).getType());
        Assert.assertEquals("2016-09-01", resolution.getValues().get(0).getStart());
        Assert.assertEquals("2016-10-01", resolution.getValues().get(0).getEnd());
        Assert.assertNull(resolution.getValues().get(0).getValue());

        Assert.assertEquals("XXXX-09", resolution.getValues().get(1).getTimex());
        Assert.assertEquals("daterange", resolution.getValues().get(1).getType());
        Assert.assertEquals("2017-09-01", resolution.getValues().get(1).getStart());
        Assert.assertEquals("2017-10-01", resolution.getValues().get(1).getEnd());
        Assert.assertNull(resolution.getValues().get(1).getValue());
    }

    @Test
    public void dataTypesResolverDateRangeWinter()
    {
        LocalDateTime today = LocalDateTime.now();
        Resolution resolution = TimexResolver.resolve(new String[] { "WI" }, today);
        Assert.assertEquals(1, resolution.getValues().size());

        Assert.assertEquals("WI", resolution.getValues().get(0).getTimex());
        Assert.assertEquals("daterange", resolution.getValues().get(0).getType());
        Assert.assertEquals("not resolved", resolution.getValues().get(0).getValue());
        Assert.assertNull(resolution.getValues().get(0).getStart());
        Assert.assertNull(resolution.getValues().get(0).getEnd());
    }

    @Test
    public void dataTypesResolverDateRangeLast_Week()
    {
        LocalDateTime today = LocalDateTime.of(2019, 4, 30,0,0);
        Resolution resolution = TimexResolver.resolve(new String[] { "2019-W17" }, today);
        Assert.assertEquals(1, resolution.getValues().size());

        Assert.assertEquals("2019-W17", resolution.getValues().get(0).getTimex());
        Assert.assertEquals("daterange", resolution.getValues().get(0).getType());
        Assert.assertEquals("2019-04-22", resolution.getValues().get(0).getStart());
        Assert.assertEquals("2019-04-29", resolution.getValues().get(0).getEnd());
    }

    @Test
    public void dataTypesResolverDateRangeLastMonth()
    {
        LocalDateTime today = LocalDateTime.of(2019, 4, 30,0,0);
        Resolution resolution = TimexResolver.resolve(new String[] { "2019-03" }, today);
        Assert.assertEquals(1, resolution.getValues().size());

        Assert.assertEquals("2019-03", resolution.getValues().get(0).getTimex());
        Assert.assertEquals("daterange", resolution.getValues().get(0).getType());
        Assert.assertEquals("2019-03-01", resolution.getValues().get(0).getStart());
        Assert.assertEquals("2019-04-01", resolution.getValues().get(0).getEnd());
    }

    @Test
    public void dataTypesResolverDateRangeLastYear()
    {
        LocalDateTime today = LocalDateTime.of(2019, 4, 30,0,0);
        Resolution resolution = TimexResolver.resolve(new String[] { "2018" }, today);
        Assert.assertEquals(1, resolution.getValues().size());

        Assert.assertEquals("2018", resolution.getValues().get(0).getTimex());
        Assert.assertEquals("daterange", resolution.getValues().get(0).getType());
        Assert.assertEquals("2018-01-01", resolution.getValues().get(0).getStart());
        Assert.assertEquals("2019-01-01", resolution.getValues().get(0).getEnd());
    }

    @Test
    public void dataTypesResolverDateRangeLastThreeWeeks()
    {
        LocalDateTime today = LocalDateTime.of(2019, 4, 30,0,0);
        Resolution resolution = TimexResolver.resolve(new String[] { "(2019-04-10,2019-05-01,P3W)" }, today);
        Assert.assertEquals(1, resolution.getValues().size());

        Assert.assertEquals("(2019-04-10,2019-05-01,P3W)", resolution.getValues().get(0).getTimex());
        Assert.assertEquals("daterange", resolution.getValues().get(0).getType());
        Assert.assertEquals("2019-04-10", resolution.getValues().get(0).getStart());
        Assert.assertEquals("2019-05-01", resolution.getValues().get(0).getEnd());
    }

    @Test
    public void dataTypesResolverTimeRange4amto8pm()
    {
        LocalDateTime today = LocalDateTime.now();
        Resolution resolution = TimexResolver.resolve(new String[] { "(T04,T20,PT16H)" }, today);
        Assert.assertEquals(1, resolution.getValues().size());

        Assert.assertEquals("(T04,T20,PT16H)", resolution.getValues().get(0).getTimex());
        Assert.assertEquals("timerange", resolution.getValues().get(0).getType());
        Assert.assertEquals("04:00:00", resolution.getValues().get(0).getStart());
        Assert.assertEquals("20:00:00", resolution.getValues().get(0).getEnd());
        Assert.assertNull(resolution.getValues().get(0).getValue());
    }

    @Test
    public void dataTypesResolverTimeRangeMorning()
    {
        LocalDateTime today = LocalDateTime.now();
        Resolution resolution = TimexResolver.resolve(new String[] { "TMO" }, today);
        Assert.assertEquals(1, resolution.getValues().size());

        Assert.assertEquals("TMO", resolution.getValues().get(0).getTimex());
        Assert.assertEquals("timerange", resolution.getValues().get(0).getType());
        Assert.assertEquals("08:00:00", resolution.getValues().get(0).getStart());
        Assert.assertEquals("12:00:00", resolution.getValues().get(0).getEnd());
        Assert.assertNull(resolution.getValues().get(0).getValue());
    }

    @Test
    public void dataTypesResolverTimeRangeAfternoon()
    {
        LocalDateTime today = LocalDateTime.now();
        Resolution resolution = TimexResolver.resolve(new String[] { "TAF" }, today);
        Assert.assertEquals(1, resolution.getValues().size());

        Assert.assertEquals("TAF", resolution.getValues().get(0).getTimex());
        Assert.assertEquals("timerange", resolution.getValues().get(0).getType());
        Assert.assertEquals("12:00:00", resolution.getValues().get(0).getStart());
        Assert.assertEquals("16:00:00", resolution.getValues().get(0).getEnd());
        Assert.assertNull(resolution.getValues().get(0).getValue());
    }

    @Test
    public void dataTypesResolverTimeRangeEvening()
    {
        LocalDateTime today = LocalDateTime.now();
        Resolution resolution = TimexResolver.resolve(new String[] { "TEV" }, today);
        Assert.assertEquals(1, resolution.getValues().size());

        Assert.assertEquals("TEV", resolution.getValues().get(0).getTimex());
        Assert.assertEquals("timerange", resolution.getValues().get(0).getType());
        Assert.assertEquals("16:00:00", resolution.getValues().get(0).getStart());
        Assert.assertEquals("20:00:00", resolution.getValues().get(0).getEnd());
        Assert.assertNull(resolution.getValues().get(0).getValue());
    }

    @Test
    public void dataTypesResolverDateTimeRangeThisMorning()
    {
        LocalDateTime today = LocalDateTime.now();
        Resolution resolution = TimexResolver.resolve(new String[] { "2017-10-07TMO" }, today);
        Assert.assertEquals(1, resolution.getValues().size());

        Assert.assertEquals("2017-10-07TMO", resolution.getValues().get(0).getTimex());
        Assert.assertEquals("datetimerange", resolution.getValues().get(0).getType());
        Assert.assertEquals("2017-10-07 08:00:00", resolution.getValues().get(0).getStart());
        Assert.assertEquals("2017-10-07 12:00:00", resolution.getValues().get(0).getEnd());
        Assert.assertNull(resolution.getValues().get(0).getValue());
    }

    @Test
    public void dataTypesResolverDateTimeRangeTonight()
    {
        LocalDateTime today = LocalDateTime.now();
        Resolution resolution = TimexResolver.resolve(new String[] { "2018-03-18TNI" }, today);
        Assert.assertEquals(1, resolution.getValues().size());

        Assert.assertEquals("2018-03-18TNI", resolution.getValues().get(0).getTimex());
        Assert.assertEquals("datetimerange", resolution.getValues().get(0).getType());
        Assert.assertEquals("2018-03-18 20:00:00", resolution.getValues().get(0).getStart());
        Assert.assertEquals("2018-03-18 24:00:00", resolution.getValues().get(0).getEnd());
        Assert.assertNull(resolution.getValues().get(0).getValue());
    }

    @Test
    public void dataTypesResolverDateTimeRangeNextMonday4amToNextThursday3pm()
    {
        LocalDateTime today = LocalDateTime.now();
        Resolution resolution = TimexResolver.resolve(new String[] { "(2017-10-09T04,2017-10-12T15,PT83H)" }, today);
        Assert.assertEquals(1, resolution.getValues().size());

        Assert.assertEquals("(2017-10-09T04,2017-10-12T15,PT83H)", resolution.getValues().get(0).getTimex());
        Assert.assertEquals("datetimerange", resolution.getValues().get(0).getType());
        Assert.assertEquals("2017-10-09 04:00:00", resolution.getValues().get(0).getStart());
        Assert.assertEquals("2017-10-12 15:00:00", resolution.getValues().get(0).getEnd());
        Assert.assertNull(resolution.getValues().get(0).getValue());
    }

    @Test
    public void dataTypesResolverTime4am()
    {
        LocalDateTime today = LocalDateTime.now();
        Resolution resolution = TimexResolver.resolve(new String[] { "T04" }, today);
        Assert.assertEquals(1, resolution.getValues().size());

        Assert.assertEquals("T04", resolution.getValues().get(0).getTimex());
        Assert.assertEquals("time", resolution.getValues().get(0).getType());
        Assert.assertEquals("04:00:00", resolution.getValues().get(0).getValue());
        Assert.assertNull(resolution.getValues().get(0).getStart());
        Assert.assertNull(resolution.getValues().get(0).getEnd());
    }

    @Test
    public void dataTypesResolverTime4oclock()
    {
        LocalDateTime today = LocalDateTime.now();
        Resolution resolution = TimexResolver.resolve(new String[] { "T04", "T16" }, today);
        Assert.assertEquals(2, resolution.getValues().size());

        Assert.assertEquals("T04", resolution.getValues().get(0).getTimex());
        Assert.assertEquals("time", resolution.getValues().get(0).getType());
        Assert.assertEquals("04:00:00", resolution.getValues().get(0).getValue());
        Assert.assertNull(resolution.getValues().get(0).getStart());
        Assert.assertNull(resolution.getValues().get(0).getEnd());

        Assert.assertEquals("T16", resolution.getValues().get(1).getTimex());
        Assert.assertEquals("time", resolution.getValues().get(1).getType());
        Assert.assertEquals("16:00:00", resolution.getValues().get(1).getValue());
        Assert.assertNull(resolution.getValues().get(1).getStart());
        Assert.assertNull(resolution.getValues().get(1).getEnd());
    }

    @Test
    public void dataTypesResolverDateSecondWeekInAugust()
    {
        LocalDateTime today = LocalDateTime.of(2019, 11, 06,0,0);
        Resolution resolution = TimexResolver.resolve(new String[] { "XXXX-08-W02" }, today);
        Assert.assertEquals(2, resolution.getValues().size());

        Assert.assertEquals("XXXX-08-W02", resolution.getValues().get(0).getTimex());
        Assert.assertEquals("daterange", resolution.getValues().get(0).getType());
        Assert.assertEquals("2018-08-06", resolution.getValues().get(0).getStart());
        Assert.assertEquals("2018-08-13", resolution.getValues().get(0).getEnd());
        Assert.assertNull(resolution.getValues().get(0).getValue());

        Assert.assertEquals("XXXX-08-W02", resolution.getValues().get(1).getTimex());
        Assert.assertEquals("daterange", resolution.getValues().get(1).getType());
        Assert.assertEquals("2019-08-05", resolution.getValues().get(1).getStart());
        Assert.assertEquals("2019-08-12", resolution.getValues().get(1).getEnd());
        Assert.assertNull(resolution.getValues().get(1).getValue());
    }

    @Test
    public void dataTypesResolverDateTimeNov6at114525()
    {
        LocalDateTime today = LocalDateTime.of(2017, 9, 28, 15, 30, 0);
        Resolution resolution = TimexResolver.resolve(new String[] { "2019-11-06T11:45:25" }, today);
        Assert.assertEquals(1, resolution.getValues().size());

        Assert.assertEquals("2019-11-06T11:45:25", resolution.getValues().get(0).getTimex());
        Assert.assertEquals("datetime", resolution.getValues().get(0).getType());
        Assert.assertEquals("2019-11-06 11:45:25", resolution.getValues().get(0).getValue());
        Assert.assertNull(resolution.getValues().get(0).getStart());
        Assert.assertNull(resolution.getValues().get(0).getEnd());
    }

    @Test
    public void dataTypesResolverDateTimeNov6at114525UTC()
    {
        LocalDateTime today = LocalDateTime.of(2017, 9, 28, 15, 30, 0);
        Resolution resolution = TimexResolver.resolve(new String[] { "2019-11-06T11:45:25Z" }, today);
        Assert.assertEquals(1, resolution.getValues().size());

        Assert.assertEquals("2019-11-06T11:45:25", resolution.getValues().get(0).getTimex());
        Assert.assertEquals("datetime", resolution.getValues().get(0).getType());
        Assert.assertEquals("2019-11-06 11:45:25", resolution.getValues().get(0).getValue());
        Assert.assertNull(resolution.getValues().get(0).getStart());
        Assert.assertNull(resolution.getValues().get(0).getEnd());
    }

    @Test
    public void dataTypesResolverDateTimeTuesAt12PM()
    {
        LocalDateTime today = LocalDateTime.of(2019, 12, 05,0,0);
        Resolution resolution = TimexResolver.resolve(new String[] { "XXXX-WXX-2T12" }, today);
        Assert.assertEquals(2, resolution.getValues().size());

        Assert.assertEquals("XXXX-WXX-2T12", resolution.getValues().get(0).getTimex());
        Assert.assertEquals("datetime", resolution.getValues().get(0).getType());
        Assert.assertEquals("2019-12-03 12:00:00", resolution.getValues().get(0).getValue());
        Assert.assertNull(resolution.getValues().get(0).getStart());
        Assert.assertNull(resolution.getValues().get(0).getEnd());

        Assert.assertEquals("XXXX-WXX-2T12", resolution.getValues().get(1).getTimex());
        Assert.assertEquals("datetime", resolution.getValues().get(1).getType());
        Assert.assertEquals("2019-12-10 12:00:00", resolution.getValues().get(1).getValue());
        Assert.assertNull(resolution.getValues().get(1).getStart());
        Assert.assertNull(resolution.getValues().get(1).getEnd());
    }

    @Test
    public void dataTypesResolverDateTimeTuesAt12PMUtcInput()
    {
        LocalDateTime today = LocalDateTime.of(2019, 12, 05,0,0);
        Resolution resolution = TimexResolver.resolve(new String[] { "XXXX-WXX-2T12" }, today);
        Assert.assertEquals(2, resolution.getValues().size());

        LocalDateTime previousWeekUtc = LocalDateTime.of(2019, 12, 03, 12, 0, 0);
        previousWeekUtc.atZone(ZoneOffset.UTC);

        Assert.assertEquals("XXXX-WXX-2T12", resolution.getValues().get(0).getTimex());
        Assert.assertEquals("datetime", resolution.getValues().get(0).getType());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Assert.assertEquals(formatter.format(previousWeekUtc), resolution.getValues().get(0).getValue());

        Assert.assertNull(resolution.getValues().get(0).getStart());
        Assert.assertNull(resolution.getValues().get(0).getEnd());

        LocalDateTime nextWeekUtc = LocalDateTime.of(2019, 12, 10, 12, 0, 0);
        nextWeekUtc.atZone(ZoneOffset.UTC);

        Assert.assertEquals("XXXX-WXX-2T12", resolution.getValues().get(1).getTimex());
        Assert.assertEquals("datetime", resolution.getValues().get(1).getType());

        Assert.assertEquals(formatter.format(nextWeekUtc), resolution.getValues().get(1).getValue());

        Assert.assertNull(resolution.getValues().get(1).getStart());
        Assert.assertNull(resolution.getValues().get(1).getEnd());
    }

    @Test
    public void dataTypesResolverDateTime2021W01() // first day of the year is a Friday - week 1
    {
        LocalDateTime today = LocalDateTime.of(2021, 01, 05,0,0);
        today.atZone(ZoneOffset.UTC);
        Resolution resolution = TimexResolver.resolve(new String[] { "2021-W01" }, today);
        Assert.assertEquals(1, resolution.getValues().size());

        Assert.assertEquals("2021-W01", resolution.getValues().get(0).getTimex());
        Assert.assertEquals("daterange", resolution.getValues().get(0).getType());
        Assert.assertEquals("2021-01-04", resolution.getValues().get(0).getStart());
        Assert.assertEquals("2021-01-11", resolution.getValues().get(0).getEnd());
    }

    @Test
    public void dataTypesResolverDateTime2021W02() // first day of the year is a Friday - week 2
    {
        LocalDateTime today = LocalDateTime.of(2021, 01, 05,0,0);
        today.atZone(ZoneOffset.UTC);
        Resolution resolution = TimexResolver.resolve(new String[] { "2021-W02" }, today);
        Assert.assertEquals(1, resolution.getValues().size());

        Assert.assertEquals("2021-W02", resolution.getValues().get(0).getTimex());
        Assert.assertEquals("daterange", resolution.getValues().get(0).getType());
        Assert.assertEquals("2021-01-11", resolution.getValues().get(0).getStart());
        Assert.assertEquals("2021-01-18", resolution.getValues().get(0).getEnd());
    }

    @Test
    public void dataTypesResolverDateTime2020W53() // has a 53-week year
    {
        LocalDateTime today = LocalDateTime.of(2020, 12, 30,0,0);
        today.atZone(ZoneOffset.UTC);

        Resolution resolution = TimexResolver.resolve(new String[] { "2020-W53" }, today);
        Assert.assertEquals(1, resolution.getValues().size());

        Assert.assertEquals("2020-W53", resolution.getValues().get(0).getTimex());
        Assert.assertEquals("daterange", resolution.getValues().get(0).getType());
        Assert.assertEquals("2020-12-28", resolution.getValues().get(0).getStart());
        Assert.assertEquals("2021-01-04", resolution.getValues().get(0).getEnd());
    }

    @Test
    public void dataTypesResolverDateTime2024W01() // first day of the year is a Monday
    {
        LocalDateTime today = LocalDateTime.of(2024, 01, 01,0,0);
        today.atZone(ZoneOffset.UTC);
        Resolution resolution = TimexResolver.resolve(new String[] { "2024-W01" }, today);
        Assert.assertEquals(1, resolution.getValues().size());

        Assert.assertEquals("2024-W01", resolution.getValues().get(0).getTimex());
        Assert.assertEquals("daterange", resolution.getValues().get(0).getType());
        Assert.assertEquals("2024-01-01", resolution.getValues().get(0).getStart());
        Assert.assertEquals("2024-01-08", resolution.getValues().get(0).getEnd());
    }

    @Test
    public void dataTypesResolverDateTimeWeekend()
    {
        LocalDateTime today = LocalDateTime.of(2020, 1, 7,0,0);
        Resolution resolution = TimexResolver.resolve(new String[] { "2020-W02-WE" }, today);
        Assert.assertEquals(1, resolution.getValues().size());

        Assert.assertEquals("2020-W02-WE", resolution.getValues().get(0).getTimex());
        Assert.assertEquals("daterange", resolution.getValues().get(0).getType());
        Assert.assertEquals("2020-01-11", resolution.getValues().get(0).getStart());
        Assert.assertEquals("2020-01-13", resolution.getValues().get(0).getEnd());
        Assert.assertNull(resolution.getValues().get(0).getValue());
    }

    @Test
    public void dataTypesResolverMonthRangeDecember()
    {
        LocalDateTime today = LocalDateTime.of(2020, 3, 25,0,0);
        Resolution resolution = TimexResolver.resolve(new String[] { "XXXX-12" }, today);
        Assert.assertEquals(2, resolution.getValues().size());

        Assert.assertEquals("XXXX-12", resolution.getValues().get(0).getTimex());
        Assert.assertEquals("daterange", resolution.getValues().get(0).getType());
        Assert.assertEquals("2019-12-01", resolution.getValues().get(0).getStart());
        Assert.assertEquals("2020-01-01", resolution.getValues().get(0).getEnd());
        Assert.assertNull(resolution.getValues().get(0).getValue());

        Assert.assertEquals("XXXX-12", resolution.getValues().get(1).getTimex());
        Assert.assertEquals("daterange", resolution.getValues().get(1).getType());
        Assert.assertEquals("2020-12-01", resolution.getValues().get(1).getStart());
        Assert.assertEquals("2021-01-01", resolution.getValues().get(1).getEnd());
        Assert.assertNull(resolution.getValues().get(1).getValue());
    }
}
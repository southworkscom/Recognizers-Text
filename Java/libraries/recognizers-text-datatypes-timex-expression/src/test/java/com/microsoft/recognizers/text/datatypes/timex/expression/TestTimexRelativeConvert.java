// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.recognizers.text.datatypes.timex.expression;

import com.microsoft.recognizers.datatypes.timex.expression.TimexProperty;
import com.microsoft.recognizers.datatypes.timex.expression.TimexRelativeConvert;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

public class TestTimexRelativeConvert {
    @Test
    public void dataTypesRelativeConvertDateToday()
    {
        TimexProperty timex = new TimexProperty("2017-09-25");
        Calendar today = Calendar.getInstance();
        today.set(2017, 9, 25);
        Assert.assertEquals("today", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }

    @Test
    public void dataTypesRelativeConvertDateTomorrow()
    {
        TimexProperty timex = new TimexProperty("2017-09-23");
        Calendar today = Calendar.getInstance();
        today.set(2017, 9, 22);
        Assert.assertEquals("tomorrow", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }

    @Test
    public void dataTypesRelativeConvertDateTomorrowCrossYearMonthBoundary()
    {
        TimexProperty timex = new TimexProperty("2018-01-01");
        Calendar today = Calendar.getInstance();
        today.set(2017, 12, 31);
        Assert.assertEquals("tomorrow", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }

    @Test
    public void dataTypesRelativeConvertDateYesterday()
    {
        TimexProperty timex = new TimexProperty("2017-09-21");
        Calendar today = Calendar.getInstance();
        today.set(2017, 9, 22);
        Assert.assertEquals("yesterday", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }

    @Test
    public void dataTypesRelativeConvertYesterdayCrossYearMonthBoundary()
    {
        TimexProperty timex = new TimexProperty("2017-12-31");
        Calendar today = Calendar.getInstance();
        today.set(2018, 1, 1);
        Assert.assertEquals("yesterday", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }

    @Test
    public void dataTypesRelativeConvertDateThisWeek()
    {
        TimexProperty timex = new TimexProperty("2017-10-18");
        Calendar today = Calendar.getInstance();
        today.set(2017, 10, 16);
        Assert.assertEquals("this Wednesday", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }

    @Test
    public void dataTypesRelativeConvertDateThisWeekCrossYearMonthBoundary()
    {
        TimexProperty timex = new TimexProperty("2017-11-03");
        Calendar today = Calendar.getInstance();
        today.set(2017, 10, 31);
        Assert.assertEquals("this Friday", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }

    @Test
    public void dataTypesRelativeConvertDateNextWeek()
    {
        TimexProperty timex = new TimexProperty("2017-09-27");
        Calendar today = Calendar.getInstance();
        today.set(2017, 9, 22);
        Assert.assertEquals("next Wednesday", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }

    @Test
    public void dataTypesRelativeConvertDateNextWeekCrossYearMonthBoundary()
    {
        TimexProperty timex = new TimexProperty("2018-01-05");
        Calendar today = Calendar.getInstance();
        today.set(2017, 12, 28);
        Assert.assertEquals("next Friday", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }

    @Test
    public void dataTypesRelativeConvertDateLastWeek()
    {
        TimexProperty timex = new TimexProperty("2017-09-14");
        Calendar today = Calendar.getInstance();
        today.set(2017, 9, 22);
        Assert.assertEquals("last Thursday", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }

    @Test
    public void dataTypesRelativeConvertDateLastWeekCrossYearMonthBoundary()
    {
        TimexProperty timex = new TimexProperty("2017-12-25");
        Calendar today = Calendar.getInstance();
        today.set(2018, 1, 4);
        Assert.assertEquals("last Monday", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }

    @Test
    public void dataTypesRelativeConvertDateThisWeek2()
    {
        TimexProperty timex = new TimexProperty("2017-10-25");
        Calendar today = Calendar.getInstance();
        today.set(2017, 9, 9);
        Assert.assertEquals("25th October 2017", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }

    @Test
    public void dataTypesRelativeConvertDateNextWeek2()
    {
        TimexProperty timex = new TimexProperty("2017-10-04");
        Calendar today = Calendar.getInstance();
        today.set(2017, 9, 22);
        Assert.assertEquals("4th October 2017", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }

    @Test
    public void dataTypesRelativeConvertDateLastWeek2()
    {
        TimexProperty timex = new TimexProperty("2017-09-07");
        Calendar today = Calendar.getInstance();
        today.set(2017, 9, 22);
        Assert.assertEquals("7th September 2017", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }

    @Test
    public void dataTypesRelativeConvertDateTimeToday()
    {
        TimexProperty timex = new TimexProperty("2017-09-25T16:00:00");
        Calendar today = Calendar.getInstance();
        today.set(2017, 9, 25);
        Assert.assertEquals("today 4PM", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }

    @Test
    public void dataTypesRelativeConvertDateTimeTomorrow()
    {
        TimexProperty timex = new TimexProperty("2017-09-23T16:00:00");
        Calendar today = Calendar.getInstance();
        today.set(2017, 9, 22);
        Assert.assertEquals("tomorrow 4PM", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }

    @Test
    public void dataTypesRelativeConvertDateTimeYesterday()
    {
        TimexProperty timex = new TimexProperty("2017-09-21T16:00:00");
        Calendar today = Calendar.getInstance();
        today.set(2017, 9, 22);
        Assert.assertEquals("yesterday 4PM", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }

    @Test
    public void dataTypesRelativeConvertDateRangeThisWeek()
    {
        TimexProperty timex = new TimexProperty("2017-W40");
        Calendar today = Calendar.getInstance();
        today.set(2017, 9, 25);
        Assert.assertEquals("this week", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }

    @Test
    public void dataTypesRelativeConvertDateRangeNextWeek()
    {
        TimexProperty timex = new TimexProperty("2017-W41");
        Calendar today = Calendar.getInstance();
        today.set(2017, 9, 25);
        Assert.assertEquals("next week", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }

    @Test
    public void dataTypesRelativeConvertDateRangeLastWeek()
    {
        TimexProperty timex = new TimexProperty("2017-W39");
        Calendar today = Calendar.getInstance();
        today.set(2017, 9, 25);
        Assert.assertEquals("last week", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }

    @Test
    public void dataTypesRelativeConvertThisWeek2()
    {
        TimexProperty timex = new TimexProperty("2017-W41");
        Calendar today = Calendar.getInstance();
        today.set(2017, 10, 4);
        Assert.assertEquals("this week", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }

    @Test
    public void dataTypesRelativeConvertDateRangeNextWeek2()
    {
        TimexProperty timex = new TimexProperty("2017-W42");
        Calendar today = Calendar.getInstance();
        today.set(2017, 10, 4);
        Assert.assertEquals("next week", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }

    @Test
    public void dataTypesRelativeConvertDateRangeLastWeek2()
    {
        TimexProperty timex = new TimexProperty("2017-W40");
        Calendar today = Calendar.getInstance();
        today.set(2017, 10, 4);
        Assert.assertEquals("last week", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }

    @Test
    public void dataTypesRelativeConvertWeekendThisWeekend()
    {
        TimexProperty timex = new TimexProperty("2017-W40-WE");
        Calendar today = Calendar.getInstance();
        today.set(2017, 9, 25);
        Assert.assertEquals("this weekend", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }

    @Test
    public void dataTypesRelativeConvertWeekendNextWeekend()
    {
        TimexProperty timex = new TimexProperty("2017-W41-WE");
        Calendar today = Calendar.getInstance();
        today.set(2017, 9, 25);
        Assert.assertEquals("next weekend", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }

    @Test
    public void dataTypesRelativeConvertWeekendLastWeekend()
    {
        TimexProperty timex = new TimexProperty("2017-W39-WE");
        Calendar today = Calendar.getInstance();
        today.set(2017, 9, 25);
        Assert.assertEquals("last weekend", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }

    @Test
    public void dataTypesRelativeConvertMonthThisMonth()
    {
        TimexProperty timex = new TimexProperty("2017-09");
        Calendar today = Calendar.getInstance();
        today.set(2017, 9, 25);
        Assert.assertEquals("this month", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }

    @Test
    public void dataTypesRelativeConvertMonthNextMonth()
    {
        TimexProperty timex = new TimexProperty("2017-10");
        Calendar today = Calendar.getInstance();
        today.set(2017, 9, 25);
        Assert.assertEquals("next month", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }

    @Test
    public void dataTypesRelativeConvertMonthLastMonth()
    {
        TimexProperty timex = new TimexProperty("2017-08");
        Calendar today = Calendar.getInstance();
        today.set(2017, 9, 25);
        Assert.assertEquals("last month", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }

    @Test
    public void dataTypesRelativeConvertYearThisYear()
    {
        TimexProperty timex = new TimexProperty("2017");
        Calendar today = Calendar.getInstance();
        today.set(2017, 9, 25);
        Assert.assertEquals("this year", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }

    @Test
    public void dataTypesRelativeConvertYearNextYear()
    {
        TimexProperty timex = new TimexProperty("2018");
        Calendar today = Calendar.getInstance();
        today.set(2017, 9, 25);
        Assert.assertEquals("next year", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }

    @Test
    public void dataTypesRelativeConvertYearLastYear()
    {
        TimexProperty timex = new TimexProperty("2016");
        Calendar today = Calendar.getInstance();
        today.set(2017, 9, 25);
        Assert.assertEquals("last year", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }

    @Test
    public void dataTypesRelativeConvertSeasonThisSummer()
    {
        TimexProperty timex = new TimexProperty("2017-SU");
        Calendar today = Calendar.getInstance();
        today.set(2017, 9, 25);
        Assert.assertEquals("this summer", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }

    @Test
    public void dataTypesRelativeConvertSeasonNextSummer()
    {
        TimexProperty timex = new TimexProperty("2018-SU");
        Calendar today = Calendar.getInstance();
        today.set(2017, 9, 25);
        Assert.assertEquals("next summer", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }

    @Test
    public void dataTypesRelativeConvertSeasonLastSummer()
    {
        TimexProperty timex = new TimexProperty("2016-SU");
        Calendar today = Calendar.getInstance();
        today.set(2017, 9, 25);
        Assert.assertEquals("last summer", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }

    @Test
    public void dataTypesRelativeConvertPartOfDayThisEvening()
    {
        TimexProperty timex = new TimexProperty("2017-09-25TEV");
        Calendar today = Calendar.getInstance();
        today.set(2017, 9, 25);
        Assert.assertEquals("this evening", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }

    @Test
    public void dataTypesRelativeConvertPartOfDayTonight()
    {
        TimexProperty timex = new TimexProperty("2017-09-25TNI");
        Calendar today = Calendar.getInstance();
        today.set(2017, 9, 25);
        Assert.assertEquals("tonight", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }

    @Test
    public void dataTypesRelativeConvertPartOfDayTomorrowMorning()
    {
        TimexProperty timex = new TimexProperty("2017-09-26TMO");
        Calendar today = Calendar.getInstance();
        today.set(2017, 9, 25);
        Assert.assertEquals("tomorrow morning", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }

    @Test
    public void dataTypesRelativeConvertPartOfDayYesterdayAfternoon()
    {
        TimexProperty timex = new TimexProperty("2017-09-24TAF");
        Calendar today = Calendar.getInstance();
        today.set(2017, 9, 25);
        Assert.assertEquals("yesterday afternoon", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }

    @Test
    public void dataTypesRelativeConvertPartOfDayNextWednesdayEvening()
    {
        TimexProperty timex = new TimexProperty("2017-10-04TEV");
        Calendar today = Calendar.getInstance();
        today.set(2017, 9, 25);
        Assert.assertEquals("next Wednesday evening", TimexRelativeConvert.convertTimexToStringRelative(timex, today));
    }
}

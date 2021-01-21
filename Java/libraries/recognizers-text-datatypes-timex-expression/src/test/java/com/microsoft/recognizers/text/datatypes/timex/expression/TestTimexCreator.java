// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.recognizers.text.datatypes.timex.expression;

import com.microsoft.recognizers.datatypes.timex.expression.TimexCreator;
import com.microsoft.recognizers.datatypes.timex.expression.TimexDateHelpers;
import com.microsoft.recognizers.datatypes.timex.expression.TimexFormat;
import com.microsoft.recognizers.datatypes.timex.expression.TimexProperty;
import org.junit.Assert;
import org.junit.Test;

import java.time.DayOfWeek;
import java.util.Calendar;

public class TestTimexCreator {
    
    @Test
    public void dataTypesCreatorToday()
    {
        Calendar d = Calendar.getInstance();
        String expected = TimexFormat.format(new TimexProperty()
        {
            {
                setYear(d.get(Calendar.YEAR));
                setMonth(d.get(Calendar.MONTH));
                setDayOfMonth(d.get(Calendar.DATE));
            }
        });
        Assert.assertEquals(expected, TimexCreator.today(d));
    }

    @Test
    public void dataTypesCreatorTodayRelative()
    {
        Calendar d = Calendar.getInstance();
        d.set(2017, 10, 5);
        Assert.assertEquals("2017-10-05", TimexCreator.today(d));
    }

    @Test
    public void dataTypesCreatorTomorrow()
    {
        Calendar d = Calendar.getInstance();
        d.add(Calendar.DATE, 1);        
        String expected = TimexFormat.format(new TimexProperty() {
        {
            setYear(d.get(Calendar.YEAR));
            setMonth(d.get(Calendar.MONTH));
            setDayOfMonth(d.get(Calendar.DATE));
        }});
        Assert.assertEquals(expected, TimexCreator.tomorrow(d));
    }

    @Test
    public void dataTypesCreatorTomorrowRelative()
    {
        Calendar d = Calendar.getInstance();
        d.set(2017, 10 ,5);
        Assert.assertEquals("2017-10-06", TimexCreator.tomorrow(d));
    }

    @Test
    public void dataTypesCreatorYesterday()
    {
        Calendar d = Calendar.getInstance();
        d.add(Calendar.DATE,-1);
        String expected = TimexFormat.format(new TimexProperty()
        {
            {
                setYear(d.get(Calendar.YEAR));
                setMonth(d.get(Calendar.MONTH));
                setDayOfMonth(d.get(Calendar.DATE));
            }
        });
        Assert.assertEquals(expected, TimexCreator.yesterday(d));
    }

    @Test
    public void dataTypesCreatorYesterdayRelative()
    {
        Calendar d = Calendar.getInstance();
        d.set(2017, 10 ,5);
        Assert.assertEquals("2017-10-04", TimexCreator.yesterday(d));
    }

    @Test
    public void dataTypesCreatorWeekFromToday()
    {
        Calendar d = Calendar.getInstance();
        String expected = TimexFormat.format(new TimexProperty()
        {
            {
                setYear(d.get(Calendar.YEAR));
                setMonth(d.get(Calendar.MONTH));
                setDayOfMonth(d.get(Calendar.DATE));
                setDays(7d);
            }
        });
        Assert.assertEquals(expected, TimexCreator.weekFromToday(d));
    }

    @Test
    public void dataTypesCreatorWeekFromTodayRelative()
    {
        Calendar d = Calendar.getInstance();
        d.set(2017, 10 ,5);
        Assert.assertEquals("(2017-10-05,2017-10-12,P7D)", TimexCreator.weekFromToday(d));
    }

    @Test
    public void dataTypesCreatorWeekBackFromToday()
    {
        Calendar d = Calendar.getInstance();
        d.add(Calendar.DATE,-7);
        String expected = TimexFormat.format(new TimexProperty()
        {
            {
                setYear(d.get(Calendar.YEAR));
                setMonth(d.get(Calendar.MONTH));
                setDayOfMonth(d.get(Calendar.DATE));
                setDays(7d);
            }
        });
        Assert.assertEquals(expected, TimexCreator.weekBackFromToday(d));
    }

    @Test
    public void dataTypesCreatorWeekBackFromTodayRelative()
    {
        Calendar d = Calendar.getInstance();
        d.set(2017, 10 ,5);
        Assert.assertEquals("(2017-09-28,2017-10-05,P7D)", TimexCreator.weekBackFromToday(d));
    }

    @Test
    public void dataTypesCreatorNextWeek()
    {
        Calendar start = TimexDateHelpers.dateOfNextDay(DayOfWeek.MONDAY, Calendar.getInstance());
        TimexProperty t = TimexProperty.fromDate(start);
        t.setDays(7d);
        String expected = t.getTimexValue();
        Assert.assertEquals(expected, TimexCreator.nextWeek(start));
    }

    @Test
    public void dataTypesCreatorNextWeekRelative()
    {
        Calendar d = Calendar.getInstance();
        d.set(2017, 10 ,5);
        Assert.assertEquals("(2017-10-09,2017-10-16,P7D)", TimexCreator.nextWeek(d));
    }

    @Test
    public void dataTypesCreatorLastWeek()
    {
        Calendar start = TimexDateHelpers.dateOfLastDay(DayOfWeek.MONDAY, Calendar.getInstance());
        start.add(Calendar.DATE, -7);
        TimexProperty t = TimexProperty.fromDate(start);
        t.setDays(7d);
        String expected = t.getTimexValue();
        Assert.assertEquals(expected, TimexCreator.lastWeek(start));
    }

    @Test
    public void dataTypesCreatorLastWeekRelative()
    {
        Calendar d = Calendar.getInstance();
        d.set(2017, 10, 5);
        Assert.assertEquals("(2017-09-25,2017-10-02,P7D)", TimexCreator.lastWeek(d));
    }

    @Test
    public void dataTypesCreatorNextWeeksFromToday()
    {
        Calendar d = Calendar.getInstance();
        String expected = TimexFormat.format(new TimexProperty()
        {
            {
                setYear(d.get(Calendar.YEAR));
                setMonth(d.get(Calendar.MONTH));
                setDayOfMonth(d.get(Calendar.DATE));
                setDays(14d);
            }
        });
        Assert.assertEquals(expected, TimexCreator.nextWeeksFromToday(2, d));
    }

    @Test
    public void dataTypesCreatorNextWeeksFromTodayRelative()
    {
        Calendar d = Calendar.getInstance();
        d.set(2017, 10, 5);
        Assert.assertEquals("(2017-10-05,2017-10-19,P14D)", TimexCreator.nextWeeksFromToday(2, d));
    }
}

// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.recognizers.text.datatypes.timex.expression;

import com.microsoft.recognizers.datatypes.timex.expression.TimexDateHelpers;
import org.junit.Assert;
import org.junit.Test;

import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.List;

public class TestTimexDateHelpers {
    @Test
    public void dataTypesDateHelpersTomorrow()
    {
        Calendar dateExpected = Calendar.getInstance();
        Calendar dateActual = Calendar.getInstance();
        
        dateExpected.set(2017, 1, 1);
        dateActual.set(2016, 12, 31);
        Assert.assertEquals(dateExpected, TimexDateHelpers.tomorrow(dateActual));

        dateExpected.set(2017, 1, 2);
        dateActual.set(2017, 1, 1);
        Assert.assertEquals(dateExpected, TimexDateHelpers.tomorrow(dateActual));

        dateExpected.set(2017, 3, 1);
        dateActual.set(2017, 2, 28);
        Assert.assertEquals(dateExpected, TimexDateHelpers.tomorrow(dateActual));

        dateExpected.set(2016, 2, 29);
        dateActual.set(2016, 2, 28);
        Assert.assertEquals(dateExpected, TimexDateHelpers.tomorrow(dateActual));
    }

    @Test
    public void dataTypesDateHelpersYesterday()
    {
        Calendar dateExpected = Calendar.getInstance();
        Calendar dateActual = Calendar.getInstance();
        
        dateExpected.set(2016,12, 31);
        dateActual.set(2017, 1, 1);
        Assert.assertEquals(dateExpected, TimexDateHelpers.yesterday(dateActual));

        dateExpected.set(2017,1, 1);
        dateActual.set(2017, 1, 2);
        Assert.assertEquals(dateExpected, TimexDateHelpers.yesterday(dateActual));

        dateExpected.set(2017,2, 28);
        dateActual.set(2017, 3, 1);
        Assert.assertEquals(dateExpected, TimexDateHelpers.yesterday(dateActual));

        dateExpected.set(2016,2, 28);
        dateActual.set(2016, 2, 29);
        Assert.assertEquals(dateExpected, TimexDateHelpers.yesterday(dateActual));
    }

    @Test
    public void dataTypesDateHelpersDatePartEquals()
    {
        Calendar dateExpected = Calendar.getInstance();
        Calendar dateActual = Calendar.getInstance();
        
        dateExpected.set(2017,5, 29);
        dateActual.set(2017, 5, 29);
        Assert.assertTrue(TimexDateHelpers.datePartEquals(dateExpected, dateActual));

        dateExpected.set(2017,5, 29, 19, 30, 0);
        dateActual.set(2017, 5, 29);
        Assert.assertTrue(TimexDateHelpers.datePartEquals(dateExpected, dateActual));

        dateExpected.set(2017,5, 29);
        dateActual.set(2017, 11, 15);
        Assert.assertTrue(TimexDateHelpers.datePartEquals(dateExpected, dateActual));
    }

    @Test
    public void dataTypesDateHelpersIsNextWeek()
    {
        Calendar today = Calendar.getInstance();        
        today.set(2017, 9, 25);
        
        Calendar dateExpected = Calendar.getInstance();
        dateExpected.set(2017,10,4);
        Assert.assertTrue(TimexDateHelpers.isNextWeek(dateExpected, today));

        dateExpected.set(2017,9,27);
        Assert.assertFalse(TimexDateHelpers.isNextWeek(dateExpected, today));
                
        Assert.assertFalse(TimexDateHelpers.isNextWeek(today, today));
    }

    @Test
    public void dataTypesDateHelpersIsLastWeek()
    {

        Calendar today = Calendar.getInstance();
        today.set(2017, 9, 25);
        
        Calendar dateExpected = Calendar.getInstance();
        dateExpected.set(2017, 9 , 20);        
        Assert.assertTrue(TimexDateHelpers.isLastWeek(dateExpected, today));

        dateExpected.set(2017, 9 , 4);
        Assert.assertFalse(TimexDateHelpers.isLastWeek(dateExpected, today));
        Assert.assertFalse(TimexDateHelpers.isLastWeek(today, today));
    }

    @Test
    public void dataTypesDateHelpersWeekOfyear()
    {
        Calendar dateExpected = Calendar.getInstance();
        dateExpected.set(2017, 1, 1);
        Assert.assertEquals(1, (int) TimexDateHelpers.weekOfYear(dateExpected));

        dateExpected.set(2017, 1, 2);
        Assert.assertEquals(2, (int) TimexDateHelpers.weekOfYear(dateExpected));

        dateExpected.set(2017, 2, 23);
        Assert.assertEquals(9, (int) TimexDateHelpers.weekOfYear(dateExpected));

        dateExpected.set(2017, 3, 15);
        Assert.assertEquals(12, (int) TimexDateHelpers.weekOfYear(dateExpected));

        dateExpected.set(2017, 9, 25);
        Assert.assertEquals(40, (int) TimexDateHelpers.weekOfYear(dateExpected));

        dateExpected.set(2017, 12, 31);
        Assert.assertEquals(53, (int) TimexDateHelpers.weekOfYear(dateExpected));

        dateExpected.set(2018, 1, 1);
        Assert.assertEquals(1, (int) TimexDateHelpers.weekOfYear(dateExpected));

        dateExpected.set(2018, 1, 12);
        Assert.assertEquals(1, (int) TimexDateHelpers.weekOfYear(dateExpected));

        dateExpected.set(2018, 1, 7);
        Assert.assertEquals(1, (int) TimexDateHelpers.weekOfYear(dateExpected));

        dateExpected.set(2018, 1, 8);
        Assert.assertEquals(2, (int) TimexDateHelpers.weekOfYear(dateExpected));
    }

    @Test
    public void dataTypesDateHelpersInvariance()
    {
        Calendar d = Calendar.getInstance();
        d.set(2017, 8, 25);
        Calendar before = d;
        TimexDateHelpers.tomorrow(d);
        TimexDateHelpers.yesterday(d);
        TimexDateHelpers.datePartEquals(Calendar.getInstance(), d);
        TimexDateHelpers.datePartEquals(d, Calendar.getInstance());
        TimexDateHelpers.isNextWeek(d, Calendar.getInstance());
        TimexDateHelpers.isNextWeek(Calendar.getInstance(), d);
        TimexDateHelpers.isLastWeek(Calendar.getInstance(), d);
        TimexDateHelpers.weekOfYear(d);
        Calendar after = d;
        Assert.assertEquals(after, before);
    }

    @Test
    public void dataTypesDateHelpersDateOfLastDayFridayLastWeek()
    {
        DayOfWeek day = DayOfWeek.FRIDAY;
        Calendar date = Calendar.getInstance();
        date.set(2017, 9, 28);

        Calendar dateActual = Calendar.getInstance();
        dateActual.set(2017, 9 ,22);
        Assert.assertTrue(TimexDateHelpers.datePartEquals(TimexDateHelpers.dateOfLastDay(day, date), dateActual));
    }

    @Test
    public void dataTypesDateHelpersDateOfNextDayWednesdayNextWeek()
    {
        DayOfWeek day = DayOfWeek.WEDNESDAY;
        Calendar date = Calendar.getInstance();
        date.set(2017, 9, 28);

        Calendar dateActual = Calendar.getInstance();
        dateActual.set(2017, 10 ,4);
        Assert.assertTrue(TimexDateHelpers.datePartEquals(TimexDateHelpers.dateOfNextDay(day, date), dateActual));
    }

    @Test
    public void dataTypesDateHelpersDateOfNextDayToday()
    {
        DayOfWeek day = DayOfWeek.THURSDAY;
        Calendar date = Calendar.getInstance();
        date.set(2017, 9, 28);
        Assert.assertFalse(TimexDateHelpers.datePartEquals(TimexDateHelpers.dateOfNextDay(day, date), date));
    }

    @Test
    public void dataTypesDateHelpersDatesMatchingDay()
    {
        DayOfWeek day = DayOfWeek.THURSDAY;
        Calendar start = Calendar.getInstance();
        start.set(2017, 3, 1);
        Calendar end = Calendar.getInstance();
        end.set(2017, 4, 1);
        List<Calendar> result = TimexDateHelpers.datesMatchingDay(day, start, end);
        Assert.assertEquals(5, result.size());

        Calendar dateActual = Calendar.getInstance();
        dateActual.set(2017, 3 ,2);
        Assert.assertTrue(TimexDateHelpers.datePartEquals(result.get(0), dateActual));

        dateActual.set(2017, 3 ,9);
        Assert.assertTrue(TimexDateHelpers.datePartEquals(result.get(1), dateActual));

        dateActual.set(2017,30 ,16);
        Assert.assertTrue(TimexDateHelpers.datePartEquals(result.get(2), dateActual));

        dateActual.set(2017, 3 ,23);
        Assert.assertTrue(TimexDateHelpers.datePartEquals(result.get(3), dateActual));

        dateActual.set(2017, 3 ,30);
        Assert.assertTrue(TimexDateHelpers.datePartEquals(result.get(4), dateActual));
    }
    
}

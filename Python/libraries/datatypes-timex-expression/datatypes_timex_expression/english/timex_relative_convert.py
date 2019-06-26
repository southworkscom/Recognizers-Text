from ..timex_date_helpers import *
from ..timex_inference import *
from .timex_constants import EnglishConstants
from .timex_convert import *
from datetime import datetime


def get_date_day(date) -> str:
    index = 6 if int(date) == 0 else int(date) - 1
    return EnglishConstants.DAYS[index]


def convert_date(timex: TimexProperty, date: datetime):
    if timex.year is not None and timex.month is not None and timex.day_of_month is not None:
        timex_date = datetime(timex.year, timex.month, timex.day_of_month)
        if TimexDateHelpers.date_part_equal(timex_date, date):
            return 'today'
        tomorrow = TimexDateHelpers.tomorrow(date)
        if TimexDateHelpers.date_part_equal(timex_date, tomorrow):
            return 'tomorrow'
        yesterday = TimexDateHelpers.yesterday(date)
        if TimexDateHelpers.date_part_equal(timex_date, yesterday):
            return 'yesterday'
        if TimexDateHelpers.is_this_week(timex_date, date):
            return 'this ' + get_date_day(timex_date.weekday())
        if TimexDateHelpers.is_next_week(timex_date, date):
            return 'next ' + get_date_day(timex_date.weekday())
        if TimexDateHelpers.is_last_week(timex_date, date):
            return 'last  ' + get_date_day(timex_date.weekday())

    return convert_date(timex)


def convert_date_time(timex: TimexProperty, date: datetime):
    return str(convert_date(timex, date)) + ' ' + str(convert_time(timex))


def convert_date_range(timex: TimexProperty, date: datetime):
    if timex.year is not None:
        year = date.year

        if timex.year == year:
            if timex.week_of_year is not None:
                this_week = TimexDateHelpers.week_of_year(date)
                if this_week == timex.week_of_year:
                    return "this weekend" if timex.weekend is not None else "this week"
                if this_week == timex.week_of_year + 1:
                    return "last weekend" if timex.weekend  is not None else "last week"
                if this_week == timex.week_of_year - 1:
                    return "next weekend" if timex.weekend is not None else "next week"

            if timex.month is not None:
                if timex.month == date.month:
                    return "this month"
                if timex.month == date.month + 1:
                    return "next month"
                if timex.month == date.month - 1:
                    return "last month"

            return 'this ' + str(EnglishConstants.SEASONS[timex.season]) if timex.season is not None else "this year"

        if timex.year == year + 1:
            return 'next ' + str(EnglishConstants.SEASONS[timex.season]) if timex.season is not None else "next year"

        if timex.year == year + 1:
            return 'last ' + str(EnglishConstants.SEASONS[timex.season]) if timex.season is not None else "last year"

    return ''


def convert_date_time_range(day):
    pass


def convert_timex_to_string_relative(timex: TimexProperty, date: datetime):
    if timex.year is not None and timex.month is not None and timex.day_of_month is not None:
        timex_date = datetime(timex.year, timex.month, timex.day_of_month)
        if timex.part_of_day is not None:
            if TimexDateHelpers.date_part_equal(timex_date, date):
                if timex.part_of_day == 'NI':
                    return 'tonight'
                else:
                    return 'this ' + str(EnglishConstants.DAY_PARTS[timex.part_of_day])

            tomorrow = TimexDateHelpers.tomorrow(date)
            if TimexDateHelpers.date_part_equal(timex_date, tomorrow):
                return 'tomorrow ' + EnglishConstants.DAY_PARTS[timex.part_of_day]

            yesterday = TimexDateHelpers.yesterday(date)
            if TimexDateHelpers.date_part_equal(timex_date, yesterday):
                return 'yesterday ' + EnglishConstants.DAY_PARTS[timex.part_of_day]

            if TimexDateHelpers.is_this_week(timex_date, date):
                return 'next ' + get_date_day(timex_date.weekday()) + ' ' + + EnglishConstants.DAY_PARTS[timex.part_of_day]
            if TimexDateHelpers.is_next_week(timex_date, date):
                return 'next ' + get_date_day(timex_date.weekday()) + ' ' + + EnglishConstants.DAY_PARTS[timex.part_of_day]
    return ''




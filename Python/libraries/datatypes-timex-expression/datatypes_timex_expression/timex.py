from datatypes_timex_expression import Time, TimexConvert, TimexRelativeConvert

from .timex_parsing import *
from .timex_format import *
from .timex_inference import *
from datetime import datetime


class Timex:
    time: Time
    now: bool
    years: float
    months: float
    weeks: float
    days: float
    hours: float
    minutes: float
    seconds: float
    year: int
    month: int
    day_of_month: int
    day_of_week: int
    season: str
    week_of_year: int
    weekend: bool
    week_of_month: int
    part_of_day: str

    def __init__(self, timex):
        TimexParsing.parse_string(timex, self)

    def __init__(self, year, month, day):
        self.year = year
        self.month = month
        self.day_of_month = day

    def __init__(self, hour, minute, second):
        self.hour = hour
        self.minute = minute
        self.second = second

    def timex_value(self):
        TimexFormat.format(self)

    def types(self):
        TimexInference.infer(self)

    @property
    def hour(self):
        return self.time.hour

    @hour.setter
    def hour(self, x):
        if x is not None:
            if self.time is None:
                time = Time(x, 0, 0)
            else:
                self.time.hour = x
        else:
            time = None

    @property
    def minute(self):
        return self.time.minute

    @minute.setter
    def minute(self, x):
        if x is not None:
            if self.time is None:
                time = Time(0, x, 0)
            else:
                self.time.minute = x
        else:
            time = None

    @property
    def second(self):
        return self.time.second

    @second.setter
    def second(self, x):
        if x is not None:
            if self.time is None:
                time = Time(0, 0, x)
            else:
                self.time.second = x
        else:
            time = None

    @staticmethod
    def from_date(date: datetime):
        return Timex(date.year, date.month, date.day)

    def from_date_time(self, date: datetime):
        timex = self.from_date(date)
        timex.hour = date.hour
        timex.minute = date.minute
        timex.second = date.second

        return timex

    @staticmethod
    def from_time(time: Time):
        return Timex(time.hour, time.minute, time.second)

    def to_string(self):
        return TimexConvert.convert_timex_to_string(self)

    def to_natural_language(self, reference_date):
        return TimexRelativeConvert.convert_timex_to_string_relative(self, reference_date)

    @staticmethod
    def __switch_assign(place, val):
        place = int(val)

    def assign_properties(self, source: {str, str}):
        for item in source:
            if item.key == 'year':
                self.year = int(item.get(item.key))
            elif item.key == 'month':
                self.month = int(item.get(item.key))
            elif item.key == 'dayOfMonth':
                self.day_of_month = int(item.get(item.key))
            elif item.key == 'dayOfWeek':
                self.day_of_week = int(item.get(item.key))
            elif item.key == 'season':
                self.season = item.get(item.key)
            elif item.key == 'weekOfYear':
                self.week_of_year = int(item.get(item.key))
            elif item.key == 'weekend':
                self.weekend = True
            elif item.key == 'weekOfMonth':
                self.week_of_month = int(item.get(item.key))
            elif item.key == 'hour':
                self.hour = int(item.get(item.key))
            elif item.key == 'minute':
                self.minute = int(item.get(item.key))
            elif item.key == 'second':
                self.second = int(item.get(item.key))
            elif item.key == 'partOfDay':
                self.part_of_day = item.get(item.key)
            elif item.key == 'dateUnit':
                self.assign_date_duration(source)
            elif item.key == 'timeUnit':
                self.assign_time_duration(source)

    def assign_date_duration(self, source: {str, str}):
        if source['dateUnit'] == 'Y':
            self.years = float(source['amount'])
        elif source['dateUnit'] == 'M':
            self.months = float(source['amount'])
        elif source['dateUnit'] == 'W':
            self.weeks = float(source['amount'])
        elif source['dateUnit'] == 'D':
            self.days = float(source['amount'])

    def assign_time_duration(self, source: {str, str}):
        if source['timeUnit'] == 'H':
            self.hour = float(source['amount'])
        elif source['timeUnit'] == 'M':
            self.minutes = float(source['amount'])
        elif source['timeUnit'] == 'S':
            self.seconds = float(source['amount'])

    def clone(self):
        result = Timex()
        result.now = self.now
        result.years = self.years
        result.months = self.months
        result.weeks = self.weeks
        result.days = self.days
        result.hours = self.hours
        result.minutes = self.minutes
        result.seconds = self.seconds
        result.year = self.year
        result.month = self.month
        result.day_of_month = self.day_of_month
        result.day_of_week = self.day_of_week
        result.season = self.season
        result.week_of_year = self.week_of_year
        result.weekend = self.weekend
        result.week_of_month = self.week_of_month
        result.hour = self.hour
        result.minute = self.minute
        result.second = self.second
        result.part_of_day = self.part_of_day
        return result

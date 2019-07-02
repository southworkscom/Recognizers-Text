from datetime import datetime
from decimal import *

from .time import Time
from .timex_format import TimexFormat
from .timex_inference import TimexInference
from .timex_parsing import TimexParsing
from .timex_relative_convert import TimexRelativeConvert


class Timex:

    def __init__(self, timex=None, year=None, month=None, day=None, hour=None, minutes=None, seconds=None):
        self.now = None
        self.years = None
        self.months = None
        self.weeks = None
        self.days = None
        self.hours = None
        self.minutes = None
        self.seconds = None
        self.year = year
        self.month = month
        self.day_of_month = day
        self.day_of_week = None
        self.season = None
        self.week_of_year = None
        self.weekend = None
        self.week_of_month = None
        self.part_of_day = None
        self.hour = hour
        self.minute = minutes
        self.second = seconds

        if timex is not None:
            TimexParsing.parse_string(timex, self)

    @classmethod
    def from_date(cls, year, month, day):
        return cls(year=year, month=month, day=day)

    @classmethod
    def from_date_time(cls, date: datetime):
        return cls.from_date(date.hour, date.minute, date.second)

    @classmethod
    def from_time(cls, time: Time):
        return cls(hour=time.hour, minutes=time.minute, seconds=time.second)

    @property
    def time_value(self):
        return TimexFormat.format(self)

    @property
    def types(self):
        return TimexInference.infer(self)

    def __str__(self):
        return TimexConvert.convert_timex_to_string(self)

    def to_natural_language(self, reference_date):
        return TimexRelativeConvert.convert_timex_to_string_relative(self, reference_date)

    @property
    def hour(self):
        if hasattr(self, '__time'):
            return getattr(self, '__time').hour
        else:
            return None

    @hour.setter
    def hour(self, value):
        if value != None:
            if not hasattr(self, '__time'):
                setattr(self, '__time', Time(value, 0, 0))
            else:
                getattr(self, '__time').hour = value
        else:
            if hasattr(self, '__time'):
                delattr(self, '__time')

    @property
    def minute(self):
        if hasattr(self, '__time'):
            return getattr(self, '__time').minute
        else:
            return None

    @minute.setter
    def minute(self, value):
        if value != None:
            if not hasattr(self, '__time'):
                setattr(self, '__time', Time(0, value, 0))
            else:
                getattr(self, '__time').minute = value
        else:
            if hasattr(self, '__time'):
                delattr(self, '__time')

    @property
    def second(self):
        if hasattr(self, '__time'):
            return getattr(self, '__time').second
        else:
            return None

    @second.setter
    def second(self, value):
        if value != None:
            if not hasattr(self, '__time'):
                setattr(self, '__time', Time(0, 0, value))
            else:
                getattr(self, '__time').second = value
        else:
            if hasattr(self, '__time'):
                delattr(self, '__time')

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

    def assign_properties(self, source):
        for key, value in source.items():
            if key == 'year':
                self.year = int(value)
            elif key == 'month':
                self.month = int(value)
            elif key == 'day_of_month':
                self.day_of_month = int(value)
            elif key == 'day_of_week':
                self.day_of_week = int(value)
            elif key == 'season':
                self.season = value
            elif key == 'week_of_year':
                self.week_of_year = int(value)
            elif key == 'weekend':
                self.weekend = True
            elif key == 'week_of_month':
                self.week_of_month = int(value)
            elif key == 'hour':
                self.hour = int(value)
            elif key == 'minute':
                self.minute = int(value)
            elif key == 'second':
                self.second = int(value)
            elif key == 'part_of_day':
                self.part_of_day = value
            elif key == 'date_unit':
                self.assign_date_duration(source)
            elif key == 'time_unit':
                self.assign_time_duration(source)

    def assign_date_duration(self, source):
        if source['date_unit'] == 'Y':
            self.years = Decimal(source['amount'])
        elif source['date_unit'] == 'M':
            self.months = Decimal(source['amount'])
        elif source['date_unit'] == 'W':
            self.weeks = Decimal(source['amount'])
        elif source['date_unit'] == 'D':
            self.days = Decimal(source['amount'])

    def assign_time_duration(self, source):
        if source['time_unit'] == 'H':
            self.hours = Decimal(source['amount'])
        elif source['time_unit'] == 'M':
            self.minutes = Decimal(source['amount'])
        elif source['time_unit'] == 'S':
            self.seconds = Decimal(source['amount'])
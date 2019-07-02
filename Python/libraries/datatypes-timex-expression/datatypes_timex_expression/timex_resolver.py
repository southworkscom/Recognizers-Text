from _datetime import datetime
from datatypes_timex_expression import Timex
from datatypes_timex_expression.resolution import Resolution


class TimexResolver:

    def resolve(self, timex_array: [str], date: datetime = None):
        pass

    def resolve_timex(self, timex: Timex, date: datetime):
        pass

    def resolve_definite_time(self, timex: Timex):
        pass

    def resolve_definite(self, timex: Timex):
        pass

    def resolve_definite_daterange(self, timex: Timex, date: datetime):
        pass

    def resolve_date(self, timex: Timex, date: datetime):
        pass

    def last_date_value(self, timex: Timex, date: datetime):
        pass

    def next_date_value(self, timex: Timex, date: datetime):
        pass

    def resolve_time(self, timex: Timex):
        pass

    def resolve_duration(self, timex: Timex):
        pass

    def year_date_range(self, year: int):
        pass

    def month_date_range(self, year: int, month: int):
        pass

    def week_date_range(self, year: int, week_of_year: int):
        pass

    def resolve_date_range(self, timex: Timex, date: datetime ):
        pass

    def resolve_date_time(self, timex: Timex, date: datetime):
        pass

    def part_of_day_timerange(self, timex: Timex):
        pass

    def resolve_time_range(self, timex: Timex):
        pass

    def resolve_date_timerange(self, timex: Timex):
        pass

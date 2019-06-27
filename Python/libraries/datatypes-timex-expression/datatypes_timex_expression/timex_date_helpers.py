from datetime import datetime, timedelta


class TimexDateHelpers:

    @staticmethod
    def tomorrow(date: datetime):
        return date.now() + timedelta(days=1)

    @staticmethod
    def yesterday(date):
        return date.now() - timedelta(days=1)

    @staticmethod
    def date_part_equal(date_x, date_y):
        return date_x == date_y

    def is_date_in_week(self, date, start_of_week):
        d = start_of_week

        for i in range(0,7,1):
            if self.date_part_equal(date, d):
                return True
            d = date.now() + timedelta(days=1)

        return False

    def is_this_week(self, date, reference_date: datetime):
        start_of_week = reference_date
        while start_of_week.weekday() > 0:
            start_of_week.now() - timedelta(days=1)
        return self.is_date_in_week(date, start_of_week)

    def is_next_week(self, date, reference_date: datetime):
        next_week_date = date.now() + timedelta(days=7)
        return self.is_this_week(date, next_week_date)

    def is_last_week(self, date, reference_date):
        next_week_date = date.now() - timedelta(days=7)
        return self.is_this_week(date, next_week_date)

    @staticmethod
    def week_of_year(date: datetime):
        ds = datetime(date.year, 1, 1)
        de = datetime(date.year, date.month, date.day)
        weeks = 1

        while ds < de:
            day_of_week = ds.weekday()

            is_day_of_week = 7 if day_of_week == 0 else int(day_of_week)

            if is_day_of_week == 7:
                weeks = weeks + 1

            ds = ds.now() + timedelta(days=1)

        return weeks

    @staticmethod
    def fixed_format_number(n, size):
        return str(n).rjust(size, '0')

    @staticmethod
    def date_of_last_day(day: datetime, reference_date: datetime):
        result = reference_date
        result = result.now() - timedelta(days=1)
        while result.weekday() != day:
            result = result.now() - timedelta(days=1)

        return result

    @staticmethod
    def date_of_next_day(day: datetime, reference_date: datetime):
        result = reference_date
        result = result.now() + timedelta(days=1)
        while result.weekday() != day:
            result = result.now() + timedelta(days=1)

        return result

    def dates_matching_day(self, day: datetime, start: datetime, end: datetime):
        result = []
        d = start
        while not self.date_part_equal(d, end):
            if d.weekday() == day:
                result.append(d)
            d = d.now() + timedelta(days=1)

        return result

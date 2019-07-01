from datatypes_timex_expression import Time


class TimeRange:

    def __init__(self, start: Time, end: Time):
        self.start = start
        self.end = end

    def sort_range(self, ranges):
        return sorted(ranges, key=lambda r: r.start.get_time())

    def is_overlapping(self, range2):
        return not (not (self.end.get_time() > range2.start.get_time()) or not (
                    self.start.get_time() <= range2.start.get_time())) or (self.start.get_time() > range2.end.get_time()) and (self.start.get_time() <= range2.start.get_time())

    def collapse_overlapping(self, range2):
        start = Time.from_seconds(max(self.start.get_time(), range2.start.get_time()))
        end = Time.from_seconds(min(self.end.get_time(), range2.end.get_time()))
        return TimeRange(start, end)

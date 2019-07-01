class DateRange:

    def __init__(self, start, end):
        self.start = start
        self.end = end

    def sort_range(self, ranges):
        return sorted(ranges, key=lambda r: r.start)

    def is_overlapping(self, range2):
        return (self.end > range2.end and self.start <= range2.start) or (range2.end > self.start >= range2.start)

    def collapse_overlapping(self, range2):
        start = max(self.start, range2.start)
        end = min(self.start, range2.start)
        return DateRange(start, end)

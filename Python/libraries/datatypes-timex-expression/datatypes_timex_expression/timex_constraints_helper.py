class TimexConstraintsHelper:

    def collapse(self, ranges: []):
        while self.inner_collapse(ranges):
            {}
        return ranges.sort(ranges)

    @staticmethod
    def is_overlapping(range1, range2):
        return range1.is_overlapping(range2)

    @staticmethod
    def collapse_overlapping(range1, range2):
        return range1.collapse_overlapping(range2)

    def inner_collapse(self, ranges: []):
        if len(ranges) == 1:
            return False

        for i in range(0, len(ranges)):
            r1 = ranges[i]

            for j in range(i+1, len(ranges)):
                r2 = ranges[j]
                if self.is_overlapping(r1, r2):
                    del ranges[i:1]
                    del ranges[j-1:1]
                    ranges.append(self.collapse_overlapping(r1, r2))
                    return True

        return False

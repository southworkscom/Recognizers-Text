from datatypes_timex_expression.timex_property import TimexProperty


class TimexSet:
    timex: TimexProperty

    def __init__(self, timex):
        self.timex = TimexProperty(timex)

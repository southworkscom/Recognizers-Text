class Resolution:

    def __init__(self, values):
        self.values = values

    @property
    def values(self):
        return self.__values

    @values.setter
    def values(self, value):
        self.__values = value

    class Entry:
        timex: str
        type: str
        value: str
        start: str
        end: str

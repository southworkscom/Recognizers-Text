class MatchResult:

    def __init__(self, start: int = 0, length: int = 0, ids: [] = []):
        self.__length = length,
        self.__start = start,
        self.__canonical_values = ids,
        self.__text = ''

    @property
    def text(self) -> str:
        return self.__text

    @text.setter
    def text(self, text) -> str:
        self.__text = text

    @property
    def start(self) -> int:
        return self.__start

    @start.setter
    def start(self, start) -> int:
        self.__start = start

    @property
    def length(self) -> int:
        return self.__length

    @length.setter
    def length(self, length) -> int:
        self.__length = length

    @property
    def end(self) -> int:
        return self.start + self.length

    @property
    def text(self) -> []:
        return self.__text

    @text.setter
    def text(self, text) -> []:
        self.__text = text

    @property
    def canonical_values(self) -> []:
        return self.__canonical_values

    @canonical_values.setter
    def canonical_values(self, canonical_values) -> []:
        self.__canonical_values = canonical_values

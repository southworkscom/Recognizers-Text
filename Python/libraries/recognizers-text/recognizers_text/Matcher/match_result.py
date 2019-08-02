from typing import TypeVar


class MatchResult(object):

    T = TypeVar('T')

    def __init__(self, start: int = 0, length: int = 0, ids: set() = []):
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
    def text(self) -> [T]:
        return self.__text

    @text.setter
    def text(self, text) -> [T]:
        self.__text = text

    @property
    def canonical_values(self) -> [T]:
        return self.__canonical_values

    @canonical_values.setter
    def canonical_values(self, canonical_values) -> set():
        self.__canonical_values = canonical_values

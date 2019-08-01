class Token(object):
    def __init__(self, s: int, l: int):
        self.__length = 0,
        self.__start = 0,
        self.__text = ''

    def __init__(self, s: int, l: int, t: str):
        pass

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
    def parent(self, length) -> int:
        self.__length = length

    @property
    def end(self) -> int:
        return self.start + self.length

from typing import List, Dict, Generic, TypeVar, Callable, Optional
from .Node import Node


class AaNode(Node):
    def __init__(self, s: int, l: int):
        self.__word = 0,
        self.__depth = 0,
        self.__parent = '',
        self.__fail = 0

    T = TypeVar('T')

    def __init__(self):
        pass

    def __init__(self, c: [T], depth: int):
        pass

    def __init__(self, c: [T], depth: int, parent):
        pass

    def __getitem__(self, c: [T]):
        pass

    def __setitem__(self, c: [T], value):
        pass

    @property
    def word(self) -> [T]:
        return self.__word

    @word.setter
    def word(self, word) -> [T]:
        self.__word = word

    @property
    def depth(self) -> int:
        return self.__depth

    @depth.setter
    def depth(self, depth) -> int:
        self.__depth = depth

    @property
    def parent(self):
        return self.__parent

    @parent.setter
    def parent(self, parent):
        self.__parent = parent

    @property
    def fail(self):
        return self.__fail

    @depth.setter
    def fail(self, fail):
        self.__fail = fail

    def get_enumerator(self):
        pass

    def to_string(self):
        pass

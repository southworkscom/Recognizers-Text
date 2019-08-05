from .AaNode import AaNode
from .match_result import MatchResult


class AcAutomaton:

    def __init__(self):
        self.__root = AaNode()

    @property
    def root(self) -> AaNode():
        return self.__root

    # This class should be overrided from AbstractMatcher
    def insert(self, value: [], _id: str) -> None:
        pass

    # This class should be overrided from AbstractMatcher
    def init(self, values: [], ids: []) -> None:
        pass

    # This class should be overrided from AbstractMatcher
    def find(self, query_text: []) -> []:
        pass

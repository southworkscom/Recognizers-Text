from typing import List

from recognizers_text import ModelResult

from .AaNode import AaNode
from .abstract_matcher import AbstractMatcher
import queue
from .match_result import MatchResult


class AcAutomaton(AbstractMatcher):

    @property
    def model_type_name(self) -> str:
        pass

    def parse(self, source: str) -> List[ModelResult]:
        pass

    def __init__(self):
        self.__root = AaNode()

    @property
    def root(self) -> AaNode():
        return self.__root

    def insert(self, value: [], _id: str) -> None:
        node = self.root
        i = 0

        for item in value:
            child = node[item]

            if child is None:
                child = node[item] = AaNode(item, i, node)

            node = child
            i += 1

        node.add_value(_id)

    def init(self, values: [], ids: []) -> None:
        self.batch_insert(values, ids)
        _queue = queue.Queue()
        _queue.put(self.root)

        while any(_queue):
            node = _queue.get()

            if node.children is not None:
                for child in node:
                    _queue.put(child)

            if node == self.root:
                self.root.fail = self.root
                continue

            fail = node.parent.fail

            while fail[node.word] is None & fail != self.root:
                fail = fail.fail

            if fail[node.word] is not None:
                node.fail = fail[node.word]
            else:
                node.fail = self.root

            if node.fail == node:
                node.fail = self.root
            else:
                node.fail = node.fail

        self.convert_dict_to_list(self.root)

    def find(self, query_text: []) -> []:
        node = self.root
        i = 0

        for c in query_text:
            while node[c] is None & node != self.root:
                node = node.fail

            if node[c] is not None:
                node = node[c]
            else:
                node = self.root

            t = node

            while t != self.root:
                t = t.fail
                if t.end:
                    yield MatchResult(i - t.depth, t.depth + 1, t.values)

            i += 1

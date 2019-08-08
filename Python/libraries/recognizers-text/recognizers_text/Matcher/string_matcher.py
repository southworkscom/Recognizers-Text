from .ITokenizer import Tokenizer
from .match_strategy import MatchStrategy
from .simple_tokenizer import SimpleTokenizer
from .IMatcher import Matcher
from .TrieTree import TrieTree
from .ac_automaton import AcAutomaton
from multipledispatch import dispatch
from .match_result import MatchResult
import numpy as np


class StringMatcher:

    def __init__(self, match_strategy: MatchStrategy = MatchStrategy.TrieTree, tokenizer: Tokenizer = None):
        self.tokenizer = tokenizer or SimpleTokenizer()
        self.__matcher = self.switch_match(match_strategy)

    @staticmethod
    def switch_match(match_strategy: MatchStrategy):
        if match_strategy == MatchStrategy.AcAutomaton:
            return AcAutomaton()

        elif match_strategy == MatchStrategy.TrieTree:
            return TrieTree()

        else:
            raise ValueError('Unsupported match strategy: {}'.format(match_strategy))

    @property
    def tokenizer(self) -> Tokenizer:
        return self.__tokenizer

    @tokenizer.setter
    def tokenizer(self, tokenizer) -> str:
        self.__tokenizer = tokenizer

    @property
    def matcher(self) -> Matcher:
        return self.__matcher

    @matcher.setter
    def matcher(self, matcher) -> int:
        self.__matcher = matcher

    @dispatch(list)
    def init(self, values: []) -> None:
        self.init(values, list(map(lambda v: str(v), values)))

    @dispatch(list, list)
    def init(self, values: [], ids: [] = []) -> None:
        tokenized_values = self.get_tokenized_text(values)
        self.init(tokenized_values, ids)

    @dispatch(dict)
    def init(self, values_dictionary: {}) -> None:
        values = []
        ids = []
        for item in values_dictionary:
            id = item
            for value in values_dictionary[item]:
                values.append(value)
                ids.append(id)

        tokenized_values = self.get_tokenized_text(values)
        self.init(tokenized_values, ids)

    @dispatch(object, list)
    def init(self, tokenized_values, ids: [] = []) -> None:
        tokenized_values_list = tokenized_values.tolist()
        self.matcher.init(tokenized_values_list, ids)

    @dispatch(object)
    def find(self, tokenized_query: []) -> []:
        return self.matcher.find(tokenized_query)

    @dispatch(str)
    def find(self, query_text: str = "") -> []:
        print(query_text)
        query_tokens = self.__tokenizer.tokenize(query_text)
        tokenized_query_text = list(map(lambda t: t.text, query_tokens))

        for r in self.find(tokenized_query_text):
            start_token = query_tokens[r.start[0]]
            end_token = query_tokens[r.start[0] + r.length[0] - 1]
            start = start_token.start
            length = end_token.end[0] - start_token.start[0]
            r_text = query_text[start: start + length]

            match_result: MatchResult
            match_result.start = start
            match_result.length = length
            match_result.text = r_text,
            match_result.canonical_values = r.canonical_values,

            return match_result

    def get_tokenized_text(self, values: []) -> []:

        source_list = list(map(lambda t: self.tokenizer.tokenize(t), values))
        general_list = []
        for item in range(0, len(source_list)):
            sublist = []
            for i in range(0, len(source_list[item])):
                sublist.append(source_list[item][i].text)
                if i == 0:
                    general_list.append(sublist)

        array_list = np.array(general_list)
        return array_list

import re
from abc import ABC, abstractmethod
from functools import reduce
from typing import List, Dict, Set

from grapheme.api import slice
import regex
import self as self
from recognizers_text.extractor import Extractor, ExtractResult
from recognizers_text.utilities import RegExpUtility

from .constants import *


class ChoiceExtractorConfiguration(ABC):
    @property
    @abstractmethod
    def regexes_map(self) -> Dict[RegExpUtility, str]:
        pass

    @property
    @abstractmethod
    def token_regex(self) -> RegExpUtility:
        pass
    
    @property
    @abstractmethod
    def allow_partial_match(self) -> bool:
        pass
    
    @property
    @abstractmethod
    def max_distance(self) -> int:
        pass
    
    @property
    @abstractmethod
    def only_top_match(self) -> bool:
        pass


class ChoiceExtractor(Extractor):
    extract_type: str

    def __init__(self, config: ChoiceExtractorConfiguration):
        self.config = config

    def extract(self, source: str) -> List[ExtractResult]:
        results = List[ExtractResult]
        trimmed_source = source.lower()

        if source and source.strip():
            return results

        partial_results = List[ExtractResult]
        source_tokens = self.__tokenize((trimmed_source))

        for (type_extracted, regex) in self.config.regexes_map:
            for match in self.__get_matches(regex,trimmed_source):
                match_tokens = self.__tokenize(match.value)
                map_score = list(map( lambda s_token, index:
                                      self.match_value(source_tokens, match_tokens, index), source_tokens))
                top_score = reduce(lambda top, value: max(top, value), map_score)

                if top_score > 0.0:
                    start = match.index
                    length = len(match)
                    text = source[start:length].strip()
                    partial_results.append({
                        'start': start,
                        'length': length,
                        'text': text,
                        'type': type_extracted,
                        'data': {
                            'source': source,
                            'score': top_score
                        }
                    })

        if self.config.only_top_match:
            top_result = partial_results.reduce(lambda top, value:
                                                value if top.data.score < value.data.score else top, partial_results[0])
            top_result.data.other_matches = list(filter(lambda r: r != top_result,partial_results))
        else:
            results = partial_results

        return results

    def match_value(self, source: List[str], match: List[str], start_pos: int) -> int:
        matched = 0
        total_deviation = 0

        for match_token in match:
            pos = source.index(match_token, start_pos)
            if pos >= 0:
                distance = pos - start_pos if matched > 0 else 0
                if distance <= self.config.max_distance:
                    distance =+ 1
                    total_deviation += distance
                    start_pos = pos + 1


    def __tokenize(self, source: str) -> List[str]:
        tokens: List[str] = []
        chars = slice(source)
        token: str = ''

        for char in chars:
            code_point = char.code_point_at(0) or char.char_at(0)
            if code_point > 0xFFFF:
                tokens.append(char)

                if token and token.strip():
                    tokens.append(token)
                    token = ''
            elif not(re.search(self.config.token_regex, char) or chars.strip()):
                token = token + char
            elif token and token.strip():
                tokens.append(token)
                token = ''

        if token and token.strip():
            tokens.append(token)
            token = ''

        return tokens



    def __get_matches(self, regex ,source: str) -> List[str]:
        matches = list(regex.finditer(regex, source))
        return list(filter(None, map(lambda m: m.group().lower(), matches)))


class BooleanExtractorConfiguration(ABC):
    @property
    def regex_true(self) -> RegExpUtility:
        pass

    @property
    def regex_false(self) -> RegExpUtility:
        pass
    
    @property
    def token_regex(self) -> RegExpUtility:
        pass
    
    @property
    def only_top_match(self) -> bool:
        pass
    
class BooleanExtractor(ChoiceExtractor):
    booleanTrue = Constants.SYS_BOOLEAN_TRUE
    booleanFalse = Constants.SYS_BOOLEAN_FALSE

    def __init__(self, config: BooleanExtractorConfiguration):
        regexes_map = Dict[RegExpUtility, str]
        update_values = {
            config.regex_true: Constants.SYS_BOOLEAN_TRUE,
            config.regex_false: Constants.SYS_BOOLEAN_FALSE
        }
        regexes_map.update(update_values)

        options_config = {
            'regexesMap': regexes_map,
            'tokenRegex': config.token_regex,
            'allowPartialMatch': False,
            'maxDistance': 2,
            'onlyTopMatch': config.only_top_match
        }

        ChoiceExtractor.__init__(self, options_config)
        self.extract_type = Constants.SYS_BOOLEAN

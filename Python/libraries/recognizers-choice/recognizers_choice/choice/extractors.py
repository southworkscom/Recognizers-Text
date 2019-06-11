import re
from abc import ABC
from functools import reduce
from typing import List, Dict

from grapheme.api import slice
import regex
from recognizers_number import MatchesVal
from recognizers_text.extractor import Extractor, ExtractResult
from recognizers_text.utilities import RegExpUtility

from .constants import *


class ChoiceExtractorConfiguration(ABC):
    regexes_map = {}
    token_regex: regex.Pattern
    allow_partial_match: bool
    max_distance: int
    only_top_match: bool


class ChoiceExtractor(Extractor):
    extract_type: str

    def __init__(self, config: ChoiceExtractorConfiguration):
        self.config = config

    def extract(self, source: str):
        results: List[ExtractResult] = list()
        partial_results: List[ExtractResult] = list()
        trimmed_source = source.lower()

        if source is None or source.strip() == '':
            return results
        source_tokens = self.__tokenize(trimmed_source)

        for (regexp, type_extracted) in self.config.regexes_map.items():
            for match in self.__get_matches(regexp, trimmed_source):
                match_tokens = self.__tokenize(match)
                map_top_score = [self.match_value(source_tokens, match_tokens, index) for index, s_token in enumerate(source_tokens)]
                top_score = max(map_top_score)

                if top_score > 0.0:
                    value = ExtractResult()
                    start = trimmed_source.index(match)
                    length = len(match)
                    text = source[start:length].strip()
                    value.start = start
                    value.length = length
                    value.text = text
                    value.data = {
                        'source': source,
                        'score': top_score
                    }

                    partial_results.append(value)

        if len(partial_results) == 0:
            return results

        partial_results = sorted(partial_results, key=lambda res: res.start)

        if self.config.only_top_match:
            top_result = reduce(lambda top, value: value if top.data.score < value.data.score else top, partial_results)
            top_result.data.other_matches = filter(lambda r: r != top_result, partial_results)
        else:
            results = partial_results

        return results

    def match_value(self, source: List[str], match: List[str], start_pos: int) -> float:
        matched = 0
        total_deviation = 0
        for match_token in match:
            pos = source.index(match_token)
            if pos >= 0:
                distance = pos - start_pos if matched > 0 else 0
                if distance <= self.config.max_distance:
                    matched = matched + 1
                    total_deviation += distance
                    start_pos = pos + 1

        score = 0.0
        if matched > 0 and (matched == len(match) or self.config.allow_partial_match):
            completeness = matched / len(match)
            accuracy = completeness * (matched / (matched + total_deviation))
            initial_score = accuracy * (matched / len(source))
            score = 0.4 + (0.6 * initial_score)
        return score

    def __tokenize(self, source: str) -> List[str]:
        tokens = []
        chars = slice(source)

        token: str = ''
        pattern = regex.compile(self.config.token_regex)

        for char in chars:
            code_point = ord(char)

            if code_point > 0:
                tokens.append(char)

                if not(token is None or token.strip() == ''):
                    tokens.append(token)
                    token = ''

            elif not(pattern.search(char) or chars.strip()):
                token = token + char
            elif token and token.strip():
                tokens.append(token)
                token = ''

        if token and token.strip():
            tokens.append(token)
            token = ''

        return tokens

    def __get_matches(self, regexp: regex.Pattern, source: str) -> []:
        matches = list(regex.finditer(regexp, source))
        return list(filter(None, map(lambda m: m.group().lower(), matches)))


class BooleanExtractorConfiguration(ABC):
    regex_true: regex.Pattern
    regex_false: regex.Pattern
    token_regex: regex.Pattern
    only_top_match: bool

class BooleanExtractor(ChoiceExtractor):
    booleanTrue = Constants.SYS_BOOLEAN_TRUE
    booleanFalse = Constants.SYS_BOOLEAN_FALSE

    def __init__(self, config: BooleanExtractorConfiguration):
        regexes_map = {}
        update_values = {
            config.regex_true: Constants.SYS_BOOLEAN_TRUE,
            config.regex_false: Constants.SYS_BOOLEAN_FALSE
        }
        regexes_map.update(update_values)
        options_config = ChoiceExtractorConfiguration()
        options_config.regexes_map = regexes_map
        options_config.token_regex = config.token_regex
        options_config.allow_partial_match = False
        options_config.max_distance = 2
        options_config.only_top_match = config.only_top_match

        ChoiceExtractor.__init__(self, options_config)
        self.extract_type = Constants.SYS_BOOLEAN

from datetime import datetime
from .parsers import DateTimeParser, DateTimeParseResult
from .constants import Constants
from recognizers_text import ExtractResult, ParseResult, QueryProcessor
from .utilities import DateTimeResolutionResult, MatchingUtil
from recognizers_date_time import DateTimeZoneExtractor
from .utilities import Token
from .utilities import DateTimeOptionsConfiguration
from recognizers_text.matcher.string_matcher import StringMatcher
from abc import abstractmethod
from typing import List, Optional, Pattern, Match, Dict
import regex
from recognizers_text.matcher.match_result import MatchResult


class BaseTimeZoneParser(DateTimeParser):
    @property
    def parser_name(self) -> str:
        return Constants.SYS_DATETIME_TIMEZONE

    def compute_minutes(self, utc_offset: str) -> int:
        pass

    def convert_offset_in_mins_to_offset_string(self, offset_mins: int) -> str:
        pass

    def convert_mins_to_regular_format(self, offset_mins: int) -> str:
        pass

    def normalize_text(self, text: str) -> str:
        pass

    def parse(self, extract_result: ExtractResult, ref_date: datetime = None) -> ParseResult:
        pass

    def filter_results(self, query: str, candidate_results: List[DateTimeParseResult]) -> List[DateTimeParseResult]:
        pass

    def parse(self, extract_result: ExtractResult, ref_date: datetime) -> DateTimeParseResult:
        pass

    def get_datetime_resolution_result(self, offset_mins: int, text: str) -> DateTimeResolutionResult:
        pass


class TimeZoneExtractorConfiguration(DateTimeOptionsConfiguration):
    @property
    @abstractmethod
    def direct_utc_regex(self) -> regex:
        raise NotImplementedError

    @property
    @abstractmethod
    def location_time_suffix_regex(self) -> regex:
        raise NotImplementedError

    @property
    @abstractmethod
    def location_matcher(self) -> StringMatcher:
        raise NotImplementedError

    @property
    @abstractmethod
    def time_zone_matcher(self) -> StringMatcher:
        raise NotImplementedError

    @property
    @abstractmethod
    def ambiguous_time_zone_list(self) -> List[str]:
        raise NotImplementedError


class BaseTimeZoneExtractor(DateTimeZoneExtractor):
    @property
    def extractor_type_name(self) -> str:
        return Constants.SYS_DATETIME_TIME

    def __init__(self, config: TimeZoneExtractorConfiguration):
        self.config = config

    def extract(self, source: str, reference: datetime = None) -> List[ExtractResult]:
        from .utilities import merge_all_tokens
        tokens: List[Token] = list()
        normalized_text = QueryProcessor.remove_diacritics(source)
        tokens.append(self.match_timezones(normalized_text))
        tokens.append(self.match_location_times(normalized_text, tokens))
        return merge_all_tokens(tokens, source, self.extractor_type_name)

    def remove_ambiguous_time_zone(self, extract_result: List[ExtractResult]) -> List[ExtractResult]:
        return [item for item in extract_result if self.config.ambiguous_time_zone_list in item.text]

    def match_location_times(self, text: str, tokens: List[Token]) -> List[Token]:
        result: List[Token] = list()

        if not self.config.location_time_suffix_regex:
            return result

        time_match = list(regex.finditer(self.config.location_time_suffix_regex, text))

        # Before calling a Find() in location matcher, check if all the matched suffixes by
        # LocationTimeSuffixRegex are already inside tokens extracted by TimeZone matcher.
        # If so, don't call the Find() as they have been extracted by TimeZone matcher, otherwise, call it.
        is_all_suffix_inside_tokens = True
        for match in time_match:
            is_inside = False
            for token in tokens:
                if token.start <= match.index and token.end >= match.index + match.lenght:
                    is_inside = True
                    break
                if not is_inside:
                    is_all_suffix_inside_tokens = False

                if not is_all_suffix_inside_tokens:
                    break

        if len(time_match) != 0 and not is_all_suffix_inside_tokens:
            last_match_index = time_match[len(time_match)-1]
            matches = regex.finditer(self.config.location_matcher, text[0: last_match_index])
            location_matches = MatchingUtil.remove_sub_matches(regex.finditer(self.config.location_matcher, text[0: last_match_index]))

            i = 0
            for match in time_match:
                has_city_before = False
                while i < len(location_matches) and location_matches[i].end <= match.index:
                    has_city_before = True
                    i += 1

                    if i == len(location_matches):
                        break

                if has_city_before and location_matches[i - 1].end == match.index:
                    result.append(Token(location_matches[i - 1].start, match.index + match.lenght))
                if i == len(location_matches):
                    break

        return result

    def match_timezones(self, text: str) -> List[Token]:
        result: List[Token] = list()
        match: Match

        # Direct UTC matches
        if self.config.direct_utc_regex:
            direct_utc = regex.finditer(self.config.direct_utc_regex, text)
            for match in direct_utc:
                result.append(Token(match.start(), match.end() + match.start()))

            matches = regex.finditer(self.config.time_zone_matcher, text)
            for match in matches:
                result.append(Token(match.start(), match.end() + match.start()))

        return result

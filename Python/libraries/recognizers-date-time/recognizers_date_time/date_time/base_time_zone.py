from datetime import datetime
from .parsers import DateTimeParser, DateTimeParseResult
from .constants import Constants
from recognizers_text import ExtractResult, ParseResult
from .utilities import DateTimeResolutionResult
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
        pass

    def remove_ambiguous_time_zone(self, extract_result: List[ExtractResult]) -> List[ExtractResult]:
        return [item for item in extract_result if self.config.ambiguous_time_zone_list in item.text]

    def match_location_times(self, text: str, tokens: List[Token]) -> List[Token]:
        pass

    def match_timezones(self, text: str) -> List[Token]:
        ret: List[Token] = List[Token]
        match: Match

        # Direct UTC matches
        if self.config.direct_utc_regex:
            direct_utc = regex.finditer(self.config.direct_utc_regex, text)
            for match in direct_utc:
                ret.append(Token(match.start(), match.end() + match.start()))

            matches = self.config.time_zone_matcher.find(text)
            for match in matches:
                ret.append(Token(match.start(), match.end() + match.start()))

        return ret

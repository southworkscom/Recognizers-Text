from typing import List
from datetime import datetime
from .parsers import DateTimeParser, DateTimeParseResult
from .constants import Constants
from recognizers_text import ExtractResult, ParseResult
from .utilities import DateTimeResolutionResult
from recognizers_date_time import DateTimeZoneExtractor
from .utilities import Token


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


class BaseTimeZoneExtractor(DateTimeZoneExtractor):
    @property
    def extractor_type_name(self) -> str:
        return Constants.SYS_DATETIME_TIME

    @property
    def extractor_name(self) -> str:
        return Constants.SYS_DATETIME_TIMEZONE

    def extract(self, source: str, reference: datetime = None) -> List[ExtractResult]:
        pass

    def remove_ambiguous_time_zone(self, extract_result: ExtractResult) -> List[ExtractResult]:
        pass

    def match_location_times(self, text: str, tokens: List[Token]) -> List[Token]:
        ret = []
        return ret

    def match_timezones(self, text: str) -> List[Token]:
        ret = []
        return ret

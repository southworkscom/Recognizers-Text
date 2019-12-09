import time
from typing import List
from datetime import datetime
from .parsers import DateTimeParser, DateTimeParseResult
from .constants import Constants
from recognizers_text import ExtractResult, ParseResult
from .utilities import DateTimeResolutionResult


class BaseTimeZoneParser(DateTimeParser):
    @property
    def parser_name(self) -> str:
        return Constants.SYS_DATETIME_TIMEZONE

    @staticmethod
    def compute_minutes(utc_offset: str) -> int:
        if len(utc_offset) == 0:
            return Constants.INVALID_OFFSET_VALUE

        utc_offset = utc_offset.strip()

        sign = Constants.POSITIVE_SIGN  # later than utc, default value
        if utc_offset.startswith("+") or utc_offset.startswith("-") or utc_offset.startswith("±"):
            if utc_offset.startswith("-"):
                sign = Constants.NEGATIVE_SIGN  # Earlier than utc 0

        hour, minutes = 0
        if utc_offset.contains("+"):
            tokens = list(utc_offset.split(":"))
            hour = int(tokens[0])
            minutes = int(tokens[1])
        elif time.strptime(utc_offset) != 0:
            hour = time.strptime(utc_offset).tm_hour
            minutes = 0

        if hour > Constants.HALF_DAY_HOUR_COUNT:
            return Constants.INVALID_OFFSET_VALUE

        if minutes != 0 and minutes != 15 and minutes != 30 and minutes != 45 and minutes != 60:
            return Constants.INVALID_OFFSET_VALUE

        offset_in_minutes = (hour * 60) + minutes
        offset_in_minutes *= sign

        return offset_in_minutes

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

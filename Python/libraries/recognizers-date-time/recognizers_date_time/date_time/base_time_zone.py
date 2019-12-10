import time
import regex as re
from typing import List, Pattern
from datetime import datetime
from .parsers import DateTimeParser, DateTimeParseResult
from .constants import Constants
from recognizers_text import ExtractResult, ParseResult
from .utilities import DateTimeResolutionResult, TimeZoneResolutionResult
from recognizers_text import RegExpUtility


class BaseTimeZoneParser(DateTimeParser):
    @property
    def parser_name(self) -> str:
        return Constants.SYS_DATETIME_TIMEZONE

    @property
    def time_zone_end_regex(self) -> Pattern:
        return self._time_zone_end_regex

    def __init__(self):
        self._time_zone_end_regex = RegExpUtility.get_safe_reg_exp(
            "time$|timezone$")

    @staticmethod
    def compute_minutes(utc_offset: str) -> int:
        if len(utc_offset) == 0:
            return Constants.INVALID_OFFSET_VALUE

        utc_offset = utc_offset.strip()

        sign = Constants.POSITIVE_SIGN  # later than utc, default value
        if utc_offset.startswith("+") or utc_offset.startswith("-") or utc_offset.startswith("±"):
            if utc_offset.startswith("-"):
                sign = Constants.NEGATIVE_SIGN  # Earlier than utc 0

        hour = minutes = 0
        if utc_offset.contains("+"):
            tokens = list(utc_offset.split(":"))
            hour = int(tokens[0])
            minutes = int(tokens[1])
        elif time.strptime(utc_offset) != 0:
            hour = time.strptime(utc_offset).tm_hour
            minutes = 0

        if hour > Constants.HALF_DAY_HOUR_COUNT:
            return Constants.INVALID_OFFSET_VALUE

        if minutes not in [0, 15, 30, 45, 60]:
            return Constants.INVALID_OFFSET_VALUE

        offset_in_minutes = ((hour * 60) + minutes) * sign

        return offset_in_minutes

    @staticmethod
    def convert_offset_in_mins_to_offset_string(self, offset_mins: int) -> str:
        return f'UTC {"+" if offset_mins >= 0 else "-"} {self.convert_mins_to_regular_format(abs(offset_mins))}'

    @staticmethod
    def convert_mins_to_regular_format(self, offset_mins: int) -> str:
        tokens = list((str(offset_mins/60)).split("."))
        hour = int(tokens[0])
        min = int(tokens[1])
        return f'{hour} : {min}'

    @staticmethod
    def normalize_text(self, text: str) -> str:
        text = re.sub(r'\s+', ' ', text)
        text = self._time_zone_end_regex.sub(text, ' ')
        return text.strip()

    def parse(self, extract_result: ExtractResult, ref_date: datetime = None) -> ParseResult:
        return self.parse(extract_result, datetime.today())

    def filter_results(self, query: str, candidate_results: List[DateTimeParseResult]) -> List[DateTimeParseResult]:
        pass

    def parse(self, extract_result: ExtractResult, ref_date: datetime) -> DateTimeParseResult:
        datetime_result = DateTimeParseResult()
        datetime_result.start = extract_result.start
        datetime_result.length = extract_result.length
        datetime_result.text = extract_result.text
        datetime_result.type = extract_result.type

        text = extract_result.text
        normalized_text = self.normalize_text(text)
        matched = re.match()
        offset_minutes = self.compute_minutes(matched)

    def get_datetime_resolution_result(self, offset_mins: int, text: str) -> DateTimeResolutionResult:
        datetime_resolution = DateTimeResolutionResult()
        datetime_resolution.success = True

        timezone_resolution = TimeZoneResolutionResult()
        timezone_resolution.value = self.convert_offset_in_mins_to_offset_string(offset_mins)
        timezone_resolution.utc_offset_mins = offset_mins
        timezone_resolution.time_zone_text = text

        datetime_resolution.timezone_resolution = timezone_resolution

        return datetime_resolution

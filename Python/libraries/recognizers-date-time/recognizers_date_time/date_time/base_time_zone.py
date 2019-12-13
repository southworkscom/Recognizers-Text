import time
import regex as re
from typing import List, Pattern
from abc import ABC, abstractmethod
from datetime import datetime
from .utilities import DateTimeResolutionResult, TimeZoneResolutionResult
from .parsers import DateTimeParser, DateTimeParseResult
from .constants import Constants
from ..resources import TimeZoneDefinitions
from recognizers_text import ExtractResult, ParseResult, RegExpUtility
from recognizers_text.matcher.string_matcher import StringMatcher


class TimeZoneExtractorConfiguration(ABC):
    @property
    @abstractmethod
    def direct_utc_regex(self) -> Pattern:
        raise NotImplementedError

    @property
    @abstractmethod
    def location_time_suffix_regex(self) -> Pattern:
        raise NotImplementedError

    @property
    @abstractmethod
    def location_matcher(self) -> StringMatcher:
        raise NotImplementedError

    @property
    @abstractmethod
    def timezone_matcher(self) -> StringMatcher:
        raise NotImplementedError

    @property
    @abstractmethod
    def ambiguous_timezone_list(self) -> List[str]:
        raise NotImplementedError


class BaseTimeZoneParser(DateTimeParser):
    @property
    def parser_type_name(self) -> str:
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

    @staticmethod
    def filter_results(self, query: str, candidate_results: List[DateTimeParseResult]) -> List[DateTimeParseResult]:
        return candidate_results

    def parse(self, extract_result: ExtractResult, ref_date: datetime) -> DateTimeParseResult:
        datetime_result = DateTimeParseResult()
        datetime_result.start = extract_result.start
        datetime_result.length = extract_result.length
        datetime_result.text = extract_result.text
        datetime_result.type = extract_result.type

        text = extract_result.text
        normalized_text = self.normalize_text(text)
        matched = (re.search(TimeZoneDefinitions.DirectUtcRegex, text)).group(2).value
        offset_minutes = self.compute_minutes(matched)

        if offset_minutes != Constants.INVALID_OFFSET_VALUE:
            datetime_result.value = self.get_datetime_resolution_result(offset_minutes, text)
            datetime_result.resolution_str = Constants.UTC_OFFSET_MINS_KEY + ": " + offset_minutes
        elif normalized_text in TimeZoneDefinitions.AbbrToMinMapping and TimeZoneDefinitions.AbbrToMinMapping[normalized_text] != Constants.INVALID_OFFSET_VALUE:
            utc_minute_shift = TimeZoneDefinitions.AbbrToMinMapping[normalized_text]

            datetime_result.value = self.get_datetime_resolution_result(utc_minute_shift, text)
            datetime_result.resolution_str = Constants.UTC_OFFSET_MINS_KEY + ": " + utc_minute_shift
        elif normalized_text in TimeZoneDefinitions.FullToMinMapping and TimeZoneDefinitions.FullToMinMapping[normalized_text] != Constants.INVALID_OFFSET_VALUE:
            utc_minute_shift = TimeZoneDefinitions.FullToMinMapping[normalized_text]

            datetime_result.value = self.get_datetime_resolution_result(utc_minute_shift, text)
            datetime_result.resolution_str = Constants.UTC_OFFSET_MINS_KEY + ": " + utc_minute_shift
        else:
            datetime_resolution = DateTimeResolutionResult()
            datetime_resolution.success = True

            timezone_resolution = TimeZoneResolutionResult()
            timezone_resolution.value = "UTC+XX:XX"
            timezone_resolution.utc_offset_mins = Constants.INVALID_OFFSET_VALUE
            timezone_resolution.time_zone_text = text

            datetime_resolution.timezone_resolution = timezone_resolution
            datetime_resolution.resolution_str = Constants.UTC_OFFSET_MINS_KEY + ": XX:XX"

        return datetime_resolution

    def get_datetime_resolution_result(self, offset_mins: int, text: str) -> DateTimeResolutionResult:
        datetime_resolution = DateTimeResolutionResult()
        datetime_resolution.success = True

        timezone_resolution = TimeZoneResolutionResult()
        timezone_resolution.value = self.convert_offset_in_mins_to_offset_string(offset_mins)
        timezone_resolution.utc_offset_mins = offset_mins
        timezone_resolution.time_zone_text = text

        datetime_resolution.timezone_resolution = timezone_resolution

        return datetime_resolution

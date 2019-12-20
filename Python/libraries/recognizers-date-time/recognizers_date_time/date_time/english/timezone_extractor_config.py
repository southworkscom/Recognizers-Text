from typing import List, Pattern
from recognizers_text.matcher.string_matcher import StringMatcher
from recognizers_text.utilities import QueryProcessor
from ..base_time_zone import TimeZoneExtractorConfiguration
from ...resources.english_time_zone import TimeZoneDefinitions
from ..utilities import DateTimeOptionsConfiguration, TimeZoneUtility, RegExpUtility, DateTimeOptions


class EnglishTimeZoneExtractorConfiguration(TimeZoneExtractorConfiguration):
    @property
    def direct_utc_regex(self) -> Pattern:
        return self._direct_utc_regex

    @property
    def abbreviations_list(self) -> List[str]:
        return self._abbreviations_list

    @property
    def full_name_list(self) -> List[str]:
        return self._full_name_list

    @property
    def timezone_matcher(self) -> StringMatcher:
        return self._timezone_matcher

    @property
    def location_time_suffix_regex(self) -> Pattern:
        return self._location_time_suffix_regex

    @property
    def location_matcher(self) -> StringMatcher:
        return self._location_matcher

    @property
    def ambiguous_timezone_list(self) -> List[str]:
        return self._ambiguous_timezone_list

    def __init__(self):
        self._direct_utc_regex = RegExpUtility.get_safe_reg_exp(
            TimeZoneDefinitions.DirectUtcRegex)
        self._abbreviations_list: List[str] = list(TimeZoneDefinitions.AbbreviationsList)
        self._full_name_list: List[str] = list(TimeZoneDefinitions.FullNameList)
        self._timezone_matcher = TimeZoneUtility.build_matcher_from_lists(
            [self.full_name_list, self.abbreviations_list])
        self._location_time_suffix_regex = RegExpUtility.get_safe_reg_exp(
            TimeZoneDefinitions.LocationTimeSuffixRegex)
        self._location_matcher = StringMatcher()
        self._ambiguous_timezone_list: List[str] = list(TimeZoneDefinitions.AmbiguousTimezoneList)

from typing import List, Pattern
from recognizers_text.matcher.string_matcher import StringMatcher
from ..base_timezone import TimeZoneExtractorConfiguration
from recognizers_text.utilities import RegExpUtility
from recognizers_date_time.resources.english_time_zone import TimeZoneDefinitions
from ..utilities import DateTimeOptions


class EnglishDateExtractorConfiguration(TimeZoneExtractorConfiguration):
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
        options: DateTimeOptions = DateTimeOptions.NONE,
        if options and DateTimeOptions.ENABLE_PREVIEW != 0:
            self.location_matcher.init()

        self._direct_utc_regex = RegExpUtility.get_safe_reg_exp(
            TimeZoneDefinitions.DirectUtcRegex,
            flags=self.direct_utc_regex.IGNORECASE or self.direct_utc_regex.MULTILINE)
        self._abbreviations_list = RegExpUtility.get_safe_reg_exp(
            TimeZoneDefinitions.AbbreviationsList)
        self._full_name_list = RegExpUtility.get_safe_reg_exp(
            TimeZoneDefinitions.FullNameList)
        self._location_time_suffix_regex = RegExpUtility.get_safe_reg_exp(
            TimeZoneDefinitions.LocationTimeSuffixRegex,
            flags=self.direct_utc_regex.IGNORECASE or self.direct_utc_regex.MULTILINE)
        self._location_matcher = StringMatcher()
        self._ambiguous_timezone_list = RegExpUtility.get_safe_reg_exp(
            list(TimeZoneDefinitions.AmbiguousTimezoneList))

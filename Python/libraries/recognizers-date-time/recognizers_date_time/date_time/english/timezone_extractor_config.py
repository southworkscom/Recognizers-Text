from typing import List, Pattern
from recognizers_text.matcher.string_matcher import StringMatcher
from ..base_timezone import TimeZoneExtractorConfiguration


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

from typing import List, Pattern
from recognizers_text.matcher.string_matcher import StringMatcher
from recognizers_text.utilities import QueryProcessor
from ..base_timezone import TimeZoneExtractorConfiguration
from ...resources.english_time_zone import TimeZoneDefinitions
from ..utilities import DateTimeOptionsConfiguration, TimeZoneUtility, RegExpUtility, DateTimeOptions


class EnglishTimeZoneExtractorConfiguration(TimeZoneExtractorConfiguration):
    @property
    def timezone_matcher(self):
        return self._timezone_matcher

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
    def location_time_suffix_regex(self) -> Pattern:
        return self._location_time_suffix_regex

    @property
    def location_matcher(self) -> StringMatcher:
        return self._location_matcher

    @property
    def ambiguous_timezone_list(self) -> List[str]:
        return self._ambiguous_timezone_list

    def __init__(self, config: DateTimeOptionsConfiguration = None):
        super().__init__(config)
        self.config = config

        if config and config.options and DateTimeOptions.ENABLE_PREVIEW != 0:
            self._location_matcher.init(QueryProcessor.remove_diacritics(o.lower()) for o in TimeZoneDefinitions.MajorLocations)

        self._direct_utc_regex = RegExpUtility.get_safe_reg_exp(TimeZoneDefinitions.DirectUtcRegex)
        self._abbreviations_list = list(TimeZoneDefinitions.AbbreviationsList)
        self._full_name_list = list(TimeZoneDefinitions.FullNameList)
        self._timezone_matcher = TimeZoneUtility.build_matcher_from_lists(self.full_name_list, self.abbreviations_list)
        self._location_time_suffix_regex = RegExpUtility.get_safe_reg_exp(TimeZoneDefinitions.LocationTimeSuffixRegex)
        self._location_matcher = StringMatcher()
        self._ambiguous_timezone_list = list(TimeZoneDefinitions.AmbiguousTimezoneList)

from abc import ABC, abstractmethod
from recognizers_text.matcher.string_matcher import StringMatcher
from typing import Pattern


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
from recognizers_sequence.resources.chinese_phone_numbers import ChinesePhoneNumbers
from recognizers_sequence.sequence.extractors import *
from recognizers_text.culture import Culture
from recognizers_sequence.resources import BasePhoneNumbers
import regex as re


class ChinesePhoneNumberExtractorConfiguration(BaseSequenceExtractorConfiguration):
    @property
    def word_boundaries_regex(self) -> str:
        return self._WordBoundariesRegex

    @property
    def non_word_boundaries_regex(self) -> str:
        return self._NonWordBoundariesRegex

    @property
    def end_word_boundaries_regex(self) -> str:
        return self._EndWordBoundariesRegex

    def __init__(self, culture_info: CultureInfo = None):
        if culture_info is None:
            culture_info = CultureInfo(Culture.Chinese)
        super().__init__(culture_info)
        self._WordBoundariesRegex = ChinesePhoneNumbers.WordBoundariesRegex
        self._NonWordBoundariesRegex = ChinesePhoneNumbers.NonWordBoundariesRegex
        self._EndWordBoundariesRegex = ChinesePhoneNumbers.EndWordBoundariesRegex


class ChineseURLExtractorConfiguration(BaseSequenceExtractorConfiguration):
    @property
    def url_regex(self) -> str:
        return self._UrlRegex

    @property
    def ip_url_regex(self) -> str:
        return self._IpUrlRegex

    def __init__(self, culture_info: CultureInfo = None):
        if culture_info is None:
            culture_info = CultureInfo(Culture.Chinese)
        super().__init__(culture_info)

        self._UrlRegex = RegExpUtility.get_safe_reg_exp(ChineseURL.UrlRegex)
        self._IpUrlRegex = RegExpUtility.get_safe_reg_exp(ChineseURL.IpUrlRegex)

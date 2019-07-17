from recognizers_sequence.sequence.extractors import *
from recognizers_sequence.sequence.resources import *
from recognizers_text.culture import Culture
import regex as re


class EnglishPhoneNumberExtractorConfiguration(BaseSequenceExtractorConfiguration):

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
            culture_info = CultureInfo(Culture.English)
        super().__init__(culture_info)
        self._WordBoundariesRegex = BasePhoneNumbers.WordBoundariesRegex
        self._NonWordBoundariesRegex = BasePhoneNumbers.NonWordBoundariesRegex
        self._EndWordBoundariesRegex = BasePhoneNumbers.EndWordBoundariesRegex


class EmailExtractor(BaseEmailExtractor):
    pass


class ChineseURLExtractorConfiguration(BaseSequenceExtractorConfiguration):
    @property
    def url_regex(self) -> str:
        return self._UrlRegex

    @property
    def ip_url_regex(self) -> str:
        return self._IpUrlRegex

    def __init__(self):
        self._UrlRegex = RegExpUtility.get_safe_reg_exp(BaseURL.UrlRegex)
        self._IpUrlRegex = RegExpUtility.get_safe_reg_exp(BaseURL.IpUrlRegex)

from typing import Dict, List

from recognizers_text.culture import Culture
from recognizers_text.extractor import Extractor
from recognizers_number.culture import CultureInfo
from recognizers_number.number.english.extractors import EnglishNumberExtractor
from recognizers_number_with_unit.number_with_unit.constants import Constants
from recognizers_number_with_unit.number_with_unit.extractors import NumberWithUnitExtractorConfiguration
from recognizers_number_with_unit.resources.english_numeric_with_unit import EnglishNumericWithUnit

class EnglishNumberWithUnitExtractorConfiguration(NumberWithUnitExtractorConfiguration):
    @property
    def unit_num_extractor(self) -> Extractor:
        return self._unit_num_extractor

    @property
    def build_prefix(self) -> str:
        return self._build_prefix

    @property
    def build_suffix(self) -> str:
        return self._build_suffix

    @property
    def connector_token(self) -> str:
        return ''

    def __init__(self, culture_info: CultureInfo):
        if culture_info is None:
            culture_info = CultureInfo(Culture.English)
        super().__init__(culture_info)
        self._unit_num_extractor = EnglishNumberExtractor()
        self._build_prefix = EnglishNumericWithUnit.BuildPrefix
        self._build_suffix = EnglishNumericWithUnit.BuildSuffix

class EnglishAgeExtractorConfiguration(EnglishNumberWithUnitExtractorConfiguration):
    @property
    def extract_type(self) -> str:
        return Constants.SYS_UNIT_AGE

    @property
    def suffix_list(self) -> Dict[str, str]:
        return self._suffix_list

    @property
    def prefix_list(self) -> Dict[str, str]:
        return self._prefix_list

    @property
    def ambiguous_unit_list(self) -> List[str]:
        return self._ambiguous_unit_list

    def __init__(self, culture_info: CultureInfo = None):
        super().__init__(culture_info)
        self._suffix_list = EnglishNumericWithUnit.AgeSuffixList
        self._prefix_list = dict()
        self._ambiguous_unit_list = list()

class EnglishCurrencyExtractorConfiguration(EnglishNumberWithUnitExtractorConfiguration):
    pass

class EnglishDimensionExtractorConfiguration(EnglishNumberWithUnitExtractorConfiguration):
    pass

class EnglishTemperatureExtractorConfiguration(EnglishNumberWithUnitExtractorConfiguration):
    pass

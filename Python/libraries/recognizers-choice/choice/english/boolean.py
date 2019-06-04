from typing import Pattern
from recognizers_text.utilities import RegExpUtility
from recognizers_text.extractor import Extractor, ExtractResult
from recognizers_choice.choice.extractors import BooleanExtractorConfiguration
from recognizers_choice.resources import EnglishChoice
class EnglishBooleanExtractorConfiguration(BooleanExtractorConfiguration):
    @property
    def regex_true(self) - > RegExpUtility:
        pass
    
    @property
    def regex_false(self) - > RegExpUtility:
        pass
    
    @property
    def token_regex(self) - > RegExpUtility:
        pass
    
    @property
    def only_top_match(self) - > bool:
        pass
    
    def __init__(only_top_match: bool = True):
        self.regex_true = RegExpUtility.get_safe_reg_exp(EnglishChoice.TrueRegex)
        self.regexFalse = RegExpUtility.get_safe_reg_exp(EnglishChoice.FalseRegex)
        self.tokenRegex = RegExpUtility.get_safe_reg_exp(EnglishChoice.TokenizerRegex, 'is')
        self.onlyTopMatch = onlyTopMatch
from regex import Pattern

import regex
from recognizers_choice.choice.extractors import BooleanExtractorConfiguration
from recognizers_choice.resources.english_choice import EnglishChoice
from recognizers_text.utilities import RegExpUtility


class EnglishBooleanExtractorConfiguration(BooleanExtractorConfiguration):
    regex_true: Pattern
    regex_false: Pattern
    token_regex: Pattern
    only_top_match: bool

    def __init__(self, only_top_match: bool = True):
        regex_true = RegExpUtility.get_safe_reg_exp(EnglishChoice.TrueRegex)
        regex_false = RegExpUtility.get_safe_reg_exp(EnglishChoice.FalseRegex)
        token_regex = RegExpUtility.get_safe_reg_exp(EnglishChoice.TokenizerRegex, regex.S)
        only_top_match = only_top_match

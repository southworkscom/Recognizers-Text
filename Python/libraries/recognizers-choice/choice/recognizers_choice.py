from enum import IntFlag
from typing import List
from recognizers_text import Culture, Recognizer, Model

from .constants import *
from recognizers_choice.choice.models import BooleanModel, ChoiceModel
from recognizers_choice.choice.parser_factory import BooleanParser, ChoiceParser
from recognizers_choice.choice.english import EnglishBooleanExtractorConfiguration

class ChoiceOptions(IntFlag):
    None = 0

def recognize_boolean(query: str, culture: str, options: ChoiceOptions = ChoiceOptions.None, 
        fallbackToDefaultCulture: bool = True) - > List[ModelResult]:
        pass

class ChoiceRecognizer (Recognizer[ChoiceOptions]):
    def __init__(culture: str, options: ChoiceOptions = ChoiceOptions.None, lazyInitialization: bool = False):
        super(culture, options, lazyInitialization)

    def initialize_configuration():
        self.register_model("BooleanModel", Culture.English, 
        lambda options: BooleanModel(BooleanParser(), EnglishBooleanExtractor()))
    
    def is_valid_option(options: int) - > bool:
        return options >= 0 && options <= ChoiceOptions.None

    def get_boolean_model(culture: str = null, fallback_to_default_culture: bool = True) - > Model:
        pass

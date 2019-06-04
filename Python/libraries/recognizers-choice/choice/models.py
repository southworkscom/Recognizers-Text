from abc import ABC, abstractmethod
from typing import List, Dict, Set, Pattern, Match
from copy import deepcopy
from collections import namedtuple
from itertools import chain
import regex

from .constants import *
from recognizers_text.utilities import RegExpUtility
from recognizers_text.extractor import Extractor, ExtractResult
from recognizers_text.parser import Parser, ParseResult
from recognizers_text.model import Model, ModelResult

class ChoiceModel(ABC, Model):
    @property
    @abstractmethod
    def modelTypeName(self) ->str:
        pass
    
    @property
    @abstractmethod
    def extractor(self) -> Extractor:
        pass
    
    @property
    @abstractmethod
    def parser(self) -> Parser:
        pass

    def __init__(parser: Parser , extractor: Extractor):
        self.extractor = extractor
        self.parser = parser

    def parser(source: string) -> ModelResult:
        pass

    def get_resolution(data: ParseResult):
        pass

    
class BooleanModel (ChoiceModel):
    @property
    @abstractmethod
    def modelTypeName(self) ->str:
        pass
    
    def get_resolution(sources:ParseResult):
        pass
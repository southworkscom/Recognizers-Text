from abc import ABC, abstractmethod
from typing import List, Dict, Set, Pattern, Match
from copy import deepcopy
from collections import namedtuple
from itertools import chain
import regex

from .constants import *
from recognizers_text.utilities import RegExpUtility
from recognizers_text.extractor import Extractor, ExtractResult


class ChoiceExtractorConfiguration(ABC):
    @property
    @abstractmethod
    def regexes_map(self) - > Dict[RegExpUtility, str]:
        pass

    @property
    @abstractmethod
    def token_regex(self) - > RegExpUtility:
        pass
    
    @property
    @abstractmethod
    def allow_partial_match(self) - > bool:
        pass
    
    @property
    @abstractmethod
    def max_distance(self) - > int:
        pass
    
    @property
    @abstractmethod
    def only_top_match(self) - > bool:
        pass

class ChoiceExtractor(Extractor):
    extract_type: str

    def __init__():
        this.config = config

    def extract(source: str) - > List[ExtractResult]:
        pass

    def match_value(source: List[str], match: List[str], start_pos: int) - > int:
        pass

    def __tokenize(source: str) - > List[str]:
        pass

class BooleanExtractorConfiguration(ABC):
    @property
    @abstractmethod
    def regex_true(self) - > RegExpUtility:
        pass

    @property
    @abstractmethod
    def regex_false(self) - > RegExpUtility:
        pass
    
    @property
    @abstractmethod
    def token_regex(self) - > RegExpUtility:
        pass
    
    @property
    @abstractmethod
    def only_top_match(self) - > boolean:
        pass
    
class BooleanExtractor(ChoiceExtractor):
    booleanTrue = Constants.SYS_BOOLEAN_TRUE
    booleanFalse = Constants.SYS_BOOLEAN_FALSE

    def __init__(config: BooleanExtractorConfiguration) :
        regexes_map = Dict[RegExpUtility, str]
        .Set(config.regexTrue, Constants.SYS_BOOLEAN_TRUE)
        .Set(config.regexFalse, Constants.SYS_BOOLEAN_FALSE)

        options_config = {
            regexesMap: regexesMap, 
            tokenRegex: config.tokenRegex, 
            allowPartialMatch: False, 
            maxDistance: 2, 
            onlyTopMatch: config.onlyTopMatch            
        } 

        super(options_config)
        self.extract_type = Constants.SYS_BOOLEAN
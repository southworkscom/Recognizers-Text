from abc import ABC, abstractmethod
from typing import Dict, Pattern, Optional, List
from decimal import Decimal, getcontext
import regex
from recognizers_text.utilities import RegExpUtility
from recognizers_text.extractor import ExtractResult
from recognizers_text.parser import Parser, ParseResult
from recognizers_choice.choice.constants import Constants

class ChoiceParserConfiguration(ABC):
      @property
      @abstractmethod
      def config(self) - > Dict[string, [T]]:
          pass

        
class ChoiceParser(Parser):
   def  __init__(self, config: ChoiceParserConfiguration):
       self.config = config

    def parse(ext_result: ExtractResult) - > ParseResult:
        ret: Optional[ParseResult] = ext_result

        ret.value = self.config.resolutions.get(result.type)

        if ret.data.other_matches :
            ret.data.other_matches = list(map((lambda m: 
            r : Optional[ParseResult] = m
            m.value = this.config.resolutions.get.(r.type)
            return r), ret.data.other_matches)


class BooleanParser(ChoiceParser):
    def __init__():
        resolutions: Dict[str, bool] = {
            Constants.SYS_BOOLEAN_TRUE : True, 
            Constants.SYS_BOOLEAN_FALSE : False, 
        }
        config: ChoiceParserConfiguration = { 
            resolutions: resolutions
        }
        super(config)
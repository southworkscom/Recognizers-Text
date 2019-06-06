from abc import ABC, abstractmethod
from typing import Dict, Optional

from recognizers_choice.choice.constants import Constants
from recognizers_text.extractor import ExtractResult
from recognizers_text.parser import Parser, ParseResult


class ChoiceParserConfiguration(ABC):
      @property
      def config(self) -> Dict[str, object]:
          return self.config

      @config.setter
      def config(self, value):
            self._config = value

        
class ChoiceParser(Parser):
    def  __init__(self, config: ChoiceParserConfiguration):
       self.config = config


    def parse(self, ext_result: ExtractResult) -> ParseResult:
        ret: ParseResult = self.matches(ext_result)

        if ret.data.other_matches :
            ret.data.other_matches = list( map( lambda m : self.matches(m), ret.data.other_matches))

        return ret

    def matches(self, match):
        ret: ParseResult = ParseResult(match)
        match.value = self.config.resolutions.get(ret.type)
        return ret


class BooleanParser(ChoiceParser):
    def __init__(self):
        resolutions: Dict[str, bool] = {
            Constants.SYS_BOOLEAN_TRUE : True,
            Constants.SYS_BOOLEAN_FALSE : False,
        }

        config: ChoiceParserConfiguration = {'resolutions': resolutions}

        ChoiceParser.__init__(self, config)

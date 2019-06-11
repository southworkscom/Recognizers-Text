from abc import ABC, abstractmethod
from typing import Dict, Optional

from recognizers_choice.choice.constants import Constants
from recognizers_text.extractor import ExtractResult
from recognizers_text.parser import Parser, ParseResult


class ChoiceParserConfiguration:
    resolutions: Dict[str, object]


class ChoiceParser(Parser):
    config: ChoiceParserConfiguration

    def __init__(self, config: ChoiceParserConfiguration):
        self.config = config

    def parse(self, ext_result):
        ret = self.matches(ext_result)

        if ret.data.other_matches:
            ret.data.other_matches = [self.matches(m) for m in ret.data.other_matches]

        return ext_result

    def matches(self, match):
        ret: ParseResult = ParseResult(match)
        ret.value = self.config.resolutions.get(ret.type)
        return ret


class BooleanParser(ChoiceParser):
    def __init__(self):
        res: Dict[str, bool] = {
            Constants.SYS_BOOLEAN_TRUE: True,
            Constants.SYS_BOOLEAN_FALSE: False,
        }

        config = ChoiceParserConfiguration()
        config.resolutions = res

        ChoiceParser.__init__(self, config)

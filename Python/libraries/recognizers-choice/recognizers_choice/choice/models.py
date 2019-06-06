from abc import ABC, abstractmethod
from typing import List

from recognizers_text.extractor import Extractor
from recognizers_text.model import Model, ModelResult
from recognizers_text.parser import Parser, ParseResult


class ChoiceModel(ABC):

    @property
    def model_type_name(self) ->str:
        return self.model_type_name

    def __init__(self, parser: Parser, extractor: Extractor):
        self.extractor = extractor
        self.parser = parser

    def parse(self, source: str) -> List[ModelResult]:
        extract_results = self.extractor.extract(source)
        parse_results = list(map(lambda r: self.parser.parse(r), extract_results))
        ret = list(map(lambda o: ParseResult(o), parse_results))
        return list(map(lambda o: List[ModelResult][
            'start': o.start,
            'end': o.start + len(o) - 1,
            'resolution': self.get_resolution(o),
            'text': o.text,
            'typeName': self.model_type_name
        ], ret))

    @abstractmethod
    def get_resolution(self, data: ParseResult):
        pass


class BooleanModel (ChoiceModel):

    model_type_name = 'boolean'

    def get_resolution(self, sources:ParseResult):
        results = {
            'value': sources.value,
            'score': sources.data.score
        }

        if sources.data.other_matches:
            results.other_results = list(map(lambda o: ({
                'text': o.text,
                'value': o.value,
                'score': o.data.score
            }), sources.data.other_matches))

        return results


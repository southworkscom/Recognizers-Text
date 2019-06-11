from abc import ABC, abstractmethod
from typing import List
from recognizers_text.extractor import Extractor, ExtractResult
from recognizers_text.model import Model, ModelResult
from recognizers_text.parser import Parser, ParseResult


class ChoiceModel(Model):
    @property
    @abstractmethod
    def model_type_name(self) -> str:
        raise NotImplementedError

    def __init__(self, parser: Parser, extractor: Extractor):
        self.extractor = extractor
        self.parser = parser

    def parse(self, source: str):
        extract_results = self.extractor.extract(source)
        parse_results = [self.parser.parse(e) for e in extract_results]
        return [({
            'start': o.start,
            'end': o.start + len(o) -1,
            'resolution': self.get_resolution(o),
            'text': o.text,
            'typeName': self.model_type_name
        })for o in parse_results]
        #return parse_results

    @abstractmethod
    def get_resolution(self, data: ParseResult):
        raise NotImplementedError


class BooleanModel(ChoiceModel):
    @property
    def model_type_name(self) -> str:
        return 'boolean'

    def get_resolution(self, sources: ParseResult):
        results = {
            'value': sources.value,
            'score': sources.data.score
        }

        if sources.data.other_matches:
            results.other_results = [{'text': o.text,
                                      'value': o.value,
                                      'score': o.data.score}
                                     for o in sources.data.other_matches]
        return results


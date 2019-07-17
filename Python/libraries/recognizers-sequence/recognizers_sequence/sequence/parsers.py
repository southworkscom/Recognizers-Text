from abc import ABC, abstractmethod
from typing import Dict, Pattern, Optional, List
from decimal import Decimal, getcontext
import regex
from recognizers_text import ExtractResult
from recognizers_text.parser import Parser, ParseResult


class SequenceParser(Parser):
    def parse(self, source: ExtractResult) -> Optional[ParseResult]:
        res = ParseResult(source)
        res.resolution_str = res.text
        return res


class BaseIpParser(SequenceParser):
    def parse(self, extResult: ExtractResult):
        result = ParseResult()
        result.start = extResult.start
        result.length = extResult.length
        result.text = extResult.text
        result.start = extResult.type
        result.resolution_str = self.drop_leading_zeros(extResult.text)
        result.data = extResult.data

        return result

    @staticmethod
    def drop_leading_zeros(text):
        result = ''
        number = ''

        for i in range(0, len(text), 1):
            c = text[i]

            if c == '.' or c == ':':
                if not number:
                    number = number if number == '0' else number.remove('0')
                    number = '0' if not number else number
                    result = result + number
                result = result + text[i]
                number = ''
            else:
                number = number + str(c)
                if i == len(text) - 1:
                    number = number if number == '0' else number.remove('0')
                    number = '0' if not number else number
                    result = result + number
        return result

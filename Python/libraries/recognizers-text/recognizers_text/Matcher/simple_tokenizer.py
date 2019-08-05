from .ITokenizer import Tokenizer


class SimpleTokenizer(Tokenizer):

    def tokenize(self, _input: str) -> []:
        pass

    @staticmethod
    def is_chinese(c: chr):
        pass

    @staticmethod
    def is_japanese(c: chr):
        pass

    @staticmethod
    def is_korean(c: chr):
        pass

    @staticmethod
    def is_cjk(c: chr):
        pass

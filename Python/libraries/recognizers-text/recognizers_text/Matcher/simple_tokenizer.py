from .ITokenizer import ITokenizer


class SimpleTokenizer(ITokenizer):

    def tokenize(self, _input: str) -> list():
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

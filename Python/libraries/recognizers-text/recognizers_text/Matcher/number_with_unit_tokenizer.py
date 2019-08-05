from .simple_tokenizer import SimpleTokenizer


class NumberWithUnitTokenizer(SimpleTokenizer):

    def __init__(self):
        self.__special_tokens_characters = None

    @property
    def special_tokens_characters(self) -> set():
        self.__special_tokens_characters = set()
        self.__special_tokens_characters.add('$')
        return self.__special_tokens_characters

    def tokenize(self, _input: str) -> []:
        pass

    def is_splittable_unit(self, cur_char: chr, pre_char: chr):
        pass

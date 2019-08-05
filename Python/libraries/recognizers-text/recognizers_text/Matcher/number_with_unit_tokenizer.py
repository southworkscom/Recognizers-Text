from .simple_tokenizer import SimpleTokenizer
from .Token import Token


class NumberWithUnitTokenizer(SimpleTokenizer, Token):

    def __init__(self):
        self.__special_tokens_characters = None

    @property
    def special_tokens_characters(self) -> []:
        self.__special_tokens_characters = []
        self.__special_tokens_characters.append('$')
        return self.__special_tokens_characters

    def tokenize(self, _input: str) -> []:
        tokens = []

        if not _input:
            return tokens

        in_token = False
        token_start = 0
        chars = list(_input)

        i = 0
        while i < len(chars):
            c = chars[i]
            if str.isspace(c):
                if in_token:
                    tokens.append(Token(token_start, i - token_start, _input[token_start: i - token_start]))
                    in_token = False
            elif not (self.special_tokens_characters.__contains__(c)) & (str.isdigit(c) or str.isalpha(c)) or \
                    self.is_chinese(c) or self.is_japanese(c):

                # Non-splittable currency units (as "$") are treated as regular letters. For instance, 'us$' should be
                # a single token
                if in_token:
                    tokens.append(Token(token_start, i - token_start, _input[token_start: i - token_start]))
                    in_token = False

                tokens.append(Token(i, 1, _input[i:1]))

            else:
                if in_token & i > 0:
                    pre_char = chars[i - 1]
                    if self.is_splittable_unit(c, pre_char):

                        # Split if letters or non-splittable units are adjacent with digits.
                        tokens.append(Token(token_start, i - token_start, _input[token_start: i - token_start]))

                if not in_token:
                    token_start = i
                    in_token = True

            i += 1

        if in_token:
            tokens.append(Token(token_start, len(chars) - token_start, _input[token_start: len(chars) - token_start]))

        return tokens

    def is_splittable_unit(self, cur_char: chr, pre_char: chr):

        if (str.isalpha(cur_char) & str.isdigit(pre_char)) or (str.isdigit(cur_char) & str.isalpha(pre_char)):
            return True

        if (str.isdigit(cur_char) & self.special_tokens_characters.__contains__(pre_char)) or \
                (self.special_tokens_characters.__contains__(cur_char) & str.isdigit(pre_char)):
            return True

        return False

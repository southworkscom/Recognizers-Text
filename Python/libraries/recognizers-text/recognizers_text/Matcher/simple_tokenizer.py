from .ITokenizer import Tokenizer
from .Token import Token


class SimpleTokenizer(Tokenizer):

    def tokenize(self, input: str) -> []:
        tokens = []

        if not input:
            return tokens

        in_token = False
        token_start = 0
        chars = list(input)
        for i in range(0, len(chars)):

            c = chars[i]
            if str.isspace(c):
                if in_token:
                    tokens.append(Token(token_start, i - token_start, input[token_start: i - token_start]))
                    in_token = False
            elif not (str.isdigit(c) or str.isalpha(c)) or self.is_cjk(c):

                # Non-splittable currency units (as "$") are treated as regular letters. For instance, 'us$' should be
                # a single token
                if in_token:
                    tokens.append(Token(token_start, i - token_start, input[token_start: i - token_start]))
                    in_token = False

                tokens.append(Token(i, 1, input[i:1]))

            else:

                if not in_token:
                    token_start = i
                    in_token = True

        if in_token:
            tokens.append(Token(token_start, len(chars) - token_start, input[token_start: len(chars) - token_start]))

        return tokens

    def is_chinese(self, c: chr):
        uc = c

        return (uc >= 0x4E00 & uc <= 0x9FBF) or (uc >= 0x3400 & uc <= 0x4DBF)

    def is_japanese(self, c: chr):
        uc = c
        return (uc >= 0x3040 & uc <= 0x309F) or (uc >= 0x30A0 & uc <= 0x30FF) or (uc >= 0xFF66 & uc <= 0xFF9D)

    def is_korean(self, c: chr):
        uc = c
        return (uc >= 0xAC00 & uc <= 0xD7AF) or (uc >= 0x1100 & uc <= 0x11FF) or (uc >= 0x3130 & uc <= 0x318F) or \
               (uc >= 0xFFB0 & uc <= 0xFFDC)

    def is_cjk(self, c: chr):
        return self.is_chinese(c) or self.is_japanese(c) or self.is_korean(c)

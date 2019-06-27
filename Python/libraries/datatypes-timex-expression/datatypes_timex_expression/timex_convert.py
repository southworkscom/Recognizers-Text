from datatypes_timex_expression.english import convert_timex_to_string as english_convert_timex_to_string
from datatypes_timex_expression.english import convert_timex_set_to_string as english_convert_timex_set_to_string


class TimexConvert:

    @staticmethod
    def convert_timex_to_string(timex):
        return english_convert_timex_to_string(timex)

    @staticmethod
    def convert_timex_set_to_string(timex):
        return english_convert_timex_set_to_string(timex)

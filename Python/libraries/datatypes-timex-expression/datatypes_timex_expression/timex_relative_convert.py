from datatypes_timex_expression.english import convert_timex_to_string_relative as english_convert_timex_to_string_relative


class TimexRelativeConvert:

    @staticmethod
    def convert_timex_to_string_relative(timex, reference_date):
        return english_convert_timex_to_string_relative(timex, reference_date)

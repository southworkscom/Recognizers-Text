from recognizers_sequence.sequence.extractors import *
from recognizers_sequence.sequence.config.sequence_configuration import SequenceConfiguration


class PhoneNumberConfiguration(BaseSequenceExtractorConfiguration):

    def __init__(self, word_boundaries_regex, non_word_boundaries_regex, end_word_boundaries_regex, options: SequenceConfiguration):
        self.__word_boundaries_regex = word_boundaries_regex
        self.__non_word_boundaries_regex = non_word_boundaries_regex
        self.__end_word_boundaries_regex = end_word_boundaries_regex
        self.options = options

    @property
    def word_boundaries_regex(self) -> str:
        return self.__word_boundaries_regex

    @word_boundaries_regex.setter
    def word_boundaries_regex(self, word_boundaries_regex):
        self.__word_boundaries_regex = word_boundaries_regex

    @property
    def non_word_boundaries_regex(self) -> str:
        return self.__non_word_boundaries_regex

    @non_word_boundaries_regex.setter
    def non_word_boundaries_regex(self, non_word_boundaries_regex):
        self.__non_word_boundaries_regex = non_word_boundaries_regex

    @property
    def end_word_boundaries_regex(self) -> str:
        return self.__end_word_boundaries_regex

    @end_word_boundaries_regex.setter
    def end_word_boundaries_regex(self, end_word_boundaries_regex):
        self.__end_word_boundaries_regex = end_word_boundaries_regex

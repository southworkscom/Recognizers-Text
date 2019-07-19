from recognizers_sequence.sequence.extractors import *

class URLConfiguration(BaseSequenceExtractorConfiguration):

    def __init__(self,x):
        self.__x = x

    @property
    def get_ip_url_regex(self) -> str:
        return self.__word_boundaries_regex

    def set_get_word_boundaries_regex(self, word_boundaries_regex):
        self.__word_boundaries_regex = word_boundaries_regex

    @property
    def get_non_word_boundaries_regex(self) -> str:
        return self.__non_word_boundaries_regex

    def set_non_get_word_boundaries_regex(self, non_word_boundaries_regex):
        self.__non_word_boundaries_regex = non_word_boundaries_regex

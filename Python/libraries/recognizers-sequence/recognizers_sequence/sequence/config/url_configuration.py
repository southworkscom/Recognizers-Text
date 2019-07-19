from recognizers_sequence.sequence.extractors import *
from recognizers_sequence.sequence.config.sequence_configuration import SequenceConfiguration


class URLConfiguration(BaseSequenceExtractorConfiguration):

    def __init__(self, ip_url_regex, url_regex, options: SequenceConfiguration):
        self.__ip_url_regex = ip_url_regex
        self.__url_regex = url_regex
        self.options = options

    @property
    def ip_url_regex(self) -> str:
        return self.__ip_url_regex

    @ip_url_regex.setter
    def word_boundaries_regex(self, ip_url_regex):
        self.__ip_url_regex = ip_url_regex

    @property
    def url_regex(self) -> str:
        return self.__url_regex

    @url_regex.setter
    def url_regex(self, url_regex):
        self.__url_regex = url_regex

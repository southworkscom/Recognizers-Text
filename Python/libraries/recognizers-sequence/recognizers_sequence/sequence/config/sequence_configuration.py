from abc import ABC, abstractmethod

from recognizers_sequence import SequenceOptions


class SequenceConfiguration(ABC):

    @property
    @abstractmethod
    def options(self) -> SequenceOptions:
        raise NotImplementedError

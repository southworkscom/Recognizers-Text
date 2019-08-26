from abc import abstractmethod, ABC
from typing import List, Optional, Pattern, Dict
from datetime import datetime
from collections import namedtuple
import regex

from recognizers_text.extractor import Extractor, ExtractResult
from .constants import Constants, TimeTypeConstants
from .extractors import DateTimeExtractor
from .parsers import DateTimeParser, DateTimeParseResult
from .base_date import BaseDateParser
from .base_time import BaseTimeParser
from .base_datetime import BaseDateTimeParser
from .base_holiday import BaseHolidayParser
from .base_dateperiod import BaseDatePeriodParser
from .base_timeperiod import BaseTimePeriodParser
from .base_datetimeperiod import BaseDateTimePeriodParser
from .base_duration import BaseDurationParser
from .base_set import BaseSetParser
from .utilities import Token, merge_all_tokens, RegExpUtility, DateTimeOptions, DateTimeFormatUtil, DateUtils,\
    MatchingUtil, RegexExtension
from .datetime_zone_extractor import DateTimeZoneExtractor
from .datetime_list_extractor import DateTimeListExtractor

MatchedIndex = namedtuple('MatchedIndex', ['matched', 'index'])


class MergedExtractorConfiguration:
    @property
    @abstractmethod
    def date_extractor(self) -> DateTimeExtractor:
        raise NotImplementedError

    @property
    @abstractmethod
    def time_extractor(self) -> DateTimeExtractor:
        raise NotImplementedError

    @property
    @abstractmethod
    def date_time_extractor(self) -> DateTimeExtractor:
        raise NotImplementedError

    @property
    @abstractmethod
    def date_period_extractor(self) -> DateTimeExtractor:
        raise NotImplementedError

    @property
    @abstractmethod
    def time_period_extractor(self) -> DateTimeExtractor:
        raise NotImplementedError

    @property
    @abstractmethod
    def date_time_period_extractor(self) -> DateTimeExtractor:
        raise NotImplementedError

    @property
    @abstractmethod
    def duration_extractor(self) -> DateTimeExtractor:
        raise NotImplementedError

    @property
    @abstractmethod
    def set_extractor(self) -> DateTimeExtractor:
        raise NotImplementedError

    @property
    @abstractmethod
    def holiday_extractor(self) -> DateTimeExtractor:
        raise NotImplementedError

    @property
    @abstractmethod
    def time_zone_extractor(self) -> DateTimeZoneExtractor:
        raise NotImplementedError

    @property
    @abstractmethod
    def datetime_alt_extractor(self) -> DateTimeListExtractor:
        raise NotImplementedError

    @property
    @abstractmethod
    def integer_extractor(self) -> Extractor:
        raise NotImplementedError

    @property
    @abstractmethod
    def term_filter_regexes(self) -> List[Pattern]:
        raise NotImplementedError

    @property
    @abstractmethod
    def after_regex(self) -> Pattern:
        raise NotImplementedError

    @property
    @abstractmethod
    def before_regex(self) -> Pattern:
        raise NotImplementedError

    @property
    @abstractmethod
    def since_regex(self) -> Pattern:
        raise NotImplementedError

    @property
    @abstractmethod
    def around_regex(self) -> Pattern:
        raise NotImplementedError

    @property
    @abstractmethod
    def equal_regex(self) -> Pattern:
        raise NotImplementedError

    @property
    @abstractmethod
    def from_to_regex(self) -> Pattern:
        raise NotImplementedError

    @property
    @abstractmethod
    def single_ambiguous_month_regex(self) -> Pattern:
        raise NotImplementedError

    @property
    @abstractmethod
    def preposition_suffix_regex(self) -> Pattern:
        raise NotImplementedError

    @property
    @abstractmethod
    def number_ending_pattern(self) -> Pattern:
        raise NotImplementedError

    @property
    @abstractmethod
    def suffix_after_regex(self) -> Pattern:
        raise NotImplementedError

    @property
    @abstractmethod
    def unspecified_date_period_regex(self) -> Pattern:
        raise NotImplementedError

    @property
    @abstractmethod
    def fail_fast_regex(self) -> Pattern:
        raise NotImplementedError

    @property
    @abstractmethod
    def superfluous_word_matcher(self) -> Pattern:
        raise NotImplementedError

    @property
    @abstractmethod
    def ambiguity_filters_dict(self) -> Pattern:
        raise NotImplementedError

    @property
    @abstractmethod
    def filter_word_regex_list(self) -> List[Pattern]:
        raise NotImplementedError


class BaseMergedExtractor(DateTimeExtractor):
    @property
    def extractor_type_name(self) -> str:
        return Constants.SYS_DATETIME_MERGED

    def __init__(self, config: MergedExtractorConfiguration, options: DateTimeOptions):
        self.config = config
        self.options = options

    def has_token_index(self, source: str, pattern: Pattern) -> MatchedIndex:

        # Support cases has two or more specific tokens
        # For example, "show me sales after 2010 and before 2018 or before 2000"
        # When extract "before 2000", we need the second "before" which will be
        # matched in the second Regex match
        match = regex.search(pattern, source)
        if match:
            return MatchedIndex(True, match.start())

        return MatchedIndex(False, -1)
        # def has_token_index(text, regex_eval: Pattern):

        #   match = regex.search('(?r)' + str(regex_eval), text)

        #  if match and match is not None and not str.isspace(text[match.index: len(match) + match.index]):
        #     index = match.index
        #    return True, index

        # return False

    def try_merge_modifier_token(self, er: ExtractResult, token_regex: Pattern, text: str):
        start = er.start if er.start else 0
        before_str = text[0:start]

        if self.has_token_index(before_str.rstrip(), token_regex).matched:
            boolean, token_index = self.has_token_index(before_str.rstrip(), token_regex)

            mod_length = len(before_str) - token_index

            er.length += mod_length
            er.start -= mod_length
            start = er.start if er.start else 0
            length = er.length if er.length else 0
            er.text = text[start: start + length]
            return True

        return False

    def extract(self, source: str, reference: datetime = None) -> List[ExtractResult]:
        if reference is None:
            reference = datetime.now()

        result: List[ExtractResult] = list()

        if (self.options & DateTimeOptions.FAIL_FAST) != 0 and self.is_fail_fast_case(source):

            ''' @TODO needs better handling of holidays and timezones.
             self.add_to(result,self.config.holiday_extractor.extract(source,reference), source)
             result = self.add_mod(result,source)
            '''
            return result

        origin_text = source

        super_fluous_word_matches = None

        if (self.options & DateTimeOptions.ENABLE_PREVIEW) != 0:
            source, super_fluous_word_matches = MatchingUtil.pre_process_text_remove_superfluous_words(
                source,
                self.config.superfluous_word_matcher
            )
        # The order is important, since there can be conflicts in merging
        result = self.add_to(
            result, self.config.date_extractor.extract(source, reference), source)
        result = self.add_to(
            result, self.config.time_extractor.extract(source, reference), source)
        result = self.add_to(
            result, self.config.date_period_extractor.extract(source, reference), source)
        result = self.add_to(
            result, self.config.duration_extractor.extract(source, reference), source)
        result = self.add_to(
            result, self.config.time_period_extractor.extract(source, reference), source)
        result = self.add_to(result, self.config.date_time_period_extractor.extract(
            source, reference), source)
        result = self.add_to(
            result, self.config.date_time_extractor.extract(source, reference), source)
        result = self.add_to(
            result, self.config.set_extractor.extract(source, reference), source)
        result = self.add_to(
            result, self.config.holiday_extractor.extract(source, reference), source)

        if (self.options & DateTimeOptions.ENABLE_PREVIEW) != 0:
            self.add_to(result, self.config.time_zone_extractor.extract(source, reference), source)
            result = self.config.time_zone_extractor.remove_ambiguous_time_zone(result)

        # this should be at the end since if need the extractor to determine the previous text contains time or not
        result = self.add_to(
            result, self.number_ending_regex_match(source, result), source)

        # Modify time entity to an alternative DateTime expression if it follows a DateTime entity
        if (self.options & DateTimeOptions.EXTENDED_TYPES) != 0:
            result = self.config.datetime_alt_extractor.extract(result, source, reference)

        result = self.filter_unespecific_date_period(result)

        # Remove common ambiguous cases
        result = self._filter_ambiguity(result, source)

        result = self.add_mod(result, source)

        # filtering
        if self.options & DateTimeOptions.CALENDAR:
            result = self.check_calendar_filter_list(result, source)

        result = sorted(result, key=lambda x: x.start)

        if (self.options & DateTimeOptions.ENABLE_PREVIEW) != 0:
            result = MatchingUtil.post_process_recover_superfluous_words(result, super_fluous_word_matches, origin_text)

        return result

    def add_to(self, destination: List[ExtractResult], source: List[ExtractResult], text: str) -> List[ExtractResult]:
        for value in source:
            if self.options & DateTimeOptions.SKIP_FROM_TO_MERGE and self.should_skip_from_merge(value):
                continue
            is_found = False
            overlap_indexes: List[int] = list()
            first_index = -1

            for index, dest in enumerate(destination):
                if dest.overlap(value):
                    is_found = True
                    if dest.cover(value):
                        if first_index == -1:
                            first_index = index
                        overlap_indexes.append(index)
                    else:
                        continue

            if not is_found:
                destination.append(value)
            elif overlap_indexes:
                temp_dst: List[ExtractResult] = list()

                for index, dest in enumerate(destination):
                    if index not in overlap_indexes:
                        temp_dst.append(dest)

                # insert at the first overlap occurence to keep the order
                temp_dst.insert(first_index, value)
                destination = temp_dst
        return destination

    def should_skip_from_merge(self, source: ExtractResult) -> bool:
        return regex.search(self.config.from_to_regex, source.text)

    def is_fail_fast_case(self, input: str):
        return self.config.fail_fast_regex is not None and not self.config.fail_fast_regex.finditer(input)

    def filter_unespecific_date_period(self, ers: List[ExtractResult]) -> List[ExtractResult]:
        for e in ers:
            if self.config.unspecified_date_period_regex.search(e.text) is not None:
                ers.remove(e)

        return ers

    def _filter_ambiguity(self, ers: List[ExtractResult], text: str, ) -> List[ExtractResult]:

        if self.config.ambiguity_filters_dict is not None:
            for regex_var in self.config.ambiguity_filters_dict:
                regexvar_value = self.config.ambiguity_filters_dict[regex_var]

                try:
                    reg_len = list(filter(lambda x: x.group(), regex.finditer(regexvar_value, text)))

                    reg_length = len(reg_len)
                    if reg_length > 0:

                        matches = reg_len
                        new_ers = list(filter(lambda x: list(
                            filter(lambda m: m.start() < x.start + x.length and m.start() +
                                   len(m.group()) > x.start, matches)), ers))
                        if len(new_ers) > 0:
                            for item in ers:
                                for i in new_ers:
                                    if item is i:
                                        ers.remove(item)
                except Exception:
                    pass

        return ers

    def number_ending_regex_match(self, source: str, extract_results: List[ExtractResult]) -> List[ExtractResult]:
        tokens: List[Token] = list()

        for extract_result in extract_results:
            if extract_result.type in [Constants.SYS_DATETIME_TIME, Constants.SYS_DATETIME_DATETIME]:
                after_str = source[extract_result.start +
                                   extract_result.length:]
                match = regex.search(
                    self.config.number_ending_pattern, after_str)
                if match:
                    new_time = RegExpUtility.get_group(match, 'newTime')
                    num_res = self.config.integer_extractor.extract(new_time)
                    if not num_res:
                        continue

                    start_position = extract_result.start + \
                        extract_result.length + match.group().index(new_time)
                    tokens.append(
                        Token(start_position, start_position + len(new_time)))

        return merge_all_tokens(tokens, source, Constants.SYS_DATETIME_TIME)

    def add_mod(self, ers: List[ExtractResult], source: str) -> List[ExtractResult]:

        for er in ers:
            success = self.try_merge_modifier_token(er, self.config.before_regex, source)

            if not success:
                success = self.try_merge_modifier_token(er, self.config.after_regex, source)

            if not success:
                success = self.try_merge_modifier_token(er, self.config.since_regex, source)

            if not success:
                self.try_merge_modifier_token(er, self.config.around_regex, source)

            if not success:
                self.try_merge_modifier_token(er, self.config.equal_regex, source)

            if er.type == Constants.SYS_DATETIME_DATEPERIOD or \
                er.type == Constants.SYS_DATETIME_DATE or \
                    er.type == Constants.SYS_DATETIME_TIME:

                start = er.start if er.start else 0
                length = er.length if er.length else 0
                after_str = source[start + length:]

                match = RegexExtension.match_begin(self.config.suffix_after_regex, after_str, True)

                if match:
                    is_followed_by_other_entity = True

                    if match.length == len(after_str.strip()):
                        is_followed_by_other_entity = False
                    else:
                        next_str = after_str.strip()[match.length].strip()
                        next_er = next((e for e in ers if e.start > er.start), None)

                        if next_er is None or not next_str.startswith(next_er.text):
                            is_followed_by_other_entity = False

                    if not is_followed_by_other_entity:
                        mod_length = match.length + after_str.index(match.value)
                        er.length += mod_length
                        start = er.start if er.start else 0
                        length = er.length if er.length else 0
                        er.text = source[start: start + length]

        return ers

    def add_mod_item(self, er: ExtractResult, source: str) -> ExtractResult:
        before_str = source[0:er.start]
        is_success = False

        before = self.has_token_index(
            before_str.strip(), self.config.before_regex)
        if before.matched:
            mod_len = len(before_str) - before.index
            er.length += mod_len
            er.start -= mod_len
            er.text = source[er.start:er.start + er.length]
            is_success = True

        if not is_success:
            after = self.has_token_index(
                before_str.strip(), self.config.after_regex)
            if after.matched:
                mod_len = len(before_str) - after.index
                er.length += mod_len
                er.start -= mod_len
                er.text = source[er.start:er.start + er.length]
                is_success = True

        if not is_success:
            since = self.has_token_index(
                before_str.strip(), self.config.since_regex)
            if not is_success and since.matched:
                mod_len = len(before_str) - since.index
                er.length += mod_len
                er.start -= mod_len
                er.text = source[er.start:er.start + er.length]

        return er

    def check_calendar_filter_list(self, ers: List[ExtractResult], source: str) -> List[ExtractResult]:
        for er in reversed(ers):
            for pattern in self.config.filter_word_regex_list:
                if regex.search(pattern, er.text):
                    ers.remove(er)
                    break

        return ers


class MergedParserConfiguration(ABC):
    @property
    @abstractmethod
    def before_regex(self) -> Pattern:
        raise NotImplementedError

    @property
    @abstractmethod
    def after_regex(self) -> Pattern:
        raise NotImplementedError

    @property
    @abstractmethod
    def since_regex(self) -> Pattern:
        raise NotImplementedError

    @property
    @abstractmethod
    def date_parser(self) -> BaseDateParser:
        raise NotImplementedError

    @property
    @abstractmethod
    def holiday_parser(self) -> BaseHolidayParser:
        raise NotImplementedError

    @property
    @abstractmethod
    def time_parser(self) -> BaseTimeParser:
        raise NotImplementedError

    @property
    @abstractmethod
    def date_time_parser(self) -> BaseDateTimeParser:
        raise NotImplementedError

    @property
    @abstractmethod
    def date_period_parser(self) -> BaseDatePeriodParser:
        raise NotImplementedError

    @property
    @abstractmethod
    def time_period_parser(self) -> BaseTimePeriodParser:
        raise NotImplementedError

    @property
    @abstractmethod
    def date_time_period_parser(self) -> BaseDateTimePeriodParser:
        raise NotImplementedError

    @property
    @abstractmethod
    def duration_parser(self) -> BaseDurationParser:
        raise NotImplementedError

    @property
    @abstractmethod
    def set_parser(self) -> BaseSetParser:
        raise NotImplementedError


class BaseMergedParser(DateTimeParser):
    @property
    def parser_type_name(self) -> str:
        return Constants.SYS_DATETIME_MERGED

    def __init__(self, config: MergedParserConfiguration, options: DateTimeOptions):
        self.__date_min_value = DateTimeFormatUtil.format_date(
            DateUtils.min_value)
        self.__date_time_min_value = DateTimeFormatUtil.format_date_time(
            DateUtils.min_value)
        self.config = config
        self.options = options

    def parse(self, source: ExtractResult, reference: datetime = None) -> Optional[DateTimeParseResult]:
        if not reference:
            reference = datetime.now()

        has_before = False
        has_after = False
        has_since = False
        mod_str = ''
        before_match = self.config.before_regex.match(source.text)
        after_match = self.config.after_regex.match(source.text)
        since_match = self.config.since_regex.match(source.text)

        if before_match:
            has_before = True
            source.start += before_match.end()
            source.length -= before_match.end()
            source.text = source.text[before_match.end():]
            mod_str = before_match.group()
        elif after_match:
            has_after = True
            source.start += after_match.end()
            source.length -= after_match.end()
            source.text = source.text[after_match.end():]
            mod_str = after_match.group()
        elif since_match:
            has_since = True
            source.start += since_match.end()
            source.length -= since_match.end()
            source.text = source.text[since_match.end():]
            mod_str = since_match.group()

        if source.type == Constants.SYS_DATETIME_DATE:
            result = self.config.date_parser.parse(source, reference)
            if not result.value:
                result = self.config.holiday_parser.parse(source, reference)
        elif source.type == Constants.SYS_DATETIME_TIME:
            result = self.config.time_parser.parse(source, reference)
        elif source.type == Constants.SYS_DATETIME_DATETIME:
            result = self.config.date_time_parser.parse(source, reference)
        elif source.type == Constants.SYS_DATETIME_DATEPERIOD:
            result = self.config.date_period_parser.parse(source, reference)
        elif source.type == Constants.SYS_DATETIME_TIMEPERIOD:
            result = self.config.time_period_parser.parse(source, reference)
        elif source.type == Constants.SYS_DATETIME_DATETIMEPERIOD:
            result = self.config.date_time_period_parser.parse(
                source, reference)
        elif source.type == Constants.SYS_DATETIME_DURATION:
            result = self.config.duration_parser.parse(source, reference)
        elif source.type == Constants.SYS_DATETIME_SET:
            result = self.config.set_parser.parse(source, reference)
        else:
            return None

        if has_before and result.value:
            result.length += len(mod_str)
            result.start -= len(mod_str)
            result.text = mod_str + result.text
            val = result.value
            val.mod = TimeTypeConstants.BEFORE_MOD
            result.value = val

        if has_after and result.value:
            result.length += len(mod_str)
            result.start -= len(mod_str)
            result.text = mod_str + result.text
            val = result.value
            val.mod = TimeTypeConstants.AFTER_MOD
            result.value = val

        if has_since and result.value:
            result.length += len(mod_str)
            result.start -= len(mod_str)
            result.text = mod_str + result.text
            val = result.value
            val.mod = TimeTypeConstants.SINCE_MOD
            result.value = val

        if self.options & DateTimeOptions.SPLIT_DATE_AND_TIME and result.value and result.value.sub_date_time_entities:
            result.value = self._date_time_resolution_for_split(result)
        else:
            result = self.set_parse_result(
                result, has_before, has_after, has_since)

        return result

    def set_parse_result(self, slot: DateTimeParseResult, has_before: bool, has_after: bool, has_since: bool) -> DateTimeParseResult:
        slot.value = self._date_time_resolution(
            slot, has_before, has_after, has_since)
        slot.type = f'{self.parser_type_name}.{self._determine_date_time_types(slot.type, has_before, has_after, has_since)}'
        return slot

    def _get_parse_result(self, extractor_result: Extractor, reference: datetime) -> DateTimeParseResult:
        extractor_type = extractor_result.type
        if extractor_type == Constants.SYS_DATETIME_DATE:
            result = self.config.date_parser.parse(extractor_result, reference)
            if not result.value:
                result = self.config.holiday_parser.parse(
                    extractor_result, reference)
            return result
        elif extractor_type == Constants.SYS_DATETIME_TIME:
            return self.config.time_parser.parse(extractor_result, reference)
        elif extractor_type == Constants.SYS_DATETIME_DATETIME:
            return self.config.date_time_parser.parse(extractor_result, reference)
        elif extractor_type == Constants.SYS_DATETIME_DATEPERIOD:
            return self.config.date_period_parser.parse(extractor_result, reference)
        elif extractor_type == Constants.SYS_DATETIME_TIMEPERIOD:
            return self.config.time_period_parser.parse(extractor_result, reference)
        elif extractor_type == Constants.SYS_DATETIME_DATETIMEPERIOD:
            return self.config.date_time_period_parser.parse(extractor_result, reference)
        elif extractor_type == Constants.SYS_DATETIME_DURATION:
            return self.config.duration_parser.parse(extractor_result, reference)
        elif extractor_type == Constants.SYS_DATETIME_SET:
            return self.config.set_parser.parse(extractor_result, reference)
        else:
            return None

    def _determine_date_time_types(self, dtype: str, has_before: bool, has_after: bool, has_since: bool) -> str:
        if self.options & DateTimeOptions.SPLIT_DATE_AND_TIME:
            if dtype == Constants.SYS_DATETIME_DATETIME:
                return Constants.SYS_DATETIME_TIME
        else:
            if has_before or has_after or has_since:
                if dtype == Constants.SYS_DATETIME_DATE:
                    return Constants.SYS_DATETIME_DATEPERIOD

                if dtype == Constants.SYS_DATETIME_TIME:
                    return Constants.SYS_DATETIME_TIMEPERIOD

                if dtype == Constants.SYS_DATETIME_DATETIME:
                    return Constants.SYS_DATETIME_DATETIMEPERIOD
        return dtype

    def _determine_source_entity_type(self, source_type: str, new_type: str, has_mod: bool) -> Optional[str]:
        if not has_mod:
            return None

        if new_type != source_type:
            return Constants.SYS_DATETIME_DATETIMEPOINT

        if new_type == Constants.SYS_DATETIME_DATEPERIOD:
            return Constants.SYS_DATETIME_DATETIMEPERIOD

    def _date_time_resolution_for_split(self, slot: DateTimeParseResult) -> List[DateTimeParseResult]:
        results = []
        if slot.value.sub_date_time_entities:
            sub_entities = slot.value.sub_date_time_entities

            for sub_entity in sub_entities:
                result = sub_entity
                result.start += slot.start
                results += self._date_time_resolution_for_split(result)
        else:
            slot.value = self._date_time_resolution(slot, False, False, False)
            slot.type = f'{self.parser_type_name}.{self._determine_date_time_types(slot.type, False, False, False)}'
            results.append(slot)

        return results

    def _date_time_resolution(self, slot: DateTimeParseResult, has_before, has_after, has_since) -> List[Dict[str, str]]:
        if not slot:
            return None

        result = dict()
        resolutions = []

        dtype = slot.type
        output_type = self._determine_date_time_types(dtype, has_before, has_after, has_since)
        source_entity = self._determine_source_entity_type(dtype, output_type, has_before or has_after or has_since)

        timex = slot.timex_str

        value = slot.value
        if not value:
            return None

        is_lunar = value.is_lunar
        mod = value.mod
        comment = value.comment

        self._add_resolution_fields_any(result, Constants.TimexKey, timex)
        self._add_resolution_fields_any(result, Constants.CommentKey, comment)
        self._add_resolution_fields_any(result, Constants.ModKey, mod)
        self._add_resolution_fields_any(result, Constants.TypeKey, output_type)
        self._add_resolution_fields_any(
            result, Constants.IsLunarKey, str(is_lunar).lower() if is_lunar else '')

        future_resolution = value.future_resolution
        past_resolution = value.past_resolution

        future = self._generate_from_resolution(dtype, future_resolution, mod)
        past = self._generate_from_resolution(dtype, past_resolution, mod)

        future_values = sorted(future.values())
        past_values = sorted(past.values())
        intersect_values = [i for i, j in zip(
            future_values, past_values) if i == j]

        if len(intersect_values) == len(past_values) and len(intersect_values) == len(future_values):
            if past_values:
                self._add_resolution_fields_any(
                    result, Constants.ResolveKey, past)
        else:
            if past_values:
                self._add_resolution_fields_any(
                    result, Constants.ResolveToPastKey, past)
            if future_resolution:
                self._add_resolution_fields_any(
                    result, Constants.ResolveToFutureKey, future)

        if comment == 'ampm':
            if Constants.ResolveKey in result:
                self._resolve_ampm(result, Constants.ResolveKey)
            else:
                self._resolve_ampm(result, Constants.ResolveToPastKey)
                self._resolve_ampm(result, Constants.ResolveToFutureKey)

        for value in result.values():
            if isinstance(value, dict):
                new_values = {}
                self._add_resolution_fields(
                    new_values, Constants.TimexKey, timex)
                self._add_resolution_fields(new_values, Constants.ModKey, mod)

                self._add_resolution_fields(new_values, Constants.TypeKey, output_type)
                self._add_resolution_fields(new_values, Constants.IsLunarKey, str(is_lunar).lower() if is_lunar else '')
                self._add_resolution_fields(new_values, Constants.SourceType, source_entity)

                for inner_key in value:
                    new_values[inner_key] = value[inner_key]

                resolutions.append(new_values)

        if not past and not future:
            o = {}
            o['timex'] = timex
            o['type'] = output_type
            o['value'] = 'not resolved'
            resolutions.append(o)

        return {'values': resolutions}

    def _add_resolution_fields_any(self, dic: Dict[str, str], key: str, value: object):
        if isinstance(value, str):
            if value:
                dic[key] = value
        else:
            dic[key] = value

    def _add_resolution_fields(self, dic: [str, str], key: str, value: str):
        if value:
            dic[key] = value

    def _generate_from_resolution(self, dtype: str, resolution: Dict[str, str], mod: str) -> Dict[str, str]:
        result = {}

        if dtype == Constants.SYS_DATETIME_DATETIME:
            self.__add_single_date_time_to_resolution(
                resolution, TimeTypeConstants.DATETIME, mod, result)
        elif dtype == Constants.SYS_DATETIME_TIME:
            self.__add_single_date_time_to_resolution(
                resolution, TimeTypeConstants.TIME, mod, result)
        elif dtype == Constants.SYS_DATETIME_DATE:
            self.__add_single_date_time_to_resolution(
                resolution, TimeTypeConstants.DATE, mod, result)
        elif dtype == Constants.SYS_DATETIME_DURATION:
            if TimeTypeConstants.DURATION in resolution:
                result[TimeTypeConstants.VALUE] = resolution[TimeTypeConstants.DURATION]

        if dtype == Constants.SYS_DATETIME_TIMEPERIOD:
            self.__add_period_to_resolution(
                resolution, TimeTypeConstants.START_TIME, TimeTypeConstants.END_TIME, mod, result)

        if dtype == Constants.SYS_DATETIME_DATEPERIOD:
            self.__add_period_to_resolution(
                resolution, TimeTypeConstants.START_DATE, TimeTypeConstants.END_DATE, mod, result)

        if dtype == Constants.SYS_DATETIME_DATETIMEPERIOD:
            self.__add_period_to_resolution(
                resolution, TimeTypeConstants.START_DATETIME, TimeTypeConstants.END_DATETIME, mod, result)

        return result

    def __add_single_date_time_to_resolution(self, resolutions: Dict[str, str], dtype: str, mod: str, result: Dict[str, str]):
        key = TimeTypeConstants.VALUE
        value = resolutions[dtype]
        if not value or self.__date_min_value == value or self.__date_time_min_value == value:
            return

        if mod:
            if mod == TimeTypeConstants.BEFORE_MOD:
                key = TimeTypeConstants.END
            elif mod == TimeTypeConstants.AFTER_MOD:
                key = TimeTypeConstants.START
            elif mod == TimeTypeConstants.SINCE_MOD:
                key = TimeTypeConstants.START

        result[key] = value

    def __add_period_to_resolution(self, resolutions: Dict[str, str], start_type: str, end_type: str, mod: str, result: Dict[str, str]):
        start = resolutions.get(start_type, None)
        end = resolutions.get(end_type, None)
        if mod:
            if mod == TimeTypeConstants.BEFORE_MOD:
                result[TimeTypeConstants.END] = start
                return
            if mod == TimeTypeConstants.AFTER_MOD:
                result[TimeTypeConstants.START] = end
                return
            if mod == TimeTypeConstants.SINCE_MOD:
                result[TimeTypeConstants.START] = start
                return

        if not (start and end):
            return

        result[TimeTypeConstants.START] = start
        result[TimeTypeConstants.END] = end

    def _resolve_ampm(self, values_map: Dict[str, str], keyname: str):
        if keyname not in values_map:
            return
        resolution = values_map[keyname]
        if Constants.TimexKey not in values_map:
            return
        timex = values_map['timex']
        values_map.pop(keyname, None)
        values_map[keyname + 'Am'] = resolution

        resolution_pm = {}
        if values_map['type'] == Constants.SYS_DATETIME_TIME:
            resolution_pm[TimeTypeConstants.VALUE] = DateTimeFormatUtil.to_pm(
                resolution[TimeTypeConstants.VALUE])
            resolution_pm['timex'] = DateTimeFormatUtil.to_pm(timex)
        elif values_map['type'] == Constants.SYS_DATETIME_DATETIME:
            split_value = resolution[TimeTypeConstants.VALUE].split(' ')
            resolution_pm[
                TimeTypeConstants.VALUE] = f'{split_value[0]} {DateTimeFormatUtil.to_pm(split_value[1])}'
            resolution_pm['timex'] = DateTimeFormatUtil.all_str_to_pm(timex)
        elif values_map['type'] == Constants.SYS_DATETIME_TIMEPERIOD:
            if TimeTypeConstants.START in resolution:
                resolution_pm[TimeTypeConstants.START] = DateTimeFormatUtil.to_pm(
                    resolution[TimeTypeConstants.START])
            if TimeTypeConstants.END in resolution:
                resolution_pm[TimeTypeConstants.END] = DateTimeFormatUtil.to_pm(
                    resolution[TimeTypeConstants.END])
            resolution_pm['timex'] = DateTimeFormatUtil.all_str_to_pm(timex)
        elif values_map['type'] == Constants.SYS_DATETIME_DATETIMEPERIOD:
            if TimeTypeConstants.START in resolution:
                split_value = resolution[TimeTypeConstants.START].split(' ')
                resolution_pm[
                    TimeTypeConstants.START] = f'{split_value[0]} {DateTimeFormatUtil.to_pm(split_value[1])}'
            if TimeTypeConstants.END in resolution:
                split_value = resolution[TimeTypeConstants.END].split(' ')
                resolution_pm[
                    TimeTypeConstants.END] = f'{split_value[0]} {DateTimeFormatUtil.to_pm(split_value[1])}'
            resolution_pm['timex'] = DateTimeFormatUtil.all_str_to_pm(timex)
        values_map[keyname + 'Pm'] = resolution_pm

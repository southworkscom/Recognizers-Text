package com.microsoft.recognizers.text.datetime.utilities;

import com.google.common.collect.ImmutableMap;
import com.microsoft.recognizers.text.ExtractResult;
import com.microsoft.recognizers.text.datetime.Constants;
import com.microsoft.recognizers.text.datetime.extractors.IDateTimeExtractor;
import com.microsoft.recognizers.text.datetime.parsers.DateTimeParseResult;
import com.microsoft.recognizers.text.datetime.parsers.IDateTimeParser;
import com.microsoft.recognizers.text.datetime.parsers.config.IDateParserConfiguration;
import com.microsoft.recognizers.text.utilities.Match;
import com.microsoft.recognizers.text.utilities.RegExpUtility;
import com.microsoft.recognizers.text.utilities.StringUtility;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class AgoLaterUtil {
    public static DateTimeResolutionResult parseDurationWithAgoAndLater(String text,
                                                                        LocalDateTime referenceTime,
                                                                        IDateTimeExtractor durationExtractor,
                                                                        IDateTimeParser durationParser,
                                                                        ImmutableMap<String, String> unitMap,
                                                                        Pattern unitRegex,
                                                                        IDateTimeUtilityConfiguration utilityConfiguration,
                                                                        IDateParserConfiguration config) {
        DateTimeResolutionResult ret = new DateTimeResolutionResult();
        List<ExtractResult> durationRes = durationExtractor.extract(text, referenceTime);
        if (durationRes.size() > 0) {
            DateTimeParseResult pr = durationParser.parse(durationRes.get(0), referenceTime);
            Match[] matches = RegExpUtility.getMatches(unitRegex, text);
            if (matches.length > 0) {
                String afterStr =
                        text.substring(durationRes.get(0).start + durationRes.get(0).length)
                                .trim().toLowerCase();

                String beforeStr =
                        text.substring(0, durationRes.get(0).start)
                                .trim().toLowerCase();

                AgoLaterMode mode = AgoLaterMode.DATE;
                if (pr.timexStr.contains("T"))
                {
                    mode = AgoLaterMode.DATETIME;
                }

                if (pr.value != null)
                {
                    return getAgoLaterResult(pr, afterStr, beforeStr, referenceTime,
                            utilityConfiguration, mode, config);
                }
            }
        }
        return ret;
    }

    private static DateTimeResolutionResult getAgoLaterResult(
            DateTimeParseResult durationParseResult,
            String afterStr,
            String beforeStr,
            LocalDateTime referenceTime,
            IDateTimeUtilityConfiguration utilityConfiguration,
            AgoLaterMode mode,
            IDateParserConfiguration config)
    {
        DateTimeResolutionResult ret = new DateTimeResolutionResult();
        LocalDateTime resultDateTime = referenceTime;
        String timex = durationParseResult.timexStr;

        if (((DateTimeResolutionResult)durationParseResult.value).getMod() == Constants.MORE_THAN_MOD)
        {
            ret.setMod(Constants.MORE_THAN_MOD);
        }
        else if (((DateTimeResolutionResult)durationParseResult.value).getMod() == Constants.LESS_THAN_MOD)
        {
            ret.setMod(Constants.LESS_THAN_MOD);
        }

        if (MatchingUtil.ContainsAgoLaterIndex(afterStr, utilityConfiguration.getAgoRegex()))
        {
            Optional<Match> match = Arrays.stream(RegExpUtility.getMatches(utilityConfiguration.getAgoRegex(), afterStr)).findFirst();
            int swift = 0;

            // Handle cases like "3 days before yesterday"
            if (match.isPresent() && !StringUtility.isNullOrEmpty(match.get().getGroup("day").value))
            {
                swift = config.getSwiftDay(match.get().getGroup("day").value);
            }

            resultDateTime = DurationParsingUtil.shiftDateTime(timex, referenceTime.plusDays(swift), false);

            ((DateTimeResolutionResult)durationParseResult.value).setMod(Constants.BEFORE_MOD);
        }
        else if (MatchingUtil.ContainsAgoLaterIndex(afterStr, utilityConfiguration.getLaterRegex()) ||
                MatchingUtil.ContainsTermIndex(beforeStr, utilityConfiguration.getInConnectorRegex()))
        {
            Optional<Match> match = Arrays.stream(RegExpUtility.getMatches(utilityConfiguration.getLaterRegex(), afterStr)).findFirst();
            int swift = 0;

            // Handle cases like "3 days after tomorrow"
            if (match.isPresent() && !StringUtility.isNullOrEmpty(match.get().getGroup("day").value))
            {
                swift = config.getSwiftDay(match.get().getGroup("day").value);
            }

            resultDateTime = DurationParsingUtil.shiftDateTime(timex, referenceTime.plusDays(swift), true);

            ((DateTimeResolutionResult)durationParseResult.value).setMod(Constants.AFTER_MOD);
        }

        if (resultDateTime != referenceTime)
        {
            if (mode.equals(AgoLaterMode.DATE))
            {
                ret.setTimex(FormatUtil.luisDate(resultDateTime));
            }
            else if (mode.equals(AgoLaterMode.DATETIME))
            {
                ret.setTimex(FormatUtil.luisDateTime(resultDateTime));
            }

            ret.setFutureValue(resultDateTime);
            ret.setPastValue(resultDateTime);

            List<Object> subDateTimeEntities = new ArrayList<>();
            subDateTimeEntities.add(durationParseResult);

            ret.setSubDateTimeEntities(subDateTimeEntities);
            ret.setSuccess(true);
        }

        return ret;
    }

    public enum AgoLaterMode
    {
        DATE,
        DATETIME
    }
}

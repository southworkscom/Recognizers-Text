package com.microsoft.recognizers.text.datetime.utilities;

import com.google.common.collect.ImmutableMap;


import java.time.LocalDateTime;
import java.util.Map;

public class DurationParsingUtil {
    public static LocalDateTime shiftDateTime(String timex, LocalDateTime referenceDateTime, Boolean future) {
        ImmutableMap<String, Double> timexUnitMap = resolveDurationTimex(timex);
        LocalDateTime ret = getShiftResult(timexUnitMap, referenceDateTime, future);
        return ret;
    }

    private static LocalDateTime getShiftResult(ImmutableMap<String, Double> timexUnitMap, LocalDateTime referenceDate, Boolean future) {
        LocalDateTime ret = referenceDate;
        int futureOrPast = future ? 1 : -1;

        for (Map.Entry<String, Double> pair : timexUnitMap.entrySet()) {
            String unitStr = pair.getKey();
            Double number = pair.getValue();
            switch (unitStr)
            {
                case "H":
                    ret = ret.plusHours(Double.valueOf(number * futureOrPast).longValue());
                    break;
                case "M":
                    ret = ret.plusMinutes(Double.valueOf(number * futureOrPast).longValue());
                    break;
                case "S":
                    ret = ret.plusSeconds(Double.valueOf(number * futureOrPast).longValue());
                    break;
                case "D":
                    ret = ret.plusDays(Double.valueOf(number * futureOrPast).longValue());
                    break;
                case "W":
                    ret = ret.plusDays(Double.valueOf(7 * number * futureOrPast).longValue());
                    break;
                case "MON":
                    ret = ret.plusMonths(Double.valueOf(number).intValue() * futureOrPast);
                    break;
                case "Y":
                    ret = ret.plusYears(Double.valueOf(number).intValue() * futureOrPast);
                    break;
                default:
                    return ret;
            }
        }

        return ret;
    }

    private static ImmutableMap<String, Double> resolveDurationTimex(String timexStr) {
        ImmutableMap.Builder<String, Double> ret = ImmutableMap.builder();
        // resolve duration timex, such as P21DT2H(21 days 2 hours)
        String durationStr = timexStr.replace("P", "");
        int numberStart = 0;
        Boolean isTime = false;
        for (int idx = 0; idx < durationStr.length(); idx++)
        {
            if (Character.isLetter(durationStr.indexOf(idx)))
            {
                if (durationStr.indexOf(idx) == 'T')
                {
                    isTime = true;
                }
                else
                {
                    String numStr = durationStr.substring(numberStart, idx - numberStart);
                    double number;

                    try {
                        number = Double.parseDouble(numStr);
                    } catch(NumberFormatException ex) {
                        return (ImmutableMap.<String, Double>builder()).build();
                    }

                    String srcTimexUnit = durationStr.substring(idx, 1);
                    if (!isTime && srcTimexUnit == "M")
                    {
                        srcTimexUnit = "MON";
                    }
                    ret.put(srcTimexUnit, number);
                }
                numberStart = idx + 1;
            }
        }
        return ret.build();
    }
}

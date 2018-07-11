package com.microsoft.recognizers.text.datetime.extractors;

import com.microsoft.recognizers.text.ExtractResult;
import com.microsoft.recognizers.text.datetime.Constants;
import com.microsoft.recognizers.text.datetime.extractors.config.IDateExtractorConfiguration;
import com.microsoft.recognizers.text.utilities.Match;
import com.microsoft.recognizers.text.utilities.StringUtility;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class BaseDateExtractor implements IDateTimeExtractor {

    private final IDateExtractorConfiguration config;

    @Override
    public String getExtractorName() {
        return Constants.SYS_DATETIME_DATE;
    }

    public BaseDateExtractor(IDateExtractorConfiguration config) {
        this.config = config;
    }

    @Override
    public List<ExtractResult> extract(String input, LocalDateTime reference) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<ExtractResult> extract(String input) {
        return this.extract(input, LocalDateTime.now());
    }

    public int getYearFromText(Optional<Match> match) {
        int year = Constants.InvalidYear;

        String yearStr = match.get().getGroup("year").value;
        if (!StringUtility.isNullOrEmpty(yearStr)) {
            year = Integer.parseInt(yearStr);
            if (year < 100 && year >= Constants.MinTwoDigitYearPastNum) {
                year += 1900;
            } else if (year >= 0 && year < Constants.MaxTwoDigitYearFutureNum) {
                year += 2000;
            }
        } else {
            String firstTwoYearNumStr = match.get().getGroup("firsttwoyearnum").value;
            if (!StringUtility.isNullOrEmpty(firstTwoYearNumStr)) {
                int start = match.get().getGroup("firsttwoyearnum").index;
                int length = match.get().getGroup("firsttwoyearnum").length;

                ExtractResult er = new ExtractResult(start, length, firstTwoYearNumStr, null, null);

                Object numberParsed = this.config.getNumberParser().parse(er).value;
                int firstTwoYearNum = Integer.parseInt(numberParsed != null ? numberParsed.toString() : "0");

                int lastTwoYearNum = 0;
                String lastTwoYearNumStr = match.get().getGroup("lasttwoyearnum").value;
                if (!StringUtility.isNullOrEmpty(lastTwoYearNumStr)) {
                    er.withText(lastTwoYearNumStr);
                    er.withStart(match.get().getGroup("lasttwoyearnum").index);
                    er.withLength(match.get().getGroup("lasttwoyearnum").length);

                    Object parsed = this.config.getNumberParser().parse(er).value;
                    lastTwoYearNum = Integer.parseInt(parsed != null ? parsed.toString() : "0");
                }

                // Exclude pure number like "nineteen", "twenty four"
                if (firstTwoYearNum < 100 && lastTwoYearNum == 0 || firstTwoYearNum < 100 && firstTwoYearNum % 10 == 0 && lastTwoYearNumStr.trim().split(" ").length == 1) {
                    year = Constants.InvalidYear;
                    return year;
                }

                if (firstTwoYearNum >= 100) {
                    year = firstTwoYearNum + lastTwoYearNum;
                } else {
                    year = firstTwoYearNum * 100 + lastTwoYearNum;
                }
            }
        }

        return year;
    }
}

package com.microsoft.recognizers.text.sequence.english.parsers;

import com.microsoft.recognizers.text.ExtractResult;
import com.microsoft.recognizers.text.ParseResult;
import com.microsoft.recognizers.text.sequence.parsers.BaseSequenceParser;
import com.microsoft.recognizers.text.sequence.resources.BaseGUID;
import com.microsoft.recognizers.text.utilities.Match;
import com.microsoft.recognizers.text.utilities.RegExpUtility;

import java.util.regex.Pattern;

public class GUIDParser extends BaseSequenceParser {
    private static double scoreUpperLimit = 100;
    private static double scoreLowerLimit = 0;
    private static double baseScore = 100;
    private static double noBoundaryPenalty = 10;
    private static double noFormatPenalty = 10;
    private static double pureDigitPenalty = 15;
    private static String pureDigitRegex = "^\\d*$";
    private static String formatRegex = "-";

    private static final Pattern GuidElementRegex = Pattern.compile(BaseGUID.GUIDRegexElement);

    public static double ScoreGUID(String textGUID) {
        double score = baseScore;

        Match[] elementMatch = RegExpUtility.getMatches(GuidElementRegex,textGUID);
        if (elementMatch.length > 0) {
            int startIndex = elementMatch[1].index;
            String guidElement = elementMatch[1].value;
            score -= startIndex == 0 ? noBoundaryPenalty : 0;
            score -= Pattern.matches(formatRegex, guidElement) ? 0 : noBoundaryPenalty;
            score -= Pattern.matches(pureDigitRegex, textGUID) ? pureDigitPenalty : 0;
        }

        return Math.max(Math.min(score, scoreUpperLimit), scoreLowerLimit) / (scoreUpperLimit - scoreLowerLimit);
    }

    @Override
    public ParseResult Parse(ExtractResult extResult) {
        return new ParseResult(extResult);
    }
}

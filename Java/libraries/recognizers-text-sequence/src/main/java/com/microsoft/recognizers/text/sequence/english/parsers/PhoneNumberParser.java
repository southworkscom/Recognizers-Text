package com.microsoft.recognizers.text.sequence.english.parsers;

import com.microsoft.recognizers.text.ExtractResult;
import com.microsoft.recognizers.text.ParseResult;
import com.microsoft.recognizers.text.sequence.resources.BasePhoneNumbers;
import com.microsoft.recognizers.text.utilities.Match;
import com.microsoft.recognizers.text.utilities.RegExpUtility;

import java.util.Arrays;
import java.util.regex.Pattern;

public class PhoneNumberParser {
    private static double scoreUpperLimit = 100;
    private static double scoreLowerLimit = 0;
    private static double baseScore = 30;
    private static double countryCodeAward = 40;
    private static double areaCodeAward = 30;
    private static double formattedAward = 20;
    private static double lengthAward = 10;
    private static double typicalFormatDeductionScore = 40;
    private static double continueDigitDeductionScore = 10;
    private static double tailSameDeductionScore = 10;
    private static double continueFormatIndicatorDeductionScore = 20;
    private static double wrongFormatDeductionScore = 20;
    private static int maxFormatIndicatorNum = 3;
    private static int maxLengthAwardNum = 3;
    private static int tailSameLimit = 2;
    private static int phoneNumberLengthBase = 8;
    private static int pureDigitLengthLimit = 11;

    // @TODO move regexes to base resource files
    private static String completeBracketRegex = "\\(.*\\)";
    private static String singleBracketRegex = "\\(|\\)";
    private static String tailSameDigitRegex = "([\\d])\\1{2,10}$";
    private static String pureDigitRegex = "^\\d*$";
    private static String continueDigitRegex = "\\d{5}\\d*";
    private static String digitRegex = "\\d";

    private static final Pattern countryCodeRegex = Pattern.compile(BasePhoneNumbers.CountryCodeRegex);
    private static final Pattern areaCodeRegex = Pattern.compile(BasePhoneNumbers.AreaCodeIndicatorRegex);
    private static final Pattern formatIndicatorRegex = Pattern.compile(BasePhoneNumbers.FormatIndicatorRegex);
    private static final Pattern noAreaCodeUsPhoneNumberRegex = Pattern.compile(BasePhoneNumbers.NoAreaCodeUSPhoneNumberRegex);

    public static double ScorePhoneNumber(String phoneNumberText) {
        double score = baseScore;

        // Country code score or area code score
        score += countryCodeRegex.matcher(phoneNumberText).lookingAt()
                ? countryCodeAward : areaCodeRegex.matcher(phoneNumberText).lookingAt() ? areaCodeAward : 0;

        // Formatted score
        Match[] formatMatches = RegExpUtility.getMatches(formatIndicatorRegex, phoneNumberText);
        if (formatMatches.length > 0) {
            int formatIndicatorCount = formatMatches.length;
            score += Math.min(formatIndicatorCount, maxFormatIndicatorNum) * formattedAward;
            boolean anyMatch = Arrays.stream(formatMatches).anyMatch( match -> match.value.length() > 1);
            score -= anyMatch ? continueFormatIndicatorDeductionScore : 0;
            if (Pattern.matches(singleBracketRegex, phoneNumberText) &&
                !Pattern.matches(completeBracketRegex, phoneNumberText)) {
                score -= wrongFormatDeductionScore;
            }
        }

        // Length score
        score += Math.min(RegExpUtility.getMatches(Pattern.compile(digitRegex), phoneNumberText).length -
                phoneNumberLengthBase, maxLengthAwardNum) * lengthAward;

        // Same tailing digit deduction
        Match[] tailSameDigitMatches = RegExpUtility.getMatches(Pattern.compile(tailSameDigitRegex), phoneNumberText);
        if (tailSameDigitMatches.length > 0) {
            score -= (tailSameDigitMatches[0].value.length() - tailSameLimit) * tailSameDeductionScore;
        }

        // Pure digit deduction
        Match[] pureDigitMatches = RegExpUtility.getMatches(Pattern.compile(pureDigitRegex), phoneNumberText);
        if (pureDigitMatches.length > 0) {
            score -= phoneNumberText.length() > pureDigitLengthLimit ?
                    (phoneNumberText.length() - pureDigitLengthLimit) * lengthAward : 0;
        }

        // Special format deduction
        score -= Arrays.stream(BasePhoneNumbers.TypicalDeductionRegexList.toArray())
                    .anyMatch(o -> Pattern.matches((String)o, phoneNumberText)) ? typicalFormatDeductionScore : 0;

        // Continue digit deduction
        Match[] continueDigitMatches = RegExpUtility.getMatches(Pattern.compile(continueDigitRegex), phoneNumberText);
        score -= Math.max(continueDigitMatches.length - 1, 0) * continueDigitDeductionScore;

        // Special award for US phonenumber without area code, i.e. 223-4567 or 223 - 4567
        Match[] noAreaCodeUsPhoneNumberMatches = RegExpUtility.getMatches(noAreaCodeUsPhoneNumberRegex, phoneNumberText);
        if (noAreaCodeUsPhoneNumberMatches.length > 0) {
            score += lengthAward * 1.5;
        }

        return Math.max(Math.min(score, scoreUpperLimit), scoreLowerLimit) / (scoreUpperLimit - scoreLowerLimit);
    }

    @Override
    public ParseResult Parse(ExtractResult extResult) {
        return new ParseResult(extResult);
    }
}

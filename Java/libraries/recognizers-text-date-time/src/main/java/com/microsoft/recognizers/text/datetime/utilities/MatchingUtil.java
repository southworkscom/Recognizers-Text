package com.microsoft.recognizers.text.datetime.utilities;

import com.microsoft.recognizers.text.utilities.Match;
import com.microsoft.recognizers.text.utilities.RegExpUtility;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Pattern;

public class MatchingUtil {

    public static Boolean GetAgoLaterIndex(String text, Pattern regex, int index) {
        index = -1;
        Optional<Match> match = Arrays.stream(RegExpUtility.getMatches(regex, text.trim().toLowerCase())).findFirst();
        if (match.isPresent() && match.get().index == 0) {
            index = text.toLowerCase().lastIndexOf(match.get().value) + match.get().value.length();
            return true;
        }

        return false;
    }

    public static Boolean GetTermIndex(String text, Pattern regex, int index) {
        index = -1;

        String[] parts = text.trim().toLowerCase().split(" ");

        Optional<Match> match = Arrays.stream(RegExpUtility.getMatches(regex, parts[parts.length - 1])).findFirst();
        if (match.isPresent()) {
            index = text.length() - text.toLowerCase().lastIndexOf(match.get().value);
            return true;
        }

        return false;
    }

    public static Boolean ContainsAgoLaterIndex(String text, Pattern regex) {
        int index = -1;
        return GetAgoLaterIndex(text, regex, index);
    }

    public static Boolean ContainsTermIndex(String text, Pattern regex) {
        int index = -1;
        return GetTermIndex(text, regex, index);
    }
}

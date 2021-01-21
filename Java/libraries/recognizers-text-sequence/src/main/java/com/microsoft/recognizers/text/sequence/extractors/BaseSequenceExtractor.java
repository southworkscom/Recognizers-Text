package com.microsoft.recognizers.text.sequence.extractors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.microsoft.recognizers.text.ExtractResult;
import com.microsoft.recognizers.text.IExtractor;
import com.microsoft.recognizers.text.utilities.Match;
import com.microsoft.recognizers.text.utilities.RegExpUtility;

public abstract class BaseSequenceExtractor implements IExtractor {
    protected LinkedHashMap<Pattern, String> Regexes;
    protected String ExtractType = "";

    @Override
    public List<ExtractResult> Extract(String text) {
        List<ExtractResult> result = new ArrayList<>();

        if (text.isEmpty()) {
            return  result;
        }

        HashMap<Match, String> matchSource = new HashMap<>();
        boolean[] matched = new boolean[text.length()];

        // Traverse every match results to see each position in the text is matched or not.
        HashMap<Match[], String> collections = new HashMap<>();
        Regexes.forEach((key, value) -> {
            Match[] matches = RegExpUtility.getMatches(key, text);
            collections.put(matches, value);
        });

        collections.forEach((key, value) -> {
            for (Match match : key) {
                if (IsValidMatch(match)) {
                    for (int j = 0; j < match.length; j++) {
                        matched[match.index + j] = true;
                    }

                    // Keep Source Data for extra information
                    matchSource.put(match, value);
                }
            }
        });

        // Form the extracted results mark all the matched intervals in the text.
        int lastNotMatched = -1;
        for (int i = 0; i < text.length(); i++) {
            if (matched[i]) {
                if (i + 1 == text.length() || !matched[i + 1]) {
                    int start = lastNotMatched + 1;
                    int length = i - lastNotMatched;
                    String substr = text.substring(start, start + length);
                    Function<Match, Boolean> MatchFunc = match -> match.index == start && match.length == length;

                    Stream<Object> matchStream = Arrays.stream(matchSource.entrySet().toArray());
                    if (matchStream.anyMatch(o -> MatchFunc.apply((Match)o))) {
                        Match srcMatch = (Match)matchSource.keySet().toArray()[0];
                        ExtractResult extResult = new ExtractResult();

                        extResult.setStart(start);
                        extResult.setLength(length);
                        extResult.setText(substr);
                        extResult.setType(this.ExtractType);
                        extResult.setData(matchSource.getOrDefault(srcMatch, null));
                        result.add(extResult);
                    }
                }
            }
            else {
                lastNotMatched = i;
            }
        }

        return PostFilter(result);
    }

    public boolean IsValidMatch(Match match) {
        return true;
    }

    protected List<ExtractResult>  PostFilter(List<ExtractResult> results) {
        return results;
    }

    protected LinkedHashMap<Pattern, String> getRegexes() {
        return Regexes;
    }

    protected String getExtractType() {
        return ExtractType;
    }
}
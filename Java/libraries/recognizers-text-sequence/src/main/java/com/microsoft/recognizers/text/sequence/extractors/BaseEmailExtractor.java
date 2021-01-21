package com.microsoft.recognizers.text.sequence.extractors;

import com.microsoft.recognizers.text.ExtractResult;
import com.microsoft.recognizers.text.sequence.Constants;
import com.microsoft.recognizers.text.sequence.SequenceOptions;
import com.microsoft.recognizers.text.sequence.config.BaseSequenceConfiguration;
import com.microsoft.recognizers.text.sequence.resources.BaseEmail;
import com.microsoft.recognizers.text.utilities.StringUtility;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BaseEmailExtractor extends BaseSequenceExtractor {
    private static final Pattern Rfc5322ValidationRegex = Pattern.compile(BaseEmail.RFC5322Regex);
    private final BaseSequenceConfiguration config;

    public BaseEmailExtractor(BaseSequenceConfiguration config) {
        this.config = config;
        LinkedHashMap<Pattern, String> regexes = new LinkedHashMap<>();
        regexes.put(Pattern.compile(BaseEmail.EmailRegex), Constants.EMAIL_REGEX);
        regexes.put(Pattern.compile(BaseEmail.EmailRegex2), Constants.EMAIL_REGEX);

        Regexes = regexes;
    }

    protected LinkedHashMap<Pattern, String> Regexes;
    protected final String ExtractType = Constants.SYS_EMAIL;

    @Override
    protected LinkedHashMap<Pattern, String> getRegexes() {
        return Regexes;
    }

    @Override
    protected String getExtractType() {
        return ExtractType;
    }

    @Override
    protected List<ExtractResult>  PostFilter(List<ExtractResult> results) {
        // If Relaxed is on, no extra validation is applied
        if (config.getOptions() != SequenceOptions.None) {
            return results;
        }
        else {
            // Not return malformed e-mail addresses and trim ending '.'
            results.forEach(result -> {
                if (result.getText().endsWith(".")) {
                    result.setText(StringUtility.trimEnd(result.getText()));
                    result.setLength(result.getLength() - 1);
                }
            });
        }

        return results.stream().filter((o ->
                Rfc5322ValidationRegex.matcher((o).getText()).matches())).collect(Collectors.toList());
    }
}
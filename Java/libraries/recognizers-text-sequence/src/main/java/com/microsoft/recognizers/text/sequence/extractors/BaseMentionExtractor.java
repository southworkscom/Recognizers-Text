// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.recognizers.text.sequence.extractors;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.microsoft.recognizers.text.sequence.Constants;
import com.microsoft.recognizers.text.sequence.resources.BaseMention;

public class BaseMentionExtractor extends BaseSequenceExtractor{
    protected Map<Pattern, String> regexes;
    protected final String extractType = Constants.SYS_MENTION;
    
    public BaseMentionExtractor() {
        Map<Pattern, String> regexes = new HashMap<Pattern, String>() {
            {
                put(Pattern.compile(BaseMention.MentionRegex), Constants.MENTION_REGEX);
            }
        };

        this.regexes = regexes;
    }

    protected String getExtractType() {
        return this.extractType;
    }
}

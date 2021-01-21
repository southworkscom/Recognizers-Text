// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.recognizers.text.sequence.extractors;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.microsoft.recognizers.text.sequence.Constants;
import com.microsoft.recognizers.text.sequence.resources.BaseGUID;

public class BaseGUIDExtractor extends BaseSequenceExtractor {
    protected Map<Pattern, String> regexes;
    protected final String extractType = Constants.SYS_GUID;
    
    public BaseGUIDExtractor() {
        Map<Pattern, String> regexes = new HashMap<Pattern, String>() {
            {
                put(Pattern.compile(BaseGUID.GUIDRegex), Constants.GUID_REGEX);
            }
        };

        this.regexes = regexes;
    }

    protected String getExtractType() {
        return this.extractType;
    }
}

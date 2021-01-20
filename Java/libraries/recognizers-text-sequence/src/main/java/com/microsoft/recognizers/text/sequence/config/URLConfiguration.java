package com.microsoft.recognizers.text.sequence.config;

import com.microsoft.recognizers.text.sequence.SequenceOptions;

import java.util.regex.Pattern;

public class URLConfiguration implements ISequenceConfiguration {
    private SequenceOptions options;
    private Pattern ipUrlRegex;
    private Pattern urlRegex;

    public URLConfiguration(SequenceOptions options) {
        this.options = options != null ? options : SequenceOptions.None;
    }

    @Override
    public SequenceOptions getOptions() {
        return this.options;
    }

    public Pattern getIpUrlRegex() {
        return ipUrlRegex;
    }

    public void setIpUrlRegex(Pattern ipUrlRegex) {
        this.ipUrlRegex = ipUrlRegex;
    }

    public Pattern getUrlRegex() {
        return urlRegex;
    }

    public void setUrlRegex(Pattern urlRegex) {
        this.urlRegex = urlRegex;
    }
}

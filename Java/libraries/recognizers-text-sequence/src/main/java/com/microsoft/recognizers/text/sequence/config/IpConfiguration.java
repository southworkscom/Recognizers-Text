package com.microsoft.recognizers.text.sequence.config;

import com.microsoft.recognizers.text.sequence.SequenceOptions;

import java.util.regex.Pattern;

public class IpConfiguration implements ISequenceConfiguration {
    private SequenceOptions options;
    private Pattern Ipv4Regex;
    private Pattern Ipv6Regex;

    public IpConfiguration(SequenceOptions options) {
        this.options = options != null ? options : SequenceOptions.None;
    }

    @Override
    public SequenceOptions getOptions() {
        return null;
    }

    public Pattern getIpv4Regex() {
        return Ipv4Regex;
    }

    public void setIpv4Regex(Pattern ipv4Regex) {
        Ipv4Regex = ipv4Regex;
    }

    public Pattern getIpv6Regex() {
        return Ipv6Regex;
    }

    public void setIpv6Regex(Pattern ipv6Regex) {
        Ipv6Regex = ipv6Regex;
    }
}

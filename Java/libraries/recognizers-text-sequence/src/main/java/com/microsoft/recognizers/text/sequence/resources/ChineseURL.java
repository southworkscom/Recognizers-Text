// ------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated by a tool.
//     Changes to this file may cause incorrect behavior and will be lost if
//     the code is regenerated.
// </auto-generated>
//
// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// ------------------------------------------------------------------------------

package com.microsoft.recognizers.text.sequence.resources;

public class ChineseURL {

    public static final String ExtractionRestrictionRegex = "(?<=\\s|[\\'\"\"\\(\\[:：]|^|[\\u0800-\\u9FFF])";

    public static final String UrlPrefixRegex = "({ExtractionRestrictionRegex}{BaseURL.ProtocolRegex}?|{BaseURL.ProtocolRegex})[a-zA-Z0-9][-a-zA-Z0-9._]{0,256}(?<![.])\\."
            .replace("{ExtractionRestrictionRegex}", ExtractionRestrictionRegex)
            .replace("{BaseURL.ProtocolRegex}", BaseURL.ProtocolRegex);

    public static final String UrlRegex = "{UrlPrefixRegex}(?<Tld>[a-zA-Z]{2,18}){BaseURL.UrlSuffixRegex}"
            .replace("{UrlPrefixRegex}", UrlPrefixRegex)
            .replace("{BaseURL.UrlSuffixRegex}", BaseURL.UrlSuffixRegex);

    public static final String IpUrlRegex = "(?<IPurl>({ExtractionRestrictionRegex}{BaseURL.ProtocolRegex}({BaseIp.Ipv4Regex}|localhost){BaseURL.UrlSuffixRegex}))"
            .replace("{ExtractionRestrictionRegex}", ExtractionRestrictionRegex)
            .replace("{BaseURL.ProtocolRegex}", BaseURL.ProtocolRegex)
            .replace("{BaseIp.Ipv4Regex}", BaseIp.Ipv4Regex)
            .replace("{BaseURL.UrlSuffixRegex}", BaseURL.UrlSuffixRegex);
}

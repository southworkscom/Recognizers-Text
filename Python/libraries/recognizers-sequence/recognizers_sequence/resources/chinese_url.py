# ------------------------------------------------------------------------------
# <auto-generated>
#     This code was generated by a tool.
#     Changes to this file may cause incorrect behavior and will be lost if
#     the code is regenerated.
# </auto-generated>
#
# Copyright (c) Microsoft Corporation. All rights reserved.
# Licensed under the MIT License.
# ------------------------------------------------------------------------------

# pylint: disable=line-too-long
from .base_ip import BaseIp
from .base_url import BaseURL


class ChineseURL:
    ExtractionRestrictionRegex = f'(?<=\\s|[\\\'\"\"\\(\\[:：]|^|[\\u0800-\\u9FFF])'
    UrlPrefixRegex = f'({ExtractionRestrictionRegex}{BaseURL.ProtocolRegex}?|{BaseURL.ProtocolRegex})[a-zA-Z0-9][-a-zA-Z0-9._]{{0,256}}(?<![.])\\.'
    UrlRegex = f'{UrlPrefixRegex}(?<Tld>[a-zA-Z]{{2,18}}){BaseURL.UrlSuffixRegex}'
    IpUrlRegex = f'(?<IPurl>({ExtractionRestrictionRegex}{BaseURL.ProtocolRegex}({BaseIp.Ipv4Regex}|localhost){BaseURL.UrlSuffixRegex}))'
# pylint: enable=line-too-long

//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated by a tool.
//     Changes to this file may cause incorrect behavior and will be lost if
//     the code is regenerated.
//
//     Generation parameters:
//     - DataFilename: Patterns\Base-Numbers.yaml
//     - Language: NULL
//     - ClassName: BaseNumbers
// </auto-generated>
//
// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// ------------------------------------------------------------------------------

namespace Microsoft.Recognizers.Definitions
{
    using System;
    using System.Collections.Generic;

    public static class BaseNumbers
    {
      public const string NumberReplaceToken = @"@builtin.num";
      public const string FractionNumberReplaceToken = @"@builtin.num.fraction";
      public static readonly Func<string, string, string> IntegerRegexDefinition = (placeholder, thousandsmark) => $@"(((?<!\d+\s*)-\s*)|((?<=\b)(?<!\d+[\.,])))\d{{1,3}}({thousandsmark}\d{{3}})+(?={placeholder})";
      public const string FractionNotationRegex = @"((((?<=\W|^)-\s*)|(?<![/-])(?<=\b))\d+[/]\d+(?=(\b[^/]|$))|[\u00BC-\u00BE\u2150-\u215E])";
      public static readonly Func<string, string, string, string> DoubleRegexDefinition = (placeholder, thousandsmark, decimalmark) => $@"(((?<!\d+\s*)-\s*)|((?<=\b)(?<!\d+[\.,])))\d{{1,3}}(({thousandsmark}\d{{3}})+{decimalmark}|({decimalmark}\d{{3}})+{thousandsmark})\d+(?={placeholder})";
      public const string PlaceHolderDefault = @"\D|\b";
      public const string CaseSensitiveTerms = @"(?<=(\s|\d))(kB|K[Bb]?|M[BbM]?|G[Bb]?|B)\b";
      public const string NumberMultiplierRegex = @"(K|k|MM?|mil|G|T|B|b)";
      public const string MultiplierLookupRegex = @"(k|m(il|m)?|t|g|b)";
      public const string CurrencyRegex = @"(((?<=\W|^)-\s*)|(?<=\b))\d+\s*(b|m|t|g)(?=\b)";
      public const string CommonCurrencySymbol = @"(¥|\$|€|£|₩)";
    }
}
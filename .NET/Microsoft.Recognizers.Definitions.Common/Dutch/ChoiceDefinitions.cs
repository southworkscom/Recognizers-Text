//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated by a tool.
//     Changes to this file may cause incorrect behavior and will be lost if
//     the code is regenerated.
//
//     Generation parameters:
//     - DataFilename: Patterns\Dutch\Dutch-Choice.yaml
//     - Language: Dutch
//     - ClassName: ChoiceDefinitions
// </auto-generated>
//
// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// ------------------------------------------------------------------------------

namespace Microsoft.Recognizers.Definitions.Dutch
{
    using System;
    using System.Collections.Generic;

    public static class ChoiceDefinitions
    {
      public const string LangMarker = @"Nl";
      public const string TokenizerRegex = @"[^\w\d]";
      public const string SkinToneRegex = @"(\uD83C\uDFFB|\uD83C\uDFFC|\uD83C\uDFFD|\uD83C\uDFFE|\uD83C\uDFFF)";
      public static readonly string TrueRegex = $@"\b(ja|jawel|jazeker|natuurlijk|vanzelfsprekend|zeker|prima|jep|yes|yep|y|ok|oke|akkoord)\b|(\uD83D\uDC4D|\uD83D\uDC4C){SkinToneRegex}?";
      public static readonly string FalseRegex = $@"\b(nee|neen|nope|nein|nop|no|niet|nooit)\b|(\uD83D\uDC4E|\u270B|\uD83D\uDD90){SkinToneRegex}?";
    }
}
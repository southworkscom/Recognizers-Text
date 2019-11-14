﻿//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated by a tool.
//     Changes to this file may cause incorrect behavior and will be lost if
//     the code is regenerated.
//
//     Generation parameters:
//     - DataFilename: Patterns\Turkish\Turkish-Numbers.yaml
//     - Language: Turkish
//     - ClassName: NumbersDefinitions
// </auto-generated>
//
// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// ------------------------------------------------------------------------------

namespace Microsoft.Recognizers.Definitions.Turkish
{
    using System;
    using System.Collections.Generic;

    public static class NumbersDefinitions
    {
      public const string LangMarker = @"Tr";
      public const bool CompoundNumberLanguage = true;
      public const bool MultiDecimalSeparatorCulture = true;
      public const string DigitsNumberRegex = @"\d+|\d{1,3}(\.\d{3})";
      public const string RoundNumberIntegerRegex = @"(yüz|bin|milyon|milyar|trilyon)";
      public const string ZeroToNineIntegerRegex = @"(sıfır|bir|[iİ]ki|üç|dört|beş|altı|yedi|sekiz|dokuz)";
      public const string OneToNineIntegerRegex = @"(bir|[iİ]ki|üç|dört|beş|altı|yedi|sekiz|dokuz)";
      public const string TwoToNineIntegerRegex = @"([iİ]ki|üç|dört|beş|altı|yedi|sekiz|dokuz)";
      public const string NegativeNumberTermsRegex = @"(?<negTerm>(eksi|negatif)\s+)";
      public static readonly string NegativeNumberSignRegex = $@"^{NegativeNumberTermsRegex}.*";
      public const string TensNumberIntegerRegex = @"(on|yirmi|otuz|kırk|elli|altmış|yetmiş|seksen|doksan)";
      public static readonly string HundredsNumberIntegerRegex = $@"({TwoToNineIntegerRegex}\s*yüz|yüz)";
      public static readonly string TenToHundredRegex = $@"({TensNumberIntegerRegex}(\s*{OneToNineIntegerRegex}))";
      public static readonly string HundredToThousandRegex = $@"({HundredsNumberIntegerRegex}(\s*({OneToNineIntegerRegex}|{TenToHundredRegex}|{TensNumberIntegerRegex})))";
      public static readonly string ThousandsNumberIntegerRegex = $@"(({HundredToThousandRegex}|{TenToHundredRegex}|{HundredsNumberIntegerRegex}|{TwoToNineIntegerRegex}|{TensNumberIntegerRegex})\s*bin|bin)";
      public static readonly string ThousandToMillionRegex = $@"({ThousandsNumberIntegerRegex}(\s*({HundredToThousandRegex}|{HundredsNumberIntegerRegex}|{TenToHundredRegex}|{{TensNumberIntegerRegex}}|{OneToNineIntegerRegex})))";
      public static readonly string MillionsNumberIntegerRegex = $@"(({HundredToThousandRegex}|{TenToHundredRegex}|{HundredsNumberIntegerRegex}|{OneToNineIntegerRegex}|{TensNumberIntegerRegex})\s*milyon)";
      public static readonly string MillionToBillionRegex = $@"({MillionsNumberIntegerRegex}(\s*({ThousandToMillionRegex}|{ThousandsNumberIntegerRegex}|{HundredToThousandRegex}|{TenToHundredRegex}|{HundredsNumberIntegerRegex}|{OneToNineIntegerRegex}|{TensNumberIntegerRegex})))";
      public static readonly string BillionsNumberIntegerRegex = $@"(({HundredToThousandRegex}|{TenToHundredRegex}|{HundredsNumberIntegerRegex}|{OneToNineIntegerRegex}|{TensNumberIntegerRegex})\s*milyar)";
      public static readonly string BillionToTrillionRegex = $@"({BillionsNumberIntegerRegex}(\s*({MillionToBillionRegex}|{MillionsNumberIntegerRegex}|{ThousandToMillionRegex}|{ThousandsNumberIntegerRegex}|{HundredToThousandRegex}|{TenToHundredRegex}|{HundredsNumberIntegerRegex}|{OneToNineIntegerRegex}|{TensNumberIntegerRegex})))";
      public static readonly string TrillionsNumberIntegerRegex = $@"(({ThousandsNumberIntegerRegex}|{HundredToThousandRegex}|{TenToHundredRegex}|{HundredsNumberIntegerRegex}|{OneToNineIntegerRegex}|{TensNumberIntegerRegex})\s*trilyon)";
      public static readonly string AboveTrillionRegex = $@"({TrillionsNumberIntegerRegex}(\s*({BillionToTrillionRegex}|{BillionsNumberIntegerRegex}|{MillionToBillionRegex}|{MillionsNumberIntegerRegex}|{ThousandToMillionRegex}|{ThousandsNumberIntegerRegex}|{HundredToThousandRegex}|{TenToHundredRegex}|{HundredsNumberIntegerRegex}|{OneToNineIntegerRegex}|{TensNumberIntegerRegex})))";
      public static readonly string AllIntRegex = $@"({AboveTrillionRegex}|{BillionToTrillionRegex}|{MillionToBillionRegex}|{ThousandToMillionRegex}|{MillionsNumberIntegerRegex}|{BillionsNumberIntegerRegex}|{TrillionsNumberIntegerRegex}|{ThousandsNumberIntegerRegex}|{HundredToThousandRegex}|{HundredsNumberIntegerRegex}|{TenToHundredRegex}|{TensNumberIntegerRegex}|{ZeroToNineIntegerRegex})";
      public static readonly string NegativeAllIntRegex = $@"(eksi\s)({OneToNineIntegerRegex}|{TenToHundredRegex}|{HundredToThousandRegex}|{ThousandToMillionRegex}|{MillionToBillionRegex}|{BillionToTrillionRegex}|{AboveTrillionRegex})";
      public const string PlaceHolderPureNumber = @"(?!'(i|ı|u|ü|n))\b";
      public const string PlaceHolderDefault = @"\D|\b";
      public static readonly Func<string, string> NumbersWithPlaceHolder = (placeholder) => $@"(((?<!\d+\s*)-\s*)|(?<=\b))\d+(?!([\.,]\d+[a-zA-Z]))(?={placeholder})";
      public static readonly string NumbersWithSuffix = $@"(((?<!\d+\s*)-\s*)|(?<=\b))\d+\s*{BaseNumbers.NumberMultiplierRegex}(?=\b)";
      public static readonly string RoundNumberIntegerRegexWithLocks = $@"(?<=\b)\d+\s+{RoundNumberIntegerRegex}(?=\b)";
      public const string NumbersWithDozenSuffix = @"(((?<!\d+\s*)-\s*)|(?<=\b))\d+\s+düzine(?=\b)";
      public static readonly string AllIntRegexWithLocks = $@"((?<=\b){AllIntRegex}(?=\b))";
      public static readonly string NegativeAllIntRegexWithLocks = $@"((?<=\b){NegativeAllIntRegex}(?=\b))";
      public static readonly string AllIntRegexWithDozenSuffixLocks = $@"(?<=\b)(((yarım\s+)?düzine)|({AllIntRegex}\s+düzine))(?=\b)";
      public const string RoundNumberOrdinalRegex = @"(yüzüncü|bininci|milyonuncu|milyarıncı|trilyonuncu)";
      public const string NumberOrdinalRegex = @"(birinci|[iİ]lk|[iİ]kinci|üçüncü|dördüncü|beşinci|altıncı|yedinci|sekizinci|dokuzuncu)";
      public const string TensOrdinalRegex = @"(onuncu|yirminci|otuzuncu|kırkıncı|ellinci|altmışıncı|yetmişinci|sekseninci|doksanıncı)";
      public static readonly string OneToHundredOrdinalRegex = $@"(({TensNumberIntegerRegex}\s)?{NumberOrdinalRegex}|{TensOrdinalRegex})";
      public static readonly string HundredsOrdinalRegex = $@"(({TwoToNineIntegerRegex}\s)?(yüzüncü))";
      public static readonly string HundredToThousandOrdinalRegex = $@"({HundredsNumberIntegerRegex}\s{OneToHundredOrdinalRegex}|{HundredsOrdinalRegex})";
      public static readonly string ThousandsOrdinalRegex = $@"(({TwoToNineIntegerRegex}\s)?(bininci))";
      public static readonly string ThousandToMillionOrdinalRegex = $@"({ThousandsNumberIntegerRegex}\s({OneToHundredOrdinalRegex}|{HundredToThousandOrdinalRegex})|{ThousandsOrdinalRegex})";
      public static readonly string MillionsOrdinalRegex = $@"(({OneToNineIntegerRegex}\s)?(milyonuncu))";
      public static readonly string MillionToBillionOrdinalRegex = $@"({MillionsNumberIntegerRegex}\s({OneToHundredOrdinalRegex}|{HundredToThousandOrdinalRegex}|{ThousandToMillionOrdinalRegex})|{MillionsOrdinalRegex})";
      public static readonly string BillionsOrdinalRegex = $@"(({OneToNineIntegerRegex}\s)?(milyarıncı))";
      public static readonly string BillionToTrillionOrdinalRegex = $@"({BillionsNumberIntegerRegex}\s({OneToHundredOrdinalRegex}|{HundredToThousandOrdinalRegex}|{ThousandToMillionOrdinalRegex}|{MillionToBillionOrdinalRegex})|{BillionsOrdinalRegex})";
      public static readonly string TrillionsOrdinalRegex = $@"((({OneToNineIntegerRegex}|{TenToHundredRegex}|{HundredToThousandRegex}|{TensNumberIntegerRegex}|{HundredsNumberIntegerRegex})\s)?(trilyonuncu))";
      public static readonly string AboveTrillionOrdinalRegex = $@"({TrillionsNumberIntegerRegex}\s({OneToHundredOrdinalRegex}|{HundredToThousandOrdinalRegex}|{ThousandToMillionOrdinalRegex}|{MillionToBillionOrdinalRegex}|{BillionToTrillionOrdinalRegex})|{TrillionsOrdinalRegex})";
      public const string RelativeOrdinalRegex = @"(?<relativeOrdinal>(bir\s+)?((?<!(yarından|veya)\s+)sonraki|önceki)|sondan\s+birinci|sondan\s+bir\s+önceki|sondan\s+ikinci|(en\s+)?son|[iİ]lki?|son(uncu(su)?)?|şimdiki)";
      public static readonly string AllOrdinalRegex = $@"({OneToHundredOrdinalRegex}|{HundredToThousandOrdinalRegex}|{ThousandToMillionOrdinalRegex}|{MillionToBillionOrdinalRegex}|{BillionToTrillionOrdinalRegex}|{AboveTrillionOrdinalRegex}|{RelativeOrdinalRegex})";
      public const string AllOrdinalSuffix = @"(onu(?=nda)?|yirmisi(?=nde)?|otuzu(?=nda)?|kırkı(?=nda)?|ellisi(?=nde)?|altmışı(?=nda)?|yetmişi(?=nde)?|sekseni(?=nde)?|doksanı(?=nda)?|((on|yirmi|otuz)\s+)?(biri(?=nde)?|[iİ]kisi(?=nde)?|üçü(?=nde)?|dördü(?=nde)?|beşi(?=nde)?|altısı(?=nda)?|yedisi(?=nde)?|sekizi(?=nde)?|dokuzu(?=nda)?))";
      public const string OrdinalSuffixRegex = @"(?<=\b)(\d*(00(\.(?!\d+)|'üncü)|000(\.(?!\d+)|'inci)|000\.?000(\.(?!\d+)|'uncu)|000\.?000\.?000(\.(?!\d+)|'ıncı)|000\.?000\.?000\.?000(\.(?!\d+)|'uncu)|10(\.(?!\d+)|'uncu|'u(?=(nda|na))?)|20(\.(?!\d+)|'nci|'si(?=(nde|ne))?)|30(\.(?!\d+)|'uncu|'u(?=(nda|na))?)|40(\.(?!\d+)|'ıncı|'ı(?=(nda|na))?)|50(\.(?!\d+)|'inci|'si(?=(nde|ne))?)|60(\.(?!\d+)|'ıncı|'ı(?=(nda|na))?)|70(\.(?!\d+)|'inci|'i(?=(nde|ne))?)|80(\.(?!\d+)|'inci|'i(?=(nde|ne))?)|90(\.(?!\d+)|'ıncı|'ı(?=(nda|na))?)|1(\.(?!\d+)|'inci|'i(?=(nde|ne))?)|2(\.(?!\d+)|'nci|'si(?=(nde|ne))?)|3(\.(?!\d+)|'üncü|'ü(?=(nde|ne))?)|4(\.(?!\d+)|'üncü|'ü(?=(nde|ne))?)|5(\.(?!\d+)|'inci|'i(?=(nde|ne))?)|6(\.(?!\d+)|'ıncı|'sı(?=(nda|na))?)|7(\.(?!\d+)|'inci|'si(?=(nde|ne))?)|8(\.(?!\d+)|'inci|'i(?=(nde|ne))?)|9(\.(?!\d+)|'uncu|'u(?=(nda|na))?)))";
      public const string OrdinalNumericRegex = @"(?<=\b)(?:\d{1,3}(\s*,\s*\d{3})*('inci|'ıncı|'uncu|'üncü|'nci|'ncı))(?=\b)";
      public static readonly string OrdinalTurkishRegex = $@"(?<=\b)({AllOrdinalRegex}(?=\b)|{AllOrdinalSuffix})";
      public const string FractionNotationWithSpacesRegex = @"(((?<=\W|^)-\s*)|(?<=\b))\d+\s+\d+[/]\d+(?=(\b[^/]|$))";
      public const string FractionNotationRegex = @"(((?<=\W|^)-\s*)|(?<![/-])(?<=\b))\d+[/]\d+(?=(\b[^/]|$))";
      public static readonly string FractionNounWithArticleRegex = $@"(?<=\b)(({AllIntRegex}\s)?(buçuk|çeyrek|yarım))(?=\b)";
      public static readonly string FractionPrepositionRegex = $@"(?<!{BaseNumbers.CommonCurrencySymbol}\s*)(?<=\b)(eksi\s)?(?<numerator>({AllIntRegex})|((?<!,)\d+))\s+(bölü)\s+(?<denominator>({AllIntRegex})|(\d+)(?!,))(?=\b)";
      public static readonly string AllPointRegex = $@"((\s{ZeroToNineIntegerRegex})+|(\s{AllIntRegex}))";
      public static readonly string FloatRegex1 = $@"(({NegativeAllIntRegex}|{AllIntRegex})(\s(nokta)){AllPointRegex})";
      public static readonly string FloatRegex2 = $@"{AllIntRegex}(\s+(tam)\s+)((onda)\s+{OneToNineIntegerRegex}|(yüzde)\s+({OneToNineIntegerRegex}|{TenToHundredRegex})|(binde)\s+({OneToNineIntegerRegex}|{TenToHundredRegex}|{HundredToThousandRegex}))";
      public static readonly string AllFloatRegex = $@"({FloatRegex1}|{FloatRegex2})";
      public static readonly string DoubleWithMultiplierRegex = $@"(((?<!\d+\s*)-\s*)|((?<=\b)(?<!\d+,)))\d+,\d+\s*{BaseNumbers.NumberMultiplierRegex}(?=\b)";
      public const string DoubleExponentialNotationRegex = @"(((?<!\d+\s*)-\s*)|((?<=\b)(?<!\d+,)))(\d+(,\d+)?)e([+-]*[1-9]\d*)(?=\b)";
      public const string DoubleCaretExponentialNotationRegex = @"(((?<!\d+\s*)-\s*)|((?<=\b)(?<!\d+,)))(\d+(,\d+)?)\^([+-]*[1-9]\d*)(?=\b)";
      public static readonly Func<string, string> DoubleDecimalPointRegex = (placeholder) => $@"(((?<!\d+\s*)-\s*)|((?<=\b)(?<!\d+,)))\d+,\d+(?!(,\d+))(?={placeholder})";
      public static readonly Func<string, string> DoubleWithoutIntegralRegex = (placeholder) => $@"(?<=\s|^)(?<!(\d+)),\d+(?!(,\d+))(?={placeholder})";
      public static readonly string DoubleWithRoundNumber = $@"(((?<!\d+\s*)-\s*)|((?<=\b)(?<!\d+,)))\d+,\d+\s+{RoundNumberIntegerRegex}(?=\b)";
      public static readonly string DoubleAllFloatRegex = $@"((?<=\b){AllFloatRegex}(?=\b))";
      public const string ConnectorRegex = @"(?<spacer>\s)";
      public const string NumberWithSuffixPercentage = @"(((({TensNumberIntegerRegex}\s)?(birin|[iİ]kinin|üçün|dördün|beşin|altının|yedinin|sekizin|dokuzun)|onun|yirminin|otuzun|kırkın|ellinin|altmışın|yetmişin|seksenin|doksanın)\s(yüzdesi))|(\d*(1'in|2'nin|3'ün|4'ün|5'in|6'nın|7'nin|8'in|9'un|10'un|20'nin|30'un|40'ın|50'nin|60'ın|70'in|80'in|90'ın)\s(yüzdesi)))";
      public const string FractionNumberWithSuffixPercentage = @"(\d+,\d+((1|5|8|70|80)'i|(2|7|20|50)'si|(3|4|100)'ü|6'sı|(9|10|30)'u|(40|60|90)'ı))";
      public static readonly string NumberWithPrefixPercentage = $@"(%|eksi\syüzde\s|yüzde\s)({BaseNumbers.NumberReplaceToken}|{AllIntRegex})";
      public static readonly string NumberWithPrepositionPercentage = $@"({BaseNumbers.NumberReplaceToken})\s*(üzerinden)\s*({BaseNumbers.NumberReplaceToken})";
      public const string TillRegex = @"(-|—|——|–|~)";
      public const string MoreRegex = @"(büyük(tür)?|(?<!<|=)>)";
      public const string LessRegex = @"(küçük(tür)?|(?<!>|=)<)";
      public const string EqualRegex = @"(eşit(tir)?|(?<!<|>)=)";
      public const string MoreOrEqualPrefix = @"((en\s+az))";
      public const string MoreOrEqual = @"((büyük(tür)?\s+veya\s+eşit(tir)?)|>\s*=)";
      public const string MoreOrEqualSuffix = @"(az\s+değil)";
      public const string LessOrEqualPrefix = @"(en\s+(fazla|çok))";
      public const string LessOrEqual = @"((küçük(tür)?\s+veya\s+eşit(tir)?)|<\s*=)";
      public const string LessOrEqualSuffix = @"(fazla\s+değil)";
      public const string NumberSplitMark = @"(?![,.](?!\d+))";
      public const string MoreRegexNoNumberSucceed = @"((daha\s+fazla)(?!(\s*\d+)))";
      public const string LessRegexNoNumberSucceed = @"((daha\s+az)(?!(\s*\d+)))";
      public const string NumberFromSuffixRegex = @"(\d*(1'den|2'den|3'ten|4'ten|5'ten|6'dan|7'den|8'den|9'dan|10'dan|20'den|30'dan|40'tan|50'den|60'tan|70'ten|80'den|90'dan|00'den|\.?000'den|000\.?000'dan|000(\.?000){2}'dan|000(\.?000){2}\.?000'dan)|((on|yirmi|otuz|kırk|elli|altmış|yetmiş|seksen|doksan|yüz)\s)?(birden|[iİ]kiden|üçten|dörtten|beşten|altıdan|yediden|sekizden|dokuzdan)|ondan|yirmiden|otuzdan|kırktan|elliden|altmıştan|yetmişten|seksenden|doksandan|yüzden|binden|çeyrekten|yarımdan)";
      public const string NumberToSuffixRegex = @"(\d*(1'e|2'ye|3'e|4'e|5'e|6'ya|7'ye|8'e|9'a|10'a|20'ye|30'a|40'a|50'ye|60'a|70'e|80'e|90'a|00'e|\.?000'e|000\.?000'a|000(\.?000){2}'a|000(\.?000){2}\.?000'a))";
      public static readonly string OneNumberRangeMoreRegex1 = $@"((?<number1>{NumberFromSuffixRegex})\s+({MoreRegex}|{MoreOrEqual}))|((?<number1>({NumberSplitMark}.)+)\s(ve|veya|ya da)\s+daha\s+(fazla|fazlası|yüksek))|{MoreRegex}\s*(?<number1>({NumberSplitMark}.)+)";
      public static readonly string OneNumberRangeMoreRegex2 = $@"(({MoreOrEqual}|{MoreOrEqualPrefix})\s*(?<number1>({NumberSplitMark}.)+))";
      public static readonly string OneNumberRangeMoreSeparateRegex = $@"((?<number1>{NumberToSuffixRegex})\s{EqualRegex}(\s+(ve|veya|ya\sda)\s+){MoreRegexNoNumberSucceed})";
      public static readonly string OneNumberRangeLessRegex1 = $@"((?<number1>{NumberFromSuffixRegex})\s+({LessRegex}|{LessOrEqual})|{LessRegex}\s*(?<number1>({NumberSplitMark}.)+))";
      public static readonly string OneNumberRangeLessRegex2 = $@"(({LessOrEqual}|{LessOrEqualPrefix})\s*(?<number1>({NumberSplitMark}.)+))";
      public static readonly string OneNumberRangeLessSeparateRegex = $@"((?<number1>{NumberFromSuffixRegex})\s{EqualRegex}(\s+(ve|veya|ya da)\s+){LessRegexNoNumberSucceed})";
      public static readonly string OneNumberRangeEqualRegex = $@"{EqualRegex}\s*(?<number1>({NumberSplitMark}.)+)|{NumberToSuffixRegex}\s+{EqualRegex}";
      public static readonly string TwoNumberRangeRegex1 = $@"(?<number1>({NumberSplitMark}.)+)\s(ile|ila|ve)\s(?<number2>({NumberSplitMark}.)+)(\sarasında)";
      public static readonly string TwoNumberRangeRegex2 = $@"({OneNumberRangeMoreRegex1}|{OneNumberRangeMoreRegex2})\s*(ve|ama|fakat|ancak|,)\s*({OneNumberRangeLessRegex1}|{OneNumberRangeLessRegex2})";
      public static readonly string TwoNumberRangeRegex3 = $@"({OneNumberRangeLessRegex1}|{OneNumberRangeLessRegex2})\s*(ve|ama|fakat|ancak|,)\s*({OneNumberRangeMoreRegex1}|{OneNumberRangeMoreRegex2})";
      public static readonly string TwoNumberRangeRegex4 = $@"(?<number1>({NumberSplitMark}.)+)\s*{TillRegex}\s*(?<number2>({NumberSplitMark}.)+)|({NumberFromSuffixRegex}\s{NumberToSuffixRegex})(\skadar)";
      public const char DecimalSeparatorChar = ',';
      public const string FractionMarkerToken = @"bölü";
      public const char NonDecimalSeparatorChar = '.';
      public const string HalfADozenText = @"altı";
      public const string WordSeparatorToken = @"ve";
      public static readonly string[] WrittenDecimalSeparatorTexts = { @"nokta" };
      public static readonly string[] WrittenGroupSeparatorTexts = { @"nokta" };
      public static readonly string[] WrittenIntegerSeparatorTexts = { @"\s" };
      public static readonly string[] WrittenFractionSeparatorTexts = { @"\s" };
      public const string HalfADozenRegex = @"yarım\s+düzine";
      public static readonly string DigitalNumberRegex = $@"((?<=\b)(yüz|bin|milyon|milyar|trilyon|düzine)(?=\b))|((?<=(\d|\b)){BaseNumbers.MultiplierLookupRegex}(?=\b))";
      public static readonly Dictionary<string, long> CardinalNumberMap = new Dictionary<string, long>
        {
            { @"sıfır", 0 },
            { @"bir", 1 },
            { @"iki", 2 },
            { @"İki", 2 },
            { @"üç", 3 },
            { @"dört", 4 },
            { @"beş", 5 },
            { @"altı", 6 },
            { @"yedi", 7 },
            { @"sekiz", 8 },
            { @"dokuz", 9 },
            { @"on", 10 },
            { @"on bir", 11 },
            { @"on iki", 12 },
            { @"düzine", 12 },
            { @"on üç", 13 },
            { @"on dört", 14 },
            { @"on beş", 15 },
            { @"on altı", 16 },
            { @"on yedi", 17 },
            { @"on sekiz", 18 },
            { @"on dokuz", 19 },
            { @"yirmi", 20 },
            { @"otuz", 30 },
            { @"kırk", 40 },
            { @"elli", 50 },
            { @"altmış", 60 },
            { @"yetmiş", 70 },
            { @"seksen", 80 },
            { @"doksan", 90 },
            { @"yüz", 100 },
            { @"iki yüz", 200 },
            { @"üç yüz", 300 },
            { @"dört yüz", 400 },
            { @"beş yüz", 500 },
            { @"altı yüz", 600 },
            { @"yedi yüz", 700 },
            { @"sekiz yüz", 800 },
            { @"dokuz yüz", 900 },
            { @"bin", 1000 },
            { @"milyon", 1000000 },
            { @"milyar", 1000000000 },
            { @"trilyon", 1000000000000 }
        };
      public static readonly Dictionary<string, long> OrdinalNumberMap = new Dictionary<string, long>
        {
            { @"birinci", 1 },
            { @"biri", 1 },
            { @"ilk", 1 },
            { @"İlk", 1 },
            { @"ikinci", 2 },
            { @"İkinci", 2 },
            { @"ikisi", 2 },
            { @"İkisi", 2 },
            { @"ikincil", 2 },
            { @"İkincil", 2 },
            { @"yarım", 2 },
            { @"buçuk", 2 },
            { @"üçüncü", 3 },
            { @"üçü", 3 },
            { @"dördüncü", 4 },
            { @"dördü", 4 },
            { @"çeyrek", 4 },
            { @"beşinci", 5 },
            { @"beşi", 5 },
            { @"altıncı", 6 },
            { @"altısı", 6 },
            { @"yedinci", 7 },
            { @"yedisi", 7 },
            { @"sekizinci", 8 },
            { @"sekizi", 8 },
            { @"dokuzuncu", 9 },
            { @"dokuzu", 9 },
            { @"onuncu", 10 },
            { @"onu", 10 },
            { @"yirminci", 20 },
            { @"yirmisi", 20 },
            { @"otuzuncu", 30 },
            { @"otuzu", 30 },
            { @"kırkıncı", 40 },
            { @"kırkı", 40 },
            { @"ellinci", 50 },
            { @"ellisi", 50 },
            { @"altmışıncı", 60 },
            { @"altmışı", 60 },
            { @"yetmişinci", 70 },
            { @"yetmişi", 70 },
            { @"sekseninci", 80 },
            { @"sekseni", 80 },
            { @"donsanıncı", 90 },
            { @"doksanı", 90 },
            { @"yüzüncü", 100 },
            { @"bininci", 1000 },
            { @"milyonuncu", 1000000 },
            { @"milyarıncı", 1000000000 },
            { @"trilyonuncu", 1000000000000 }
        };
      public static readonly Dictionary<string, long> RoundNumberMap = new Dictionary<string, long>
        {
            { @"yüz", 100 },
            { @"bin", 1000 },
            { @"milyon", 1000000 },
            { @"milyar", 1000000000 },
            { @"trilyon", 1000000000000 },
            { @"yüzüncü", 100 },
            { @"bininci", 1000 },
            { @"milyonuncu", 1000000 },
            { @"milyarıncı", 1000000000 },
            { @"trilyonuncu", 1000000000000 },
            { @"deste", 10 },
            { @"düzine", 12 },
            { @"k", 1000 },
            { @"m", 1000000 },
            { @"g", 1000000000 },
            { @"b", 1000000000 },
            { @"t", 1000000000000 }
        };
      public static readonly Dictionary<string, string> AmbiguityFiltersDict = new Dictionary<string, string>
        {
            { @"\bone\b", @"\b(bu|şu)\b" }
        };
      public static readonly Dictionary<string, string> RelativeReferenceOffsetMap = new Dictionary<string, string>
        {
            { @"en son", @"0" },
            { @"son", @"0" },
            { @"sonuncu", @"0" },
            { @"sonuncusu", @"0" },
            { @"şimdiki", @"0" },
            { @"ilk", @"0" },
            { @"İlk", @"0" },
            { @"ilki", @"0" },
            { @"İlki", @"0" },
            { @"bir sonraki", @"1" },
            { @"bir önceki", @"-1" },
            { @"sondan ikinci", @"-1" },
            { @"sondan bir önceki", @"-1" },
            { @"sondan üçüncü", @"-2" },
            { @"sonraki", @"1" },
            { @"önceki", @"-1" }
        };
      public static readonly Dictionary<string, string> RelativeReferenceRelativeToMap = new Dictionary<string, string>
        {
            { @"en son", @"end" },
            { @"son", @"end" },
            { @"sonuncu", @"end" },
            { @"sonuncusu", @"end" },
            { @"şimdiki", @"current" },
            { @"ilk", @"current" },
            { @"İlk", @"current" },
            { @"ilki", @"current" },
            { @"İlki", @"current" },
            { @"bir sonraki", @"current" },
            { @"bir önceki", @"current" },
            { @"sondan birinci", @"end" },
            { @"en sondan bir önceki", @"end" },
            { @"sondan bir önceki", @"end" },
            { @"sondan ikinci", @"end" },
            { @"sonraki", @"current" },
            { @"önceki", @"current" }
        };
    }
}
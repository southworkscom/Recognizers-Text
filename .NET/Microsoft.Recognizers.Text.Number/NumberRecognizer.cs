using Microsoft.Recognizers.Text.Number.Chinese;
using Microsoft.Recognizers.Text.Number.English;
using Microsoft.Recognizers.Text.Number.French;
using Microsoft.Recognizers.Text.Number.German;
using Microsoft.Recognizers.Text.Number.Portuguese;
using Microsoft.Recognizers.Text.Number.Spanish;

namespace Microsoft.Recognizers.Text.Number
{
    public static class NumberRecognizer
    {
        private static ModelFactory<NumberOptions> factory = new ModelFactory<NumberOptions>()
        {
            {
                (Culture.English, typeof(NumberModel)),
                (options) => new NumberModel(
                    AgnosticNumberParserFactory.GetParser(AgnosticNumberParserType.Number, new EnglishNumberParserConfiguration()),
                    English.NumberExtractor.GetInstance(NumberMode.PureNumber))
            },
            {
                (Culture.English, typeof(OrdinalModel)),
                (options) => new OrdinalModel(
                    AgnosticNumberParserFactory.GetParser(AgnosticNumberParserType.Ordinal, new EnglishNumberParserConfiguration()),
                    English.OrdinalExtractor.GetInstance())
            },
            {
                (Culture.English, typeof(PercentModel)),
                (options) => new PercentModel(
                    AgnosticNumberParserFactory.GetParser(AgnosticNumberParserType.Percentage, new EnglishNumberParserConfiguration()),
                    new English.PercentageExtractor())
            },
            {
                (Culture.English, typeof(NumberRangeModel)),
                (options) => new NumberRangeModel(
                            new BaseNumberRangeParser(new EnglishNumberRangeParserConfiguration()),
                            new English.NumberRangeExtractor())
            },
            {
                (Culture.Chinese, typeof(NumberModel)),
                (options) => new NumberModel(
                    AgnosticNumberParserFactory.GetParser(AgnosticNumberParserType.Number, new ChineseNumberParserConfiguration()),
                    new Chinese.NumberExtractor())
            },
            {
                (Culture.Chinese, typeof(OrdinalModel)),
                (options) => new OrdinalModel(
                    AgnosticNumberParserFactory.GetParser(AgnosticNumberParserType.Ordinal, new ChineseNumberParserConfiguration()),
                    new Chinese.OrdinalExtractor())
            },
            {
                (Culture.Chinese, typeof(PercentModel)),
                (options) => new PercentModel(
                    AgnosticNumberParserFactory.GetParser(AgnosticNumberParserType.Percentage, new ChineseNumberParserConfiguration()),
                    new Chinese.PercentageExtractor())
            },
            {
                (Culture.Chinese, typeof(NumberRangeModel)),
                (options) => new NumberRangeModel(
                            new BaseNumberRangeParser(new ChineseNumberRangeParserConfiguration()),
                            new Chinese.NumberRangeExtractor())
            },
            {
                (Culture.Spanish, typeof(NumberModel)),
                (options) => new NumberModel(
                    AgnosticNumberParserFactory.GetParser(AgnosticNumberParserType.Number, new SpanishNumberParserConfiguration()),
                    new Spanish.NumberExtractor(NumberMode.PureNumber))
            },
            {
                (Culture.Spanish, typeof(OrdinalModel)),
                (options) => new OrdinalModel(
                    AgnosticNumberParserFactory.GetParser(AgnosticNumberParserType.Ordinal, new SpanishNumberParserConfiguration()),
                    new Spanish.OrdinalExtractor())
            },
            {
                (Culture.Spanish, typeof(PercentModel)),
                (options) => new PercentModel(
                    AgnosticNumberParserFactory.GetParser(AgnosticNumberParserType.Percentage, new SpanishNumberParserConfiguration()),
                    new Spanish.PercentageExtractor())
            },
            {
                (Culture.Portuguese, typeof(NumberModel)),
                (options) => new NumberModel(
                    AgnosticNumberParserFactory.GetParser(AgnosticNumberParserType.Number, new PortugueseNumberParserConfiguration()),
                    new Portuguese.NumberExtractor(NumberMode.PureNumber))
            },
            {
                (Culture.Portuguese, typeof(OrdinalModel)),
                (options) => new OrdinalModel(
                    AgnosticNumberParserFactory.GetParser(AgnosticNumberParserType.Ordinal, new PortugueseNumberParserConfiguration()),
                    new Portuguese.OrdinalExtractor())
            },
            {
                (Culture.Portuguese, typeof(PercentModel)),
                (options) => new PercentModel(
                    AgnosticNumberParserFactory.GetParser(AgnosticNumberParserType.Percentage, new PortugueseNumberParserConfiguration()),
                    new Portuguese.PercentageExtractor())
            },
            {
                (Culture.French, typeof(NumberModel)),
                (options) => new NumberModel(
                    AgnosticNumberParserFactory.GetParser(AgnosticNumberParserType.Number, new FrenchNumberParserConfiguration()),
                    new French.NumberExtractor(NumberMode.PureNumber))
            },
            {
                (Culture.French, typeof(OrdinalModel)),
                (options) => new OrdinalModel(
                    AgnosticNumberParserFactory.GetParser(AgnosticNumberParserType.Ordinal, new FrenchNumberParserConfiguration()),
                    new French.OrdinalExtractor())
            },
            {
                (Culture.French, typeof(PercentModel)),
                (options) => new PercentModel(
                    AgnosticNumberParserFactory.GetParser(AgnosticNumberParserType.Percentage, new FrenchNumberParserConfiguration()),
                    new French.PercentageExtractor())
            },
            {
                (Culture.German, typeof(NumberModel)),
                (options) => new NumberModel(
                    AgnosticNumberParserFactory.GetParser(AgnosticNumberParserType.Number, new GermanNumberParserConfiguration()),
                    German.NumberExtractor.GetInstance(NumberMode.PureNumber))
            },
            {
                (Culture.German, typeof(OrdinalModel)),
                (options) => new OrdinalModel(
                    AgnosticNumberParserFactory.GetParser(AgnosticNumberParserType.Ordinal, new GermanNumberParserConfiguration()),
                    German.OrdinalExtractor.GetInstance())
            },
            {
                (Culture.German, typeof(PercentModel)),
                (options) => new PercentModel(
                    AgnosticNumberParserFactory.GetParser(AgnosticNumberParserType.Percentage, new GermanNumberParserConfiguration()),
                    new German.PercentageExtractor())
            }
        };

        public static IModel GetNumberModel(string culture, NumberOptions options = NumberOptions.None)
        {
            return factory.GetModel<NumberModel>(culture, options);
        }

        public static IModel GetOrdinalModel(string culture, NumberOptions options = NumberOptions.None)
        {
            return factory.GetModel<OrdinalModel>(culture, options);
        }

        public static IModel GetPercentageModel(string culture, NumberOptions options = NumberOptions.None)
        {
            return factory.GetModel<PercentModel>(culture, options);
        }

        public static IModel GetNumberRangeModel(string culture, NumberOptions options = NumberOptions.None)
        {
            return factory.GetModel<NumberRangeModel>(culture, options);
        }
    }
}

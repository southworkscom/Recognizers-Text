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
                ModelFactoryKeyGenerator.Generate<NumberModel>(Culture.English),
                (options) => new NumberModel(
                    AgnosticNumberParserFactory.GetParser(AgnosticNumberParserType.Number, new EnglishNumberParserConfiguration()),
                    English.NumberExtractor.GetInstance(NumberMode.PureNumber))
            },
            {
                ModelFactoryKeyGenerator.Generate<OrdinalModel>(Culture.English),
                (options) => new OrdinalModel(
                    AgnosticNumberParserFactory.GetParser(AgnosticNumberParserType.Ordinal, new EnglishNumberParserConfiguration()),
                    English.OrdinalExtractor.GetInstance())
            },
            {
                ModelFactoryKeyGenerator.Generate<PercentModel>(Culture.English),
                (options) => new PercentModel(
                    AgnosticNumberParserFactory.GetParser(AgnosticNumberParserType.Percentage, new EnglishNumberParserConfiguration()),
                    new English.PercentageExtractor())
            },
            {
                ModelFactoryKeyGenerator.Generate<NumberModel>(Culture.Chinese),
                (options) => new NumberModel(
                    AgnosticNumberParserFactory.GetParser(AgnosticNumberParserType.Number, new ChineseNumberParserConfiguration()),
                    new Chinese.NumberExtractor())
            },
            {
                ModelFactoryKeyGenerator.Generate<OrdinalModel>(Culture.Chinese),
                (options) => new OrdinalModel(
                    AgnosticNumberParserFactory.GetParser(AgnosticNumberParserType.Ordinal, new ChineseNumberParserConfiguration()),
                    new Chinese.OrdinalExtractor())
            },
            {
                ModelFactoryKeyGenerator.Generate<PercentModel>(Culture.Chinese),
                (options) => new PercentModel(
                    AgnosticNumberParserFactory.GetParser(AgnosticNumberParserType.Percentage, new ChineseNumberParserConfiguration()),
                    new Chinese.PercentageExtractor())
            },
            {
                ModelFactoryKeyGenerator.Generate<NumberModel>(Culture.Spanish),
                (options) => new NumberModel(
                    AgnosticNumberParserFactory.GetParser(AgnosticNumberParserType.Number, new SpanishNumberParserConfiguration()),
                    new Spanish.NumberExtractor(NumberMode.PureNumber))
            },
            {
                ModelFactoryKeyGenerator.Generate<OrdinalModel>(Culture.Spanish),
                (options) => new OrdinalModel(
                    AgnosticNumberParserFactory.GetParser(AgnosticNumberParserType.Ordinal, new SpanishNumberParserConfiguration()),
                    new Spanish.OrdinalExtractor())
            },
            {
                ModelFactoryKeyGenerator.Generate<PercentModel>(Culture.Spanish),
                (options) => new PercentModel(
                    AgnosticNumberParserFactory.GetParser(AgnosticNumberParserType.Percentage, new SpanishNumberParserConfiguration()),
                    new Spanish.PercentageExtractor())
            },
            {
                ModelFactoryKeyGenerator.Generate<NumberModel>(Culture.Portuguese),
                (options) => new NumberModel(
                    AgnosticNumberParserFactory.GetParser(AgnosticNumberParserType.Number, new PortugueseNumberParserConfiguration()),
                    new Portuguese.NumberExtractor(NumberMode.PureNumber))
            },
            {
                ModelFactoryKeyGenerator.Generate<OrdinalModel>(Culture.Portuguese),
                (options) => new OrdinalModel(
                    AgnosticNumberParserFactory.GetParser(AgnosticNumberParserType.Ordinal, new PortugueseNumberParserConfiguration()),
                    new Portuguese.OrdinalExtractor())
            },
            {
                ModelFactoryKeyGenerator.Generate<PercentModel>(Culture.Portuguese),
                (options) => new PercentModel(
                    AgnosticNumberParserFactory.GetParser(AgnosticNumberParserType.Percentage, new PortugueseNumberParserConfiguration()),
                    new Portuguese.PercentageExtractor())
            },
            {
                ModelFactoryKeyGenerator.Generate<NumberModel>(Culture.French),
                (options) => new NumberModel(
                    AgnosticNumberParserFactory.GetParser(AgnosticNumberParserType.Number, new FrenchNumberParserConfiguration()),
                    new French.NumberExtractor(NumberMode.PureNumber))
            },
            {
                ModelFactoryKeyGenerator.Generate<OrdinalModel>(Culture.French),
                (options) => new OrdinalModel(
                    AgnosticNumberParserFactory.GetParser(AgnosticNumberParserType.Ordinal, new FrenchNumberParserConfiguration()),
                    new French.OrdinalExtractor())
            },
            {
                ModelFactoryKeyGenerator.Generate<PercentModel>(Culture.French),
                (options) => new PercentModel(
                    AgnosticNumberParserFactory.GetParser(AgnosticNumberParserType.Percentage, new FrenchNumberParserConfiguration()),
                    new French.PercentageExtractor())
            },
            {
                ModelFactoryKeyGenerator.Generate<NumberModel>(Culture.German),
                (options) => new NumberModel(
                    AgnosticNumberParserFactory.GetParser(AgnosticNumberParserType.Number, new GermanNumberParserConfiguration()),
                    German.NumberExtractor.GetInstance(NumberMode.PureNumber))
            },
            {
                ModelFactoryKeyGenerator.Generate<OrdinalModel>(Culture.German),
                (options) => new OrdinalModel(
                    AgnosticNumberParserFactory.GetParser(AgnosticNumberParserType.Ordinal, new GermanNumberParserConfiguration()),
                    German.OrdinalExtractor.GetInstance())
            },
            {
                ModelFactoryKeyGenerator.Generate<PercentModel>(Culture.German),
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
    }
}

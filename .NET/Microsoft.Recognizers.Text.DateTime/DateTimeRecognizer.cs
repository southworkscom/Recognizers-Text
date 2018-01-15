using Microsoft.Recognizers.Text.DateTime.Chinese;
using Microsoft.Recognizers.Text.DateTime.English;
using Microsoft.Recognizers.Text.DateTime.French;
using Microsoft.Recognizers.Text.DateTime.Portuguese;
using Microsoft.Recognizers.Text.DateTime.Spanish;

namespace Microsoft.Recognizers.Text.DateTime
{
    public class DateTimeRecognizer
    {
        private static ModelFactory<DateTimeOptions> factory = new ModelFactory<DateTimeOptions>()
        {
            {
                ModelFactoryKeyGenerator.Generate<DateTimeModel>(Culture.English),
                (options) => new DateTimeModel(
                            new BaseMergedParser(new EnglishMergedParserConfiguration(options)),
                            new BaseMergedExtractor(new EnglishMergedExtractorConfiguration(options)))
            },
            {
                ModelFactoryKeyGenerator.Generate<DateTimeModel>(Culture.Chinese),
                (options) => new DateTimeModel(
                            new FullDateTimeParser(new ChineseDateTimeParserConfiguration(options)),
                            new MergedExtractorChs(options))
            },
            {
                ModelFactoryKeyGenerator.Generate<DateTimeModel>(Culture.Spanish),
                (options) => new DateTimeModel(
                            new BaseMergedParser(new SpanishMergedParserConfiguration(options)),
                            new BaseMergedExtractor(new SpanishMergedExtractorConfiguration(options)))
            },
            {
                ModelFactoryKeyGenerator.Generate<DateTimeModel>(Culture.French),
                (options) => new DateTimeModel(
                            new BaseMergedParser(new FrenchMergedParserConfiguration(options)),
                            new BaseMergedExtractor(new FrenchMergedExtractorConfiguration(options)))
            },
            {
                ModelFactoryKeyGenerator.Generate<DateTimeModel>(Culture.Portuguese),
                (options) => new DateTimeModel(
                            new BaseMergedParser(new PortugueseMergedParserConfiguration(options)),
                            new BaseMergedExtractor(new PortugueseMergedExtractorConfiguration(options)))
            }
        };

        public static DateTimeModel GetDateTimeModel(string culture, DateTimeOptions options = DateTimeOptions.None)
        {
            return (DateTimeModel)factory.GetModel<DateTimeModel>(culture, options);
        }
    }
}
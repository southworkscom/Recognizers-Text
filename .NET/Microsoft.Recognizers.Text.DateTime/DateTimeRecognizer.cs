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
                (Culture.English, typeof(DateTimeModel)),
                (options) => new DateTimeModel(
                            new BaseMergedParser(new EnglishMergedParserConfiguration(options)),
                            new BaseMergedExtractor(new EnglishMergedExtractorConfiguration(options)))
            },
            {
                (Culture.Chinese, typeof(DateTimeModel)),
                (options) => new DateTimeModel(
                            new FullDateTimeParser(new ChineseDateTimeParserConfiguration(options)),
                            new MergedExtractorChs(options))
            },
            {
                (Culture.Spanish, typeof(DateTimeModel)),
                (options) => new DateTimeModel(
                            new BaseMergedParser(new SpanishMergedParserConfiguration(options)),
                            new BaseMergedExtractor(new SpanishMergedExtractorConfiguration(options)))
            },
            {
                (Culture.French, typeof(DateTimeModel)),
                (options) => new DateTimeModel(
                            new BaseMergedParser(new FrenchMergedParserConfiguration(options)),
                            new BaseMergedExtractor(new FrenchMergedExtractorConfiguration(options)))
            },
            {
                (Culture.Portuguese, typeof(DateTimeModel)),
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
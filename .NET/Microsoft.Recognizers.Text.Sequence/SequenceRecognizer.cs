using System;
using System.Collections.Generic;

using Microsoft.Recognizers.Text.Sequence.English;

namespace Microsoft.Recognizers.Text.Sequence
{
    public class SequenceRecognizer
    {
        private static ModelFactory<SequenceOptions> factory = new ModelFactory<SequenceOptions>()
        {
             {
                (Culture.English, typeof(PhoneNumberModel)),
                (options) => new PhoneNumberModel(new PhoneNumberParser(), new PhoneNumberExtractor())
            }
        };

        public static IModel GetPhoneNumberModel(string culture, SequenceOptions options = SequenceOptions.None)
        {
            return factory.GetModel<PhoneNumberModel>(culture, options);
        }

    }
}

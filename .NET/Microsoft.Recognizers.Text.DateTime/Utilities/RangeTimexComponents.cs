using System;
using System.Collections.Generic;
using System.Collections.Immutable;
using System.Globalization;
using System.Text;
using DateObject = System.DateTime;

namespace Microsoft.Recognizers.Text.DateTime
{
    public class RangeTimexComponents
    {
        public string BeginTimex;

        public string EndTimex;

        public string DurationTimex;

        public bool IsValid = false;
    }
}

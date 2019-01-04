using System.Text.RegularExpressions;

namespace Microsoft.Recognizers.Text.DateTime.Chinese
{
    public class DateTimeExtra<T>
    {
        public GroupCollection NamedEntity { get; set; }

        public T Type { get; set; }
    }
}
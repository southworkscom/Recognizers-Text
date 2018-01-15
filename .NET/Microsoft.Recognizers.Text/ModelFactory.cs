using System;
using System.Collections.Generic;

namespace Microsoft.Recognizers.Text
{
    public class ModelFactory<TModelOptions> : Dictionary<(string culture, Type modelType), Func<TModelOptions, IModel>>
    {
        public IModel GetModel<T>(string culture, TModelOptions options)
        {
            if (string.IsNullOrEmpty(culture))
            {
                throw new ArgumentNullException("culture", "Culture is required.");
            }

            var key = (culture, typeof(T));
            if (this.ContainsKey(key))
            {
                var factoryMethod = this[key];
                return factoryMethod(options);
            }

            throw new ArgumentException($"Could not find Model with the specified configuration: {culture}, {typeof(T).ToString()}, {options.ToString()})");
        }
    }
}

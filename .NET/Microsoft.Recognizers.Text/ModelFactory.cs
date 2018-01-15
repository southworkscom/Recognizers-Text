using System;
using System.Collections.Concurrent;
using System.Collections.Generic;

namespace Microsoft.Recognizers.Text
{
    public class ModelFactory<TModelOptions> : Dictionary<(string culture, Type modelType), Func<TModelOptions, IModel>>
    {
        private static ConcurrentDictionary<(string culture, Type modelType, string modelOptions), IModel> cache = new ConcurrentDictionary<(string culture, Type modelType, string modelOptions), IModel>();

        public IModel GetModel<T>(string culture, TModelOptions options)
        {
            if (string.IsNullOrEmpty(culture))
            {
                throw new ArgumentNullException("culture", "Culture is required.");
            }

            var cacheKey = (culture.ToLowerInvariant(), typeof(T), options.ToString());
            if(cache.ContainsKey(cacheKey))
            {
                return cache[cacheKey];
            }

            var key = ModelFactoryKeyGenerator.Generate<T>(culture);
            if (this.ContainsKey(key))
            {
                var factoryMethod = this[key];
                var model = factoryMethod(options);

                cache[cacheKey] = model;

                return model;
            }

            throw new ArgumentException($"Could not find Model with the specified configuration: {culture}, {typeof(T).ToString()}");
        }
    }

    public static class ModelFactoryKeyGenerator
    {
        public static (string culture, Type modelType) Generate<T>(string culture)
        {
            return (culture.ToLowerInvariant(), typeof(T));
        }
    }
}

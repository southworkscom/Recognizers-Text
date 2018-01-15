using System.Collections.Generic;
using Microsoft.Recognizers.Text.Number;

namespace Microsoft.Recognizers.Text.NumberWithUnit
{
    public static class NumberWithUnitRecognizer
    {
        private static ModelFactory<NumberWithUnitOptions> factory = new ModelFactory<NumberWithUnitOptions>()
        {
            {
                (Culture.English, typeof(CurrencyModel)),
                (options) => new CurrencyModel(new Dictionary<IExtractor, IParser>
                            {
                                {
                                    new NumberWithUnitExtractor(new English.CurrencyExtractorConfiguration()),
                                    new NumberWithUnitParser(new English.CurrencyParserConfiguration())
                                }
                            })
            },
            {
                (Culture.English, typeof(TemperatureModel)),
                (options) => new TemperatureModel(new Dictionary<IExtractor, IParser>
                            {
                                {
                                    new NumberWithUnitExtractor(new English.TemperatureExtractorConfiguration()),
                                    new NumberWithUnitParser(new English.TemperatureParserConfiguration())
                                }
                            })
            },
            {
                (Culture.English, typeof(DimensionModel)),
                (options) => new DimensionModel(new Dictionary<IExtractor, IParser>
                            {
                                {
                                    new NumberWithUnitExtractor(new English.DimensionExtractorConfiguration()),
                                    new NumberWithUnitParser(new English.DimensionParserConfiguration())
                                }
                            })
            },
            {
                (Culture.English, typeof(AgeModel)),
                (options) => new AgeModel(new Dictionary<IExtractor, IParser>
                            {
                                {
                                    new NumberWithUnitExtractor(new English.AgeExtractorConfiguration()),
                                    new NumberWithUnitParser(new English.AgeParserConfiguration())
                                }
                            })
            },
            {
                (Culture.Chinese, typeof (CurrencyModel)),
                (options) => new CurrencyModel(new Dictionary<IExtractor, IParser>
                            {
                                {
                                    new NumberWithUnitExtractor(new Chinese.CurrencyExtractorConfiguration()),
                                    new NumberWithUnitParser(new Chinese.CurrencyParserConfiguration())
                                },
                                {
                                    new NumberWithUnitExtractor(new English.CurrencyExtractorConfiguration()),
                                    new NumberWithUnitParser(new English.CurrencyParserConfiguration())
                                }
                            })
            },
            {
                (Culture.Chinese, typeof (TemperatureModel)),
                 (options) => new TemperatureModel(new Dictionary<IExtractor, IParser>
                            {
                                {
                                    new NumberWithUnitExtractor(new Chinese.TemperatureExtractorConfiguration()),
                                    new NumberWithUnitParser(new Chinese.TemperatureParserConfiguration())
                                },
                                {
                                    new NumberWithUnitExtractor(new English.TemperatureExtractorConfiguration()),
                                    new NumberWithUnitParser(new English.TemperatureParserConfiguration())
                                }
                            })
            },
            {
                (Culture.Chinese, typeof (DimensionModel)),
                 (options) => new DimensionModel(new Dictionary<IExtractor, IParser>
                            {
                                {
                                    new NumberWithUnitExtractor(new Chinese.DimensionExtractorConfiguration()),
                                    new NumberWithUnitParser(new Chinese.DimensionParserConfiguration())
                                },
                                {
                                    new NumberWithUnitExtractor(new English.DimensionExtractorConfiguration()),
                                    new NumberWithUnitParser(new English.DimensionParserConfiguration())
                                }
                            })
            },
            {
                (Culture.Chinese, typeof (AgeModel)),
                (options) => new AgeModel(new Dictionary<IExtractor, IParser>
                            {
                                {
                                    new NumberWithUnitExtractor(new Chinese.AgeExtractorConfiguration()),
                                    new NumberWithUnitParser(new Chinese.AgeParserConfiguration())
                                },
                                {
                                    new NumberWithUnitExtractor(new English.AgeExtractorConfiguration()),
                                    new NumberWithUnitParser(new English.AgeParserConfiguration())
                                }
                            })
            },
            {
                (Culture.Spanish, typeof(CurrencyModel)),
                (options) => new CurrencyModel(new Dictionary<IExtractor, IParser>
                            {
                                {
                                    new NumberWithUnitExtractor(new Spanish.CurrencyExtractorConfiguration()),
                                    new NumberWithUnitParser(new Spanish.CurrencyParserConfiguration())
                                }
                            })
            },
            {
                (Culture.Spanish, typeof(TemperatureModel)),
                (options) => new TemperatureModel(new Dictionary<IExtractor, IParser>
                            {
                                {
                                    new NumberWithUnitExtractor(new Spanish.TemperatureExtractorConfiguration()),
                                    new NumberWithUnitParser(new Spanish.TemperatureParserConfiguration())
                                }
                            })
            },
            {
                (Culture.Spanish, typeof(DimensionModel)),
                (options) => new DimensionModel(new Dictionary<IExtractor, IParser>
                            {
                                {
                                    new NumberWithUnitExtractor(new Spanish.DimensionExtractorConfiguration()),
                                    new NumberWithUnitParser(new Spanish.DimensionParserConfiguration())
                                }
                            })
            },
            {
                (Culture.Spanish, typeof(AgeModel)),
                (options) => new AgeModel(new Dictionary<IExtractor, IParser>
                            {
                                {
                                    new NumberWithUnitExtractor(new Spanish.AgeExtractorConfiguration()),
                                    new NumberWithUnitParser(new Spanish.AgeParserConfiguration())
                                }
                            })
            },
            {
                (Culture.Portuguese, typeof(CurrencyModel)),
                (options) => new CurrencyModel(new Dictionary<IExtractor, IParser>
                            {
                                {
                                    new NumberWithUnitExtractor(new Portuguese.CurrencyExtractorConfiguration()),
                                    new NumberWithUnitParser(new Portuguese.CurrencyParserConfiguration())
                                }
                            })
            },
            {
                (Culture.Portuguese, typeof(TemperatureModel)),
                (options) => new TemperatureModel(new Dictionary<IExtractor, IParser>
                            {
                                {
                                    new NumberWithUnitExtractor(new Portuguese.TemperatureExtractorConfiguration()),
                                    new NumberWithUnitParser(new Portuguese.TemperatureParserConfiguration())
                                }
                            })
            },
            {
                (Culture.Portuguese, typeof(DimensionModel)),
                (options) => new DimensionModel(new Dictionary<IExtractor, IParser>
                            {
                                {
                                    new NumberWithUnitExtractor(new Portuguese.DimensionExtractorConfiguration()),
                                    new NumberWithUnitParser(new Portuguese.DimensionParserConfiguration())
                                }
                            })
            },
            {
                (Culture.Portuguese, typeof(AgeModel)),
                (options) => new AgeModel(new Dictionary<IExtractor, IParser>
                            {
                                {
                                    new NumberWithUnitExtractor(new Portuguese.AgeExtractorConfiguration()),
                                    new NumberWithUnitParser(new Portuguese.AgeParserConfiguration())
                                }
                            })
            },
            {
                (Culture.French, typeof(CurrencyModel)),
                (options) => new CurrencyModel(new Dictionary<IExtractor, IParser>
                            {
                                {
                                    new NumberWithUnitExtractor(new French.CurrencyExtractorConfiguration()),
                                    new NumberWithUnitParser(new French.CurrencyParserConfiguration())
                                }
                            })
            },
            {
                (Culture.French, typeof(TemperatureModel)),
                (options) => new TemperatureModel(new Dictionary<IExtractor, IParser>
                            {
                                {
                                    new NumberWithUnitExtractor(new French.TemperatureExtractorConfiguration()),
                                    new NumberWithUnitParser(new French.TemperatureParserConfiguration())
                                }
                            })
            },
            {
                (Culture.French, typeof(DimensionModel)),
                (options) => new DimensionModel(new Dictionary<IExtractor, IParser>
                            {
                                {
                                    new NumberWithUnitExtractor(new French.DimensionExtractorConfiguration()),
                                    new NumberWithUnitParser(new French.DimensionParserConfiguration())
                                }
                            })
            },
            {
                (Culture.French, typeof(AgeModel)),
                (options) => new AgeModel(new Dictionary<IExtractor, IParser>
                            {
                                {
                                    new NumberWithUnitExtractor(new French.AgeExtractorConfiguration()),
                                    new NumberWithUnitParser(new French.AgeParserConfiguration())
                                }
                            })
            },
            {
                (Culture.German, typeof(CurrencyModel)),
                (options) => new CurrencyModel(
                             new Dictionary<IExtractor, IParser>
                             {
                                {
                                    new NumberWithUnitExtractor(new German.CurrencyExtractorConfiguration()),
                                    new NumberWithUnitParser(new German.CurrencyParserConfiguration())
                                }
                             })
            },
            {
                (Culture.German, typeof(TemperatureModel)),
                (options) => new TemperatureModel(
                             new Dictionary<IExtractor, IParser>
                             {
                                {
                                    new NumberWithUnitExtractor(new German.TemperatureExtractorConfiguration()),
                                    new NumberWithUnitParser(new German.TemperatureParserConfiguration())
                                }
                             })
            },
            {
                (Culture.German, typeof(DimensionModel)),
                (options) => new DimensionModel(
                             new Dictionary<IExtractor, IParser>
                             {
                                {
                                    new NumberWithUnitExtractor(new German.DimensionExtractorConfiguration()),
                                    new NumberWithUnitParser(new German.DimensionParserConfiguration())
                                }
                             })
            },
            {
                (Culture.German, typeof(AgeModel)),
                (options) => new AgeModel(
                            new Dictionary<IExtractor, IParser>
                            {
                                {
                                    new NumberWithUnitExtractor(new German.AgeExtractorConfiguration()),
                                    new NumberWithUnitParser(new German.AgeParserConfiguration())
                                }
                            })
            },
        };

        public static IModel GetCurrencyModel(string culture, NumberWithUnitOptions options = NumberWithUnitOptions.None)
        {
            return factory.GetModel<CurrencyModel>(culture, options);
        }

        public static IModel GetTemperatureModel(string culture, NumberWithUnitOptions options = NumberWithUnitOptions.None)
        {
            return factory.GetModel<TemperatureModel>(culture, options);
        }

        public static IModel GetDimensionModel(string culture, NumberWithUnitOptions options = NumberWithUnitOptions.None)
        {
            return factory.GetModel<DimensionModel>(culture, options);
        }

        public static IModel GetAgeModel(string culture, NumberWithUnitOptions options = NumberWithUnitOptions.None)
        {
            return factory.GetModel<AgeModel>(culture, options);
        }
    }
}

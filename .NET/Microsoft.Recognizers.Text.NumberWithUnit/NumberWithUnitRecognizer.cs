using System.Collections.Generic;
using Microsoft.Recognizers.Text.Number;

namespace Microsoft.Recognizers.Text.NumberWithUnit
{
    public static class NumberWithUnitRecognizer
    {
        private static ModelFactory<NumberWithUnitOptions> factory = new ModelFactory<NumberWithUnitOptions>()
        {
            {
                ModelFactoryKeyGenerator.Generate<CurrencyModel>(Culture.English),
                (options) => new CurrencyModel(new Dictionary<IExtractor, IParser>
                            {
                                {
                                    new NumberWithUnitExtractor(new English.CurrencyExtractorConfiguration()),
                                    new NumberWithUnitParser(new English.CurrencyParserConfiguration())
                                }
                            })
            },
            {
                ModelFactoryKeyGenerator.Generate<TemperatureModel>(Culture.English),
                (options) => new TemperatureModel(new Dictionary<IExtractor, IParser>
                            {
                                {
                                    new NumberWithUnitExtractor(new English.TemperatureExtractorConfiguration()),
                                    new NumberWithUnitParser(new English.TemperatureParserConfiguration())
                                }
                            })
            },
            {
                ModelFactoryKeyGenerator.Generate<DimensionModel>(Culture.English),
                (options) => new DimensionModel(new Dictionary<IExtractor, IParser>
                            {
                                {
                                    new NumberWithUnitExtractor(new English.DimensionExtractorConfiguration()),
                                    new NumberWithUnitParser(new English.DimensionParserConfiguration())
                                }
                            })
            },
            {
                ModelFactoryKeyGenerator.Generate<AgeModel>(Culture.English),
                (options) => new AgeModel(new Dictionary<IExtractor, IParser>
                            {
                                {
                                    new NumberWithUnitExtractor(new English.AgeExtractorConfiguration()),
                                    new NumberWithUnitParser(new English.AgeParserConfiguration())
                                }
                            })
            },
            {
                ModelFactoryKeyGenerator.Generate<CurrencyModel>(Culture.Chinese),
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
                ModelFactoryKeyGenerator.Generate<TemperatureModel>(Culture.Chinese),
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
                ModelFactoryKeyGenerator.Generate<DimensionModel>(Culture.Chinese),
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
                ModelFactoryKeyGenerator.Generate<AgeModel>(Culture.Chinese),
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
                ModelFactoryKeyGenerator.Generate<CurrencyModel>(Culture.Spanish),
                (options) => new CurrencyModel(new Dictionary<IExtractor, IParser>
                            {
                                {
                                    new NumberWithUnitExtractor(new Spanish.CurrencyExtractorConfiguration()),
                                    new NumberWithUnitParser(new Spanish.CurrencyParserConfiguration())
                                }
                            })
            },
            {
                ModelFactoryKeyGenerator.Generate<TemperatureModel>(Culture.Spanish),
                (options) => new TemperatureModel(new Dictionary<IExtractor, IParser>
                            {
                                {
                                    new NumberWithUnitExtractor(new Spanish.TemperatureExtractorConfiguration()),
                                    new NumberWithUnitParser(new Spanish.TemperatureParserConfiguration())
                                }
                            })
            },
            {
                ModelFactoryKeyGenerator.Generate<DimensionModel>(Culture.Spanish),
                (options) => new DimensionModel(new Dictionary<IExtractor, IParser>
                            {
                                {
                                    new NumberWithUnitExtractor(new Spanish.DimensionExtractorConfiguration()),
                                    new NumberWithUnitParser(new Spanish.DimensionParserConfiguration())
                                }
                            })
            },
            {
                ModelFactoryKeyGenerator.Generate<AgeModel>(Culture.Spanish),
                (options) => new AgeModel(new Dictionary<IExtractor, IParser>
                            {
                                {
                                    new NumberWithUnitExtractor(new Spanish.AgeExtractorConfiguration()),
                                    new NumberWithUnitParser(new Spanish.AgeParserConfiguration())
                                }
                            })
            },
            {
                ModelFactoryKeyGenerator.Generate<CurrencyModel>(Culture.Portuguese),
                (options) => new CurrencyModel(new Dictionary<IExtractor, IParser>
                            {
                                {
                                    new NumberWithUnitExtractor(new Portuguese.CurrencyExtractorConfiguration()),
                                    new NumberWithUnitParser(new Portuguese.CurrencyParserConfiguration())
                                }
                            })
            },
            {
                ModelFactoryKeyGenerator.Generate<TemperatureModel>(Culture.Portuguese),
                (options) => new TemperatureModel(new Dictionary<IExtractor, IParser>
                            {
                                {
                                    new NumberWithUnitExtractor(new Portuguese.TemperatureExtractorConfiguration()),
                                    new NumberWithUnitParser(new Portuguese.TemperatureParserConfiguration())
                                }
                            })
            },
            {
                ModelFactoryKeyGenerator.Generate<DimensionModel>(Culture.Portuguese),
                (options) => new DimensionModel(new Dictionary<IExtractor, IParser>
                            {
                                {
                                    new NumberWithUnitExtractor(new Portuguese.DimensionExtractorConfiguration()),
                                    new NumberWithUnitParser(new Portuguese.DimensionParserConfiguration())
                                }
                            })
            },
            {
                ModelFactoryKeyGenerator.Generate<AgeModel>(Culture.Portuguese),
                (options) => new AgeModel(new Dictionary<IExtractor, IParser>
                            {
                                {
                                    new NumberWithUnitExtractor(new Portuguese.AgeExtractorConfiguration()),
                                    new NumberWithUnitParser(new Portuguese.AgeParserConfiguration())
                                }
                            })
            },
            {
                ModelFactoryKeyGenerator.Generate<CurrencyModel>(Culture.French),
                (options) => new CurrencyModel(new Dictionary<IExtractor, IParser>
                            {
                                {
                                    new NumberWithUnitExtractor(new French.CurrencyExtractorConfiguration()),
                                    new NumberWithUnitParser(new French.CurrencyParserConfiguration())
                                }
                            })
            },
            {
                ModelFactoryKeyGenerator.Generate<TemperatureModel>(Culture.French),
                (options) => new TemperatureModel(new Dictionary<IExtractor, IParser>
                            {
                                {
                                    new NumberWithUnitExtractor(new French.TemperatureExtractorConfiguration()),
                                    new NumberWithUnitParser(new French.TemperatureParserConfiguration())
                                }
                            })
            },
            {
                ModelFactoryKeyGenerator.Generate<DimensionModel>(Culture.French),
                (options) => new DimensionModel(new Dictionary<IExtractor, IParser>
                            {
                                {
                                    new NumberWithUnitExtractor(new French.DimensionExtractorConfiguration()),
                                    new NumberWithUnitParser(new French.DimensionParserConfiguration())
                                }
                            })
            },
            {
                ModelFactoryKeyGenerator.Generate<AgeModel>(Culture.French),
                (options) => new AgeModel(new Dictionary<IExtractor, IParser>
                            {
                                {
                                    new NumberWithUnitExtractor(new French.AgeExtractorConfiguration()),
                                    new NumberWithUnitParser(new French.AgeParserConfiguration())
                                }
                            })
            }
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

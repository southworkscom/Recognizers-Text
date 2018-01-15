﻿using System;
using System.Collections.Generic;
using System.Linq;

using Microsoft.Recognizers.Text;
using Microsoft.Recognizers.Text.DateTime;
using Microsoft.Recognizers.Text.Number;
using Microsoft.Recognizers.Text.NumberWithUnit;

using Newtonsoft.Json;

namespace SimpleConsole
{
    class Program
    {
        // Use English for the Recognizers culture
        private const string defaultCulture = Culture.English;

        static void Main(string[] args)
        {
            ShowIntro();

            while (true)
            {
                // Read the text to recognize
                Console.WriteLine("Enter the text to recognize:");
                string input = Console.ReadLine().Trim();
                Console.WriteLine();

                if (input.ToLower() == "exit")
                {
                    // Close application if user types "exit"
                    break;
                }
                else
                {
                    // Validate input 
                    if (input.Length > 0)
                    {
                        // Retrieve all the parsers and call 'Parse' to recognize all the values from the user input
                        var results = GetModels()
                            .Select(parser => parser.Parse(input))
                            .SelectMany(a => a);

                        // Write output
                        Console.WriteLine(results.Count() > 0 ? (string.Format("I found the following entities ({0:d}):", results.Count())) : "I found no entities.");
                        results.ToList().ForEach(result => Console.WriteLine(JsonConvert.SerializeObject(result, Formatting.Indented)));
                        Console.WriteLine();
                    }
                }
            }
        }
        
        /// <summary>
        /// Get all recognizers model instances.
        /// </summary>
        /// <returns>A list of all the existing recognizer's models</returns>
        private static IEnumerable<IModel> GetModels()
        {
            return new IModel[]
            {
                // Add Number recognizer - This recognizer will find any number from the input
                // E.g "I have two apples" will return "2".
                NumberRecognizer.GetNumberModel(defaultCulture),
                
                // Add Ordinal number recognizer - This recognizer will find any ordinal number
                // E.g "eleventh" will return "11".
                NumberRecognizer.GetOrdinalModel(defaultCulture),

                // Add Percentage recognizer - This recognizer will find any number presented as percentage
                // E.g "one hundred percents" will return "100%"
                NumberRecognizer.GetPercentageModel(defaultCulture),

                // Add Age recognizer - This recognizer will find any age number presented
                // E.g "After ninety five years of age, perspectives change" will return "95 Year"
                NumberWithUnitRecognizer.GetAgeModel(defaultCulture),

                // Add Currency recognizer - This recognizer will find any currency presented
                // E.g "Interest expense in the 1988 third quarter was $ 75.3 million" will return "75300000 Dollar"
                NumberWithUnitRecognizer.GetCurrencyModel(defaultCulture),

                // Add Dimension recognizer - This recognizer will find any dimension presented
                // E.g "The six-mile trip to my airport hotel that had taken 20 minutes earlier in the day took more than three hours." will return "6 Mile"
                NumberWithUnitRecognizer.GetDimensionModel(defaultCulture),

                // Add Temperature recognizer - This recognizer will find any temperature presented
                // E.g "Set the temperature to 30 degrees celsius" will return "30 C"
                NumberWithUnitRecognizer.GetTemperatureModel(defaultCulture),
                
                // Add Datetime recognizer - This model will find any Date even if its write in coloquial language - 
                // E.g "I'll go back 8pm today" will return "2017-10-04 20:00:00"
                DateTimeRecognizer.GetDateTimeModel(defaultCulture)
            };
        }

        /// <summary>
        /// Introduction
        /// </summary>
        private static void ShowIntro()
        {
            Console.WriteLine("Welcome to the Recognizer's Sample console application!");
            Console.WriteLine("To try the recognizers enter a phrase and let us show you the different outputs for each recognizer or just type 'exit' to leave the application.");
            Console.WriteLine();
            Console.WriteLine("Here are some examples you could try:");
            Console.WriteLine();
            Console.WriteLine("\" I want twenty meters of cable for tomorrow\"");
            Console.WriteLine("\" I'll be available tomorrow from 11am to 2pm to receive up to 5kg of sugar\"");
            Console.WriteLine("\" I'll be out between 4 and 22 this month\"");
            Console.WriteLine("\" I was the fifth person to finish the 10 km race\"");
            Console.WriteLine("\" The temperature this night will be of 40 deg celsius\"");
            Console.WriteLine("\" The american stock exchange said a seat was sold for down $ 5,000 from the previous sale last friday\"");
            Console.WriteLine("\" It happened when the baby was only ten months old\"");
            Console.WriteLine();
        }
    }
}

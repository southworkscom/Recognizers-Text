package com.microsoft.recognizers.text.tests.datetime;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import com.google.common.collect.ImmutableMap;
import com.microsoft.recognizers.text.Culture;
import com.microsoft.recognizers.text.ExtractResult;
import com.microsoft.recognizers.text.ModelResult;
import com.microsoft.recognizers.text.ResolutionKey;
import com.microsoft.recognizers.text.datetime.english.extractors.EnglishDateExtractorConfiguration;
import com.microsoft.recognizers.text.datetime.english.extractors.EnglishHolidayExtractorConfiguration;
import com.microsoft.recognizers.text.datetime.english.extractors.EnglishDatePeriodExtractorConfiguration;
import com.microsoft.recognizers.text.datetime.extractors.BaseDateExtractor;
import com.microsoft.recognizers.text.datetime.extractors.BaseHolidayExtractor;
import com.microsoft.recognizers.text.datetime.extractors.BaseDatePeriodExtractor;
import com.microsoft.recognizers.text.tests.AbstractTest;
import com.microsoft.recognizers.text.tests.TestCase;
import com.microsoft.recognizers.text.datetime.english.extractors.EnglishDurationExtractorConfiguration;
import com.microsoft.recognizers.text.datetime.extractors.BaseDurationExtractor;
import com.microsoft.recognizers.text.datetime.extractors.IDateTimeExtractor;
import org.javatuples.Pair;
import org.junit.Assert;
import org.junit.AssumptionViolatedException;
import org.junit.runners.Parameterized;

public class DateTimeExtractorTest extends AbstractTest {

	private static final String recognizerType = "DateTime";
	
	@Parameterized.Parameters(name = "{0}")
	public static Collection<TestCase> testCases() {
			return AbstractTest.enumerateTestCases(recognizerType, "Extractor");
	}
	
	public DateTimeExtractorTest(TestCase currentCase) {
		super(currentCase);
	}

	@Override
	protected List<ModelResult> recognize(TestCase currentCase) {
		return null;
	}

	protected List<ExtractResult> extract(TestCase currentCase) {
		IDateTimeExtractor extractor = getExtractor(currentCase);
		return extractor.extract(currentCase.input, currentCase.getReferenceDateTime());
	}

	@Override
	protected void recognizeAndAssert(TestCase currentCase) {
			List<ExtractResult> results = extract(currentCase);
			assertExtractResults(currentCase, results);
	}

	public static void assertExtractResults(TestCase currentCase, List<ExtractResult> results) {
		List<ExtractResult> expectedResults = readExpectedExtractResults(ExtractResult.class, currentCase.results);
		Assert.assertEquals(getMessage(currentCase, "\"Result Count\""), expectedResults.size(), results.size());

		IntStream.range(0, expectedResults.size())
				.mapToObj(i -> Pair.with(expectedResults.get(i), results.get(i)))
				.forEach(t -> {
					ExtractResult expected = t.getValue0();
					ExtractResult actual = t.getValue1();

					Assert.assertEquals(getMessage(currentCase, "type"), expected.type, actual.type);
					Assert.assertEquals(getMessage(currentCase, "text"), expected.text, actual.text);
					Assert.assertEquals(getMessage(currentCase, "start"), expected.start, actual.start);
					Assert.assertEquals(getMessage(currentCase, "length"), expected.length, actual.length);
				});
	}

	public static IDateTimeExtractor getExtractor(TestCase currentCase) {
		return getExtractor(currentCase.language, currentCase.modelName);
	}

  	public static IDateTimeExtractor getExtractor(String language, String modelName) {
		try {
			String culture = getCultureCode(language);
			switch (culture) {
				case Culture.English:
					return getEnglishExtractor(modelName);
				default:
					throw new AssumptionViolatedException("Extractor Type/Name not supported.");
			}
		} catch (IllegalArgumentException ex) {
			throw new AssumptionViolatedException(ex.getMessage(), ex);
		}
	}

	private static IDateTimeExtractor getEnglishExtractor(String name) {
		switch (name) {
			case "DurationExtractor":
				return new BaseDurationExtractor(new EnglishDurationExtractorConfiguration());
			case "DateExtractor":
				return new BaseDateExtractor(new EnglishDateExtractorConfiguration());
			case "DatePeriodExtractor":
				return new BaseDatePeriodExtractor(new EnglishDatePeriodExtractorConfiguration());
			case "HolidayExtractor":
				return new BaseHolidayExtractor(new EnglishHolidayExtractorConfiguration());
			default:
				throw new AssumptionViolatedException("Extractor Type/Name not supported.");
		}
	}
}
package com.microsoft.recognizers.text.tests.datetime;

import com.microsoft.recognizers.text.ModelResult;
import com.microsoft.recognizers.text.ResolutionKey;
import com.microsoft.recognizers.text.datetime.DateTimeOptions;
import com.microsoft.recognizers.text.datetime.DateTimeRecognizer;
import com.microsoft.recognizers.text.tests.AbstractTest;
import com.microsoft.recognizers.text.tests.DependencyConstants;
import com.microsoft.recognizers.text.tests.NotSupportedException;
import com.microsoft.recognizers.text.tests.TestCase;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;

import org.javatuples.Pair;
import org.junit.Assert;
import org.junit.AssumptionViolatedException;
import org.junit.runners.Parameterized;

public class DateTimeTest extends AbstractTest {

    private static final String recognizerType = "DateTime";

    @Parameterized.Parameters(name = "{0}")
    public static Collection<TestCase> testCases() {
        return AbstractTest.enumerateTestCases(recognizerType, "Model");
    }

    public DateTimeTest(TestCase currentCase) {
        super(currentCase);
    }

    @Override
    protected void recognizeAndAssert(TestCase currentCase) {

        // parse
        List<ModelResult> results = recognize(currentCase);

        // assert
        assertResults(currentCase, results, getKeysToTest(currentCase));
    }
    private List<String> getKeysToTest(TestCase currentCase) {
        switch (currentCase.modelName) {
            case "DateTimeModelCalendarMode":
                return Arrays.asList(ResolutionKey.Timex, ResolutionKey.Type, ResolutionKey.Start, ResolutionKey.End);
            case "DateTimeModelSplitDateAndTime":
                return Arrays.asList(ResolutionKey.Timex, ResolutionKey.Mod,ResolutionKey.Type, ResolutionKey.Start, ResolutionKey.End);
            default:
                return Arrays.asList(ResolutionKey.Timex, ResolutionKey.Type, ResolutionKey.Value);
        }
    }

    @Override
    protected void assertModel(ModelResult expected, ModelResult actual) {
        if (expected.start != null) {
            Assert.assertEquals(getMessage(currentCase, "start"), expected.start, actual.start);
        }
        if (expected.end != null) {
            Assert.assertEquals(getMessage(currentCase, "end"), expected.end, actual.end);
        }
    }

    @Override
    protected List<ModelResult> recognize(TestCase currentCase) {

        try {
            String culture = getCultureCode(currentCase.language);
            LocalDateTime reference = currentCase.getReferenceDateTime();
            switch (currentCase.modelName) {
                case "DateExtractor":
                case "DateParser":
                case "DatePeriodExtractor":
                case "DateTimeExtractor":
                case "DateTimeModel":
                case "DateTimeParser":
                case "DateTimePeriodExtractor":
                case "DateTimePeriodParser":
                case "DurationExtractor":
                case "DurationParser":
                case "HolidayExtractor":
                case "HolidayParser":
                case "MergedExtractor":
                case "MergedParser":
                case "SetExtractor":
                case "SetParser":
                case "TimeExtractor":
                case "TimeParser":
                case "TimePeriodExtractor":
                case "TimePeriodParser":
                case "TimeZoneExtractor":
                case "TimeZoneParser":
                    return DateTimeRecognizer.recognizeDateTime(currentCase.input, culture, DateTimeOptions.None, false, reference);
                case "DateTimeModelCalendarMode":
                    return DateTimeRecognizer.recognizeDateTime(currentCase.input, culture, DateTimeOptions.CalendarMode, false, reference);
                case "DateTimeModelExperimentalMode":
                    return DateTimeRecognizer.recognizeDateTime(currentCase.input, culture, DateTimeOptions.ExperimentalMode, false, reference);
                case "DateTimeModelExtendedTypes":
                    return DateTimeRecognizer.recognizeDateTime(currentCase.input, culture, DateTimeOptions.ExtendedTypes, false, reference);
                case "DateTimeModelSplitDateAndTime":
                    return DateTimeRecognizer.recognizeDateTime(currentCase.input, culture, DateTimeOptions.SplitDateAndTime, false, reference);
                case "DateTimeModelComplexCalendar":
                    return DateTimeRecognizer.recognizeDateTime(currentCase.input, culture, DateTimeOptions.ComplexCalendar, false, reference);
                case "MergedExtractorSkipFromTo":
                    return DateTimeRecognizer.recognizeDateTime(currentCase.input, culture, DateTimeOptions.SkipFromToMerge, false, reference);
                default:
                    throw new NotSupportedException("Model Type/Name not supported: " + currentCase.modelName + " in " + culture);
            }
        } catch (IllegalArgumentException ex) {

            // Model not existing in a given culture can be considered a skip. Other illegal argument exceptions should fail tests.
            if (ex.getMessage().toLowerCase().contains(DependencyConstants.BASE_RECOGNIZERS_MODEL_UNAVAILABLE)) {
                throw new AssumptionViolatedException(ex.getMessage(), ex);
            } else throw new IllegalArgumentException(ex.getMessage(), ex);
        } catch (NotSupportedException nex) {
            throw new AssumptionViolatedException(nex.getMessage(), nex);
        }
    }
}

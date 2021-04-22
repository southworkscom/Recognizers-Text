package com.microsoft.recognizers.text.tests.choice;

import com.microsoft.recognizers.text.ModelResult;
import com.microsoft.recognizers.text.ResolutionKey;
import com.microsoft.recognizers.text.choice.ChoiceOptions;
import com.microsoft.recognizers.text.choice.ChoiceRecognizer;
import com.microsoft.recognizers.text.tests.AbstractTest;
import com.microsoft.recognizers.text.tests.DependencyConstants;
import com.microsoft.recognizers.text.tests.NotSupportedException;
import com.microsoft.recognizers.text.tests.TestCase;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

import org.javatuples.Pair;
import org.junit.Assert;
import org.junit.AssumptionViolatedException;
import org.junit.runners.Parameterized;

public class BooleanModelTest extends AbstractTest {

    private static final String recognizerType = "Choice";

    @Parameterized.Parameters(name = "{0}")
    public static Collection<TestCase> testCases() {
        return AbstractTest.enumerateTestCases(recognizerType, "Model");
    }

    public BooleanModelTest(TestCase currentCase) {
        super(currentCase);
    }

    @Override
    protected void recognizeAndAssert(TestCase currentCase) {
        // parse
        List<ModelResult> results = recognize(currentCase);
        // assert
        assertResults(currentCase, results, Arrays.asList(ResolutionKey.Value, ResolutionKey.Score));
    }

    @Override
    protected List<ModelResult> recognize(TestCase currentCase) {
        try {

            String culture = getCultureCode(currentCase.language);
            switch (currentCase.modelName) {

                case "BooleanModel": {
                    return ChoiceRecognizer.recognizeBoolean(currentCase.input, culture, ChoiceOptions.None, false);
                }

                default: {
                    throw new NotSupportedException("Model Type/Name not supported: " + currentCase.modelName + " in " + culture);
                }
            }

        } catch (IllegalArgumentException ex) {

            // Model not existing can be considered a skip. Other exceptions should fail tests.
            if (ex.getMessage().toLowerCase().contains(DependencyConstants.BASE_RECOGNIZERS_MODEL_UNAVAILABLE)) {
                throw new AssumptionViolatedException(ex.getMessage(), ex);
            } else throw new IllegalArgumentException(ex.getMessage(), ex);
        } catch (NotSupportedException nex) {
            throw new AssumptionViolatedException(nex.getMessage(), nex);
        }
    }

    @Override
    public void assertResults(TestCase currentCase, List<ModelResult> results, List<String> testResolutionKeys) {
        List<ModelResult> expectedResults = readExpectedResults(ModelResult.class, currentCase.results);
        Assert.assertEquals(getMessage(currentCase, "\"Result Count\""), expectedResults.size(), results.size());

        IntStream.range(0, expectedResults.size())
                .mapToObj(i -> Pair.with(expectedResults.get(i), results.get(i)))
                .forEach(t -> {
                    ModelResult expected = t.getValue0();
                    ModelResult actual = t.getValue1();

                    Assert.assertEquals(getMessage(currentCase, "typeName"), expected.typeName, actual.typeName);
                    Assert.assertEquals(getMessage(currentCase, "text"), expected.text, actual.text);

                    assertModel(expected, actual, currentCase, testResolutionKeys);
                });
    }
}
package com.microsoft.recognizers.text.tests.numberwithunit;

import com.microsoft.recognizers.text.ModelResult;
import com.microsoft.recognizers.text.ResolutionKey;
import com.microsoft.recognizers.text.numberwithunit.NumberWithUnitOptions;
import com.microsoft.recognizers.text.numberwithunit.NumberWithUnitRecognizer;
import com.microsoft.recognizers.text.numberwithunit.models.CurrencyModel;
import com.microsoft.recognizers.text.tests.DependencyConstants;
import com.microsoft.recognizers.text.tests.NotSupportedException;
import com.microsoft.recognizers.text.tests.TestCase;
import org.junit.AssumptionViolatedException;

import java.util.Arrays;
import java.util.List;

public class CurrencyModelTest extends NumberWithUnitTest {

    public CurrencyModelTest(TestCase currentCase) {super(currentCase);}

    @Override
    protected void recognizeAndAssert(TestCase currentCase) {
        // parse
        List<ModelResult> results = recognize(currentCase);

        // assert
        assertResults(currentCase, results, getKeysToTest(currentCase));
    }

    @Override
    protected List<String> getKeysToTest(TestCase currentCase) {
        return Arrays.asList(ResolutionKey.Value, ResolutionKey.Unit, ResolutionKey.IsoCurrency);
    }

    @Override
    protected List<ModelResult> recognize(TestCase currentCase) {
        try {
            String culture = getCultureCode(currentCase.language);

            return NumberWithUnitRecognizer.recognizeCurrency(currentCase.input, culture, NumberWithUnitOptions.None, false);

        } catch (IllegalArgumentException ex) {

            // Model not existing can be considered a skip. Other exceptions should fail tests.
            if (ex.getMessage().toLowerCase().contains(DependencyConstants.BASE_RECOGNIZERS_MODEL_UNAVAILABLE)) {
                throw new AssumptionViolatedException(ex.getMessage(), ex);
            } else throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

}

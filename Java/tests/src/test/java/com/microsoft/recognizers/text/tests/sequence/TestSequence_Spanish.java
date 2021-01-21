// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.recognizers.text.tests.sequence;

import java.util.Collection;
import java.util.List;

import com.microsoft.recognizers.text.ModelResult;
import com.microsoft.recognizers.text.sequence.SequenceRecognizer;
import com.microsoft.recognizers.text.tests.AbstractTest;
import com.microsoft.recognizers.text.tests.TestCase;

import org.junit.AssumptionViolatedException;
import org.junit.runners.Parameterized;

public class TestSequence_Spanish extends AbstractTest {
    private static final String recognizerType = "Sequence";

    @Parameterized.Parameters(name = "{0}")
    public static Collection<TestCase> testCases() {
        return AbstractTest.enumerateTestCases(recognizerType, "Model");
    }

    public TestSequence_Spanish(TestCase currentCase) {
        super(currentCase);
    }

    @Override
    protected List<ModelResult> recognize(TestCase currentCase) {
        try {

            String culture = getCultureCode(currentCase.language);
            switch (currentCase.modelName) {
                case "PhoneNumberModel": {
                    return SequenceRecognizer.recognizePhoneNumber(currentCase.input, culture, null, null);
                }                
                default: {
                    throw new AssumptionViolatedException("Model Type/Name not supported.");
                }
            }

        } catch (IllegalArgumentException ex) {
            throw new AssumptionViolatedException(ex.getMessage(), ex);
        }
    }
}

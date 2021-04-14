// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.recognizers.text.tests.sequence;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.microsoft.recognizers.text.ModelResult;
import com.microsoft.recognizers.text.ResolutionKey;
import com.microsoft.recognizers.text.sequence.SequenceRecognizer;
import com.microsoft.recognizers.text.tests.AbstractTest;
import com.microsoft.recognizers.text.tests.TestCase;

import org.junit.Assert;
import org.junit.AssumptionViolatedException;
import org.junit.runners.Parameterized;

public class SequenceTest extends AbstractTest {
    private static final String recognizerType = "Sequence";

    @Parameterized.Parameters(name = "{0}")
    public static Collection<TestCase> testCases() {
        return AbstractTest.enumerateTestCases(recognizerType, "Model");
    }

    public SequenceTest(TestCase currentCase) {
        super(currentCase);
    }

    @Override
    protected void recognizeAndAssert(TestCase currentCase) {

        // parse
        List<ModelResult> results = recognize(currentCase);

        // assert
        assertResults(currentCase, results, getKeysToTest());
    }

    @Override
    protected void assertModel(ModelResult expected, ModelResult actual, TestCase currentCase, List<String> testResolutionKeys) {
        super.assertModel(expected, actual, currentCase, testResolutionKeys);

        // Number and NumberWithUnit are supported currently.
        if (expected.start != -1) {
            Assert.assertEquals(getMessage(currentCase, "start"), expected.start, actual.start);
        }

        // Number and NumberWithUnit are supported currently.
        if (expected.end != -1) {
            Assert.assertEquals(getMessage(currentCase, "end"), expected.end, actual.end);
        }
    }
    private List<String> getKeysToTest() {
        return Arrays.asList(ResolutionKey.Value);
    }

    @Override
    protected List<ModelResult> recognize(TestCase currentCase) {
        try {
            String culture = getCultureCode(currentCase.language);
            switch (currentCase.modelName) {
                case "EmailModel": {
                    return SequenceRecognizer.recognizeEmail(currentCase.input, culture, null, null);
                }
                case "GUIDModel": {
                    return SequenceRecognizer.recognizeGUID(currentCase.input, culture, null, null);
                }
                case "HashtagModel": {
                    return SequenceRecognizer.recognizeHashtag(currentCase.input, culture, null, null);
                }
                case "IpAddressModel": {
                    return SequenceRecognizer.recognizeIpAddress(currentCase.input, culture, null, null);
                }
                case "MentionModel": {
                    return SequenceRecognizer.recognizeMention(currentCase.input, culture, null, null);
                }
                case "PhoneNumberModel": {
                    return SequenceRecognizer.recognizePhoneNumber(currentCase.input, culture, null, null);
                }
                case "URLModel": {
                    return SequenceRecognizer.recognizeURL(currentCase.input, culture, null, null);
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

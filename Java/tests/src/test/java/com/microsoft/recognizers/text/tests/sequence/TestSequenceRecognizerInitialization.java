// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.recognizers.text.tests.sequence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.microsoft.recognizers.text.Culture;
import com.microsoft.recognizers.text.IModel;
import com.microsoft.recognizers.text.ModelResult;
import com.microsoft.recognizers.text.ResolutionKey;
import com.microsoft.recognizers.text.sequence.SequenceOptions;
import com.microsoft.recognizers.text.sequence.SequenceRecognizer;
import com.microsoft.recognizers.text.sequence.english.extractors.EnglishPhoneNumberExtractorConfiguration;
import com.microsoft.recognizers.text.sequence.english.parsers.PhoneNumberParser;
import com.microsoft.recognizers.text.sequence.models.PhoneNumberModel;

import org.junit.Assert;
import org.junit.Test;

public class TestSequenceRecognizerInitialization {
    private final String testInput = "1 (877) 609-2233";

    private final String englishCulture = Culture.English;
    private final String invalidCulture = "vo-id";

    private IModel controlModel;

    public testSequenceRecognizerInitialization() {
        SequenceOptions config = SequenceOptions.None;

        controlModel = new PhoneNumberModel(new PhoneNumberParser(),
                new EnglishPhoneNumberExtractorConfiguration(config));
    }

    @Test
    public void withoutCultureUseTargetCulture() {
        SequenceRecognizer recognizer = new SequenceRecognizer(this.englishCulture, SequenceOptions.None, true);
        IModel testedModel = recognizer.getPhoneNumberModel(null, true);

        this.testPhoneNumber(testedModel, controlModel, testInput);
    }

    @Test
    public void withInvalidCultureUseTargetCulture() {
        SequenceRecognizer recognizer = new SequenceRecognizer(this.englishCulture, SequenceOptions.None, true);
        IModel testedModel = recognizer.getPhoneNumberModel(this.invalidCulture, true);

        this.testPhoneNumber(testedModel, controlModel, testInput);
    }

    @Test
    public void withInvalidCultureAlwaysUseEnglish() {
        SequenceRecognizer recognizer = new SequenceRecognizer(null, SequenceOptions.None, true);
        IModel testedModel = recognizer.getPhoneNumberModel(this.invalidCulture, true);

        this.testPhoneNumber(testedModel, controlModel, testInput);
    }

    @Test
    public void withoutTargetCultureAndWithoutCultureFallbackToEnglishCulture() {
        SequenceRecognizer recognizer = new SequenceRecognizer(null, SequenceOptions.None, true);
        IModel testedModel = recognizer.getPhoneNumberModel(null, true);

        this.testPhoneNumber(testedModel, controlModel, testInput);
    }

    @Test
    public void withInvalidCultureAsTargetAlwaysUseEnglish() {
        SequenceRecognizer recognizer = new SequenceRecognizer(this.invalidCulture, SequenceOptions.None, true);
        Assert.assertEquals(recognizer.getPhoneNumberModel(null, true),
                recognizer.getPhoneNumberModel(this.englishCulture, true));
    }

    @Test
    public void initializationWithIntOptionResolveOptionsEnum() {
        SequenceRecognizer recognizer = new SequenceRecognizer(this.englishCulture, 0, false);
        Assert.assertTrue(recognizer.options.equals(SequenceOptions.None));
    }

    @Test
    public void initializationWithInvalidOptionsThrowError() {
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            new SequenceRecognizer(this.englishCulture, -1, false);
        });
    }

    private void testPhoneNumber(IModel testedModel, IModel controlModel, String source) {
        List<ModelResult> expectedResults = controlModel.parse(source);
        List<ModelResult> actualResults = testedModel.parse(source);

        Assert.assertEquals(expectedResults.size(), actualResults.size());
        Assert.assertTrue(expectedResults.size() > 0);

        Map<ModelResult, ModelResult> enumerable = this.zipToMap(expectedResults, actualResults);

        for (Entry<ModelResult, ModelResult> tuple : enumerable.entrySet()) {
            ModelResult expected = tuple.getKey();
            ModelResult actual = tuple.getValue();

            Assert.assertEquals(expected.typeName, actual.typeName);
            Assert.assertEquals(expected.text, actual.text);

            Assert.assertEquals(expected.resolution.get(ResolutionKey.Value),
                    actual.resolution.get(ResolutionKey.Value));
        }

    }

    private <K, V> Map<K, V> zipToMap(List<K> keys, List<V> values) {
        return IntStream.range(0, keys.size()).boxed().collect(Collectors.toMap(keys::get, values::get));
    }
}

// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.recognizers.text.tests.sequence;

import com.microsoft.recognizers.text.Culture;
import com.microsoft.recognizers.text.sequence.SequenceRecognizer;
import com.microsoft.recognizers.text.sequence.english.extractors.EnglishPhoneNumberExtractorConfiguration;
import com.microsoft.recognizers.text.sequence.english.parsers.PhoneNumberParser;
import com.microsoft.recognizers.text.sequence.models.PhoneNumberModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestSequenceRecognizerCache {

    @Before
    public void initialization() {
        SequenceRecognizer recognizer = new SequenceRecognizer();
        recognizer.getInternalCache().clear();
    }

    @Test
    public void withLazyInitializationCacheEmpty() {
        SequenceRecognizer recognizer = new SequenceRecognizer(lazyInitialization: true);
        var internalCache = recognizer.getInternalCache();
        Assert.assertEquals(0, internalCache.Count);
    }

    @Test
    public void withoutLazyInitializationCacheFull() {
        SequenceRecognizer recognizer = new SequenceRecognizer(lazyInitialization: false);
        var internalCache = recognizer.getInternalCache();
        Assert.assertNotEquals(0, internalCache.size());
    }

    public void withoutLazyInitializationAndCultureCacheWithCulture() {
        SequenceRecognizer recognizer = new SequenceRecognizer(Culture.English, lazyInitialization: false);
        var internalCache = recognizer.getInternalCache();
        Assert.assertTrue(internalCache.All(kv => kv.Key.culture == Culture.English));
    }
}

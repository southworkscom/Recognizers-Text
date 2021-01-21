// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.recognizers.text.sequence;

import java.util.List;
import java.util.Locale;
import java.util.function.Function;

import com.microsoft.recognizers.text.Culture;
import com.microsoft.recognizers.text.IModel;
import com.microsoft.recognizers.text.ModelResult;
import com.microsoft.recognizers.text.Recognizer;
import com.microsoft.recognizers.text.sequence.config.BaseSequenceConfiguration;
import com.microsoft.recognizers.text.sequence.english.extractors.EmailExtractor;
import com.microsoft.recognizers.text.sequence.english.extractors.EnglishIpExtractorConfiguration;
import com.microsoft.recognizers.text.sequence.english.extractors.EnglishPhoneNumberExtractorConfiguration;
import com.microsoft.recognizers.text.sequence.english.extractors.EnglishURLExtractorConfiguration;
import com.microsoft.recognizers.text.sequence.english.extractors.GUIDExtractor;
import com.microsoft.recognizers.text.sequence.english.extractors.HashTagExtractor;
import com.microsoft.recognizers.text.sequence.english.extractors.MentionExtractor;
import com.microsoft.recognizers.text.sequence.english.parsers.EmailParser;
import com.microsoft.recognizers.text.sequence.english.parsers.GUIDParser;
import com.microsoft.recognizers.text.sequence.english.parsers.HashTagParser;
import com.microsoft.recognizers.text.sequence.english.parsers.MentionParser;
import com.microsoft.recognizers.text.sequence.english.parsers.PhoneNumberParser;
import com.microsoft.recognizers.text.sequence.english.parsers.URLParser;
import com.microsoft.recognizers.text.sequence.extractors.BasePhoneNumberExtractorConfiguration;
import com.microsoft.recognizers.text.sequence.models.EmailModel;
import com.microsoft.recognizers.text.sequence.models.GUIDModel;
import com.microsoft.recognizers.text.sequence.models.HashTagModel;
import com.microsoft.recognizers.text.sequence.models.IpAddressModel;
import com.microsoft.recognizers.text.sequence.models.MentionModel;
import com.microsoft.recognizers.text.sequence.models.PhoneNumberModel;
import com.microsoft.recognizers.text.sequence.models.URLModel;

public class SequenceRecognizer extends Recognizer<SequenceOptions> {
    public SequenceRecognizer(String targetCulture, SequenceOptions options, Boolean lazyInitialization) {
        super(targetCulture, options != null ? options : SequenceOptions.None,
                lazyInitialization != null ? lazyInitialization : false);
    }

    public SequenceRecognizer(String targetCulture, Integer options, Boolean lazyInitialization) {
        this(targetCulture, options, lazyInitialization != null ? lazyInitialization : false);
    }

    public SequenceRecognizer(SequenceOptions options, Boolean lazyInitialization) {
        super(null, options, lazyInitialization != null ? lazyInitialization : true);
    }

    public SequenceRecognizer(Integer options, Boolean lazyInitialization) {
        this(null, options, lazyInitialization != null ? lazyInitialization : true);
    }

    public static List<ModelResult> recognizePhoneNumber(String query, String culture, SequenceOptions options,
            Boolean fallbackToDefaultCulture) {
        options = options != null ? options : SequenceOptions.None;
        fallbackToDefaultCulture = fallbackToDefaultCulture != null ? fallbackToDefaultCulture : true;
        return SequenceRecognizer.recognizeByModel(
                recognizer -> ((SequenceRecognizer) recognizer).getPhoneNumberModel(culture, fallbackToDefaultCulture), query, options);
    }

    public static List<ModelResult> recognizeIpAddres(String query, String culture, SequenceOptions options,
            Boolean fallbackToDefaultCulture) {
        options = options != null ? options : SequenceOptions.None;
        fallbackToDefaultCulture = fallbackToDefaultCulture != null ? fallbackToDefaultCulture : true;
        return SequenceRecognizer.recognizeByModel(
                recognizer -> ((SequenceRecognizer) recognizer).getIpAddressModel(culture, fallbackToDefaultCulture), query, options);
    }

    public static List<ModelResult> recognizeMention(String query, String culture, SequenceOptions options,
            Boolean fallbackToDefaultCulture) {
        options = options != null ? options : SequenceOptions.None;
        fallbackToDefaultCulture = fallbackToDefaultCulture != null ? fallbackToDefaultCulture : true;
        return SequenceRecognizer.recognizeByModel(
                recognizer -> ((SequenceRecognizer) recognizer).getMentionModel(culture, fallbackToDefaultCulture), query, options);
    }

    public static List<ModelResult> recognizeHashtag(String query, String culture, SequenceOptions options,
            Boolean fallbackToDefaultCulture) {
        options = options != null ? options : SequenceOptions.None;
        fallbackToDefaultCulture = fallbackToDefaultCulture != null ? fallbackToDefaultCulture : true;
        return SequenceRecognizer.recognizeByModel(
                recognizer -> ((SequenceRecognizer) recognizer).getHashtagModel(culture, fallbackToDefaultCulture),
                query, options);
    }

    public static List<ModelResult> recognizeEmail(String query, String culture, SequenceOptions options,
            Boolean fallbackToDefaultCulture) {
        options = options != null ? options : SequenceOptions.None;
        fallbackToDefaultCulture = fallbackToDefaultCulture != null ? fallbackToDefaultCulture : true;
        return SequenceRecognizer.recognizeByModel(
                recognizer -> ((SequenceRecognizer) recognizer).getEmailModel(culture, fallbackToDefaultCulture), query, options);
    }

    public static List<ModelResult> recognizeURL(String query, String culture, SequenceOptions options,
            Boolean fallbackToDefaultCulture) {
        options = options != null ? options : SequenceOptions.None;
        fallbackToDefaultCulture = fallbackToDefaultCulture != null ? fallbackToDefaultCulture : true;
        return SequenceRecognizer.recognizeByModel(
                recognizer -> ((SequenceRecognizer) recognizer).getURLModel(culture, fallbackToDefaultCulture), query, options);
    }

    public static List<ModelResult> recognizeGUID(String query, String culture, SequenceOptions options,
            Boolean fallbackToDefaultCulture) {
        options = options != null ? options : SequenceOptions.None;
        fallbackToDefaultCulture = fallbackToDefaultCulture != null ? fallbackToDefaultCulture : true;
        return SequenceRecognizer.recognizeByModel(
                recognizer -> ((SequenceRecognizer) recognizer).getGUIDModel(culture, fallbackToDefaultCulture), query, options);
    }

    public IModel getPhoneNumberModel(String culture, Boolean fallbackToDefaultCulture) {
        fallbackToDefaultCulture = fallbackToDefaultCulture != null ? fallbackToDefaultCulture : true;
        if (culture != null && (culture.toLowerCase(Locale.ROOT).startsWith("zh-")
                || culture.toLowerCase(Locale.ROOT).startsWith("ja-"))) {
            return this.getModel(PhoneNumberModel.class, Culture.Chinese, fallbackToDefaultCulture);
        }

        return this.getModel(PhoneNumberModel.class, culture, fallbackToDefaultCulture);
    }

    public IModel getIpAddressModel(String culture, Boolean fallbackToDefaultCulture) {
        fallbackToDefaultCulture = fallbackToDefaultCulture != null ? fallbackToDefaultCulture : true;
        if (culture != null && (culture.toLowerCase(Locale.ROOT).startsWith("zh-")
                || culture.toLowerCase(Locale.ROOT).startsWith("ja-"))) {
            return this.getModel(IpAddressModel.class, Culture.Chinese, fallbackToDefaultCulture);
        }

        return this.getModel(IpAddressModel.class, Culture.English, fallbackToDefaultCulture);
    }

    public IModel getMentionModel(String culture, Boolean fallbackToDefaultCulture) {
        fallbackToDefaultCulture = fallbackToDefaultCulture != null ? fallbackToDefaultCulture : true;
        return this.getModel(MentionModel.class, Culture.English, fallbackToDefaultCulture);
    }

    public IModel getHashtagModel(String culture, Boolean fallbackToDefaultCulture) {
        fallbackToDefaultCulture = fallbackToDefaultCulture != null ? fallbackToDefaultCulture : true;
        return this.getModel(HashTagModel.class, Culture.English, fallbackToDefaultCulture);
    }

    public IModel getEmailModel(String culture, Boolean fallbackToDefaultCulture) {
        fallbackToDefaultCulture = fallbackToDefaultCulture != null ? fallbackToDefaultCulture : true;
        return this.getModel(EmailModel.class, Culture.English, fallbackToDefaultCulture);
    }

    public IModel getURLModel(String culture, Boolean fallbackToDefaultCulture) {
        fallbackToDefaultCulture = fallbackToDefaultCulture != null ? fallbackToDefaultCulture : true;
        if (culture != null && (culture.toLowerCase(Locale.ROOT).startsWith("zh-")
                || culture.toLowerCase(Locale.ROOT).startsWith("ja-"))) {
            return this.getModel(URLModel.class, Culture.Chinese, fallbackToDefaultCulture);
        }

        return this.getModel(URLModel.class, Culture.English, fallbackToDefaultCulture);
    }

    public IModel getGUIDModel(String culture, Boolean fallbackToDefaultCulture) {
        fallbackToDefaultCulture = fallbackToDefaultCulture != null ? fallbackToDefaultCulture : true;
        return this.getModel(GUIDModel.class, Culture.English, fallbackToDefaultCulture);
    }

    @Override
    protected void initializeConfiguration() {
        this.registerModel(PhoneNumberModel.class, Culture.English, (options) -> new PhoneNumberModel(
                new PhoneNumberParser(),
                new BasePhoneNumberExtractorConfiguration(new EnglishPhoneNumberExtractorConfiguration(options))));

        this.registerModel(PhoneNumberModel.class, Culture.Chinese,
                (options) -> new PhoneNumberModel(new PhoneNumberParser(),
                        new BasePhoneNumberExtractor(new ChinesePhoneNumberExtractorConfiguration(options))));

        this.registerModel(PhoneNumberModel.class, Culture.Portuguese,
                (options) -> new PhoneNumberModel(new PhoneNumberParser(),
                        new BasePhoneNumberExtractor(new PortuguesePhoneNumberExtractorConfiguration(options))));

        this.registerModel(PhoneNumberModel.class, Culture.Spanish,
                (options) -> new PhoneNumberModel(new PhoneNumberParser(),
                        new BasePhoneNumberExtractor(new SpanishPhoneNumberExtractorConfiguration(options))));

        this.registerModel(PhoneNumberModel.class, Culture.Dutch,
                (options) -> new PhoneNumberModel(new PhoneNumberParser(),
                        new BasePhoneNumberExtractor(new DutchPhoneNumberExtractorConfiguration(options))));

        this.registerModel(PhoneNumberModel.class, Culture.French,
                (options) -> new PhoneNumberModel(new PhoneNumberParser(),
                        new BasePhoneNumberExtractor(new FrenchPhoneNumberExtractorConfiguration(options))));

        this.registerModel(PhoneNumberModel.class, Culture.German,
                (options) -> new PhoneNumberModel(new PhoneNumberParser(),
                        new BasePhoneNumberExtractor(new GermanPhoneNumberExtractorConfiguration(options))));

        this.registerModel(PhoneNumberModel.class, Culture.Hindi,
                (options) -> new PhoneNumberModel(new PhoneNumberParser(),
                        new BasePhoneNumberExtractor(new HindiPhoneNumberExtractorConfiguration(options))));

        this.registerModel(PhoneNumberModel.class, Culture.Italian,
                (options) -> new PhoneNumberModel(new PhoneNumberParser(),
                        new BasePhoneNumberExtractor(new ItalianPhoneNumberExtractorConfiguration(options))));

        this.registerModel(PhoneNumberModel.class, Culture.Korean,
                (options) -> new PhoneNumberModel(new PhoneNumberParser(),
                        new BasePhoneNumberExtractor(new KoreanPhoneNumberExtractorConfiguration(options))));

        this.registerModel(PhoneNumberModel.class, Culture.Turkish,
                (options) -> new PhoneNumberModel(new PhoneNumberParser(),
                        new BasePhoneNumberExtractor(new TurkishPhoneNumberExtractorConfiguration(options))));

        this.registerModel(IpAddressModel.class, Culture.English, (options) -> new IpAddressModel(new IpParser(),
                new BaseIpExtractor(new EnglishIpExtractorConfiguration(options))));

        this.registerModel(IpAddressModel.class, Culture.Chinese, (options) -> new IpAddressModel(new IpParser(),
                new BaseIpExtractor(new ChineseIpExtractorConfiguration(options))));

        this.registerModel(MentionModel.class, Culture.English,
                (options) -> new MentionModel(new MentionParser(), new MentionExtractor()));

        this.registerModel(HashTagModel.class, Culture.English,
                (options) -> new HashTagModel(new HashTagParser(), new HashTagExtractor()));

        this.registerModel(EmailModel.class, Culture.English,
                (options) -> new EmailModel(new EmailParser(new BaseSequenceConfiguration(options)),
                        new EmailExtractor(new BaseSequenceConfiguration(options))));

        this.registerModel(URLModel.class, Culture.English, (options) -> new URLModel(new URLParser(),
                new BaseURLExtractor(new EnglishURLExtractorConfiguration(options))));

        this.registerModel(URLModel.class, Culture.Chinese, options -> new URLModel(new URLParser(),
                new BaseURLExtractor(new ChineseURLExtractorConfiguration(options))));

        this.registerModel(GUIDModel.class, Culture.English,
                (options) -> new GUIDModel(new GUIDParser(), new GUIDExtractor()));
    }

    private static List<ModelResult> recognizeByModel(Function getModelFunc, String query, SequenceOptions options) {
        SequenceRecognizer recognizer = new SequenceRecognizer(options, false);
        IModel model = (IModel) getModelFunc.apply(recognizer);
        return model.parse(query);
    }
}

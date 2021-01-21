// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.recognizers.text.sequence.models;

import com.microsoft.recognizers.text.IExtractor;
import com.microsoft.recognizers.text.IParser;
import com.microsoft.recognizers.text.sequence.Constants;

public class EmailModel extends AbstractSequenceModel {
    private String modelTypeName = Constants.MODEL_EMAIL;

    public EmailModel(IParser parser, IExtractor extractor) {
        super(parser, extractor);
    }

    public String getModelTypeName() {
        return modelTypeName;
    }

    public void setModelTypeName(String withModelTypeName) {
        this.modelTypeName = withModelTypeName;
    }
}

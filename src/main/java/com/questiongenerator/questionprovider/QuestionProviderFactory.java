package com.questiongenerator.questionprovider;

public interface QuestionProviderFactory {

    QuestionProvider getQuestionProvider(String selector);
}

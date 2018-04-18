package com.questiongenerator.vo;


import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "questions"
})
public class QuestionsCollection {

    @JsonProperty("questions")
    private List<Question> questions = null;

    @JsonProperty("questions")
    public List<Question> getQuestions() {
        return questions;
    }
    @JsonProperty("questions")
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    @JsonIgnore
    private Map<String,Object> additionalProperties = new HashMap<>();


    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }
    @JsonAnySetter
    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

}

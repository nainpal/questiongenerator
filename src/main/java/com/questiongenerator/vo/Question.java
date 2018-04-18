package com.questiongenerator.vo;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "question","options","correct"
})
public class Question {

    @JsonProperty("question")
    private String question;
    @JsonProperty("options")
    private List<Option> options = null;
    @JsonProperty("question")
    public String getQuestion() {
        return question;
    }
    @JsonProperty("question")
    public void setQuestion(String question) {
        this.question = question;
    }
    @JsonProperty("options")
    public List<Option> getOptions() {
        return options;
    }
    @JsonProperty("options")
    public void setOptions(List<Option> options) {
        this.options = options;
    }
    @JsonProperty("correct")
    public String getCorrect() {
        return correct;
    }
    @JsonProperty("correct")
    public void setCorrect(String correct) {
        this.correct = correct;
    }

    @JsonProperty("correct")

    private String correct;

    @JsonIgnore
    private Map<String,Object> additionalProperties = new HashMap<>();

    @Override
    public String toString() {
        return "{" + question  + '}';
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }
    @JsonAnySetter
    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

}

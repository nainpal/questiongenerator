package com.questiongenerator.vo;

import com.fasterxml.jackson.annotation.*;
import java.util.HashMap;
import java.util.Map;

@JsonInclude (JsonInclude.Include.NON_NULL)

@JsonPropertyOrder({
        "option"
})
public class Option {
    @JsonProperty("option")
    private String option;
    @JsonIgnore
    private Map<String,Object> additionalProperties = new HashMap<>();
    @JsonProperty("option")
    public String getOption() {
        return option;
    }

    @Override
    public String toString() {
        return "{" + option + '}';
    }

    @JsonProperty("option")
    public void setOption(String option) {
        this.option = option;
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

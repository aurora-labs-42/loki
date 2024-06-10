package org.abstractj.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubSearchResponse {

    @JsonProperty("total_count")
    public Integer total_count;

    public String message;
}

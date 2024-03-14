package com.example.demo.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ApiResponse {
    private Results results;
}

@Data
class Results {
    @JsonProperty("api_version")
    private String apiVersion;
    @JsonProperty("results_available")
    private int resultsAvailable;
    @JsonProperty("results_returned")
    private String resultsReturned;
    @JsonProperty("results_start")
    private int resultsStart;
    @JsonProperty("large_area")
    private List<LargeArea> largeArea;
}

@Data
class LargeArea {
    private String code;
    private String name;
    @JsonProperty("service_area")
    private ServiceArea serviceArea;
    @JsonProperty("large_service_area")
    private LargeServiceArea largeServiceArea;
}

@Data
class ServiceArea {
    private String code;
    private String name;
}

@Data
class LargeServiceArea {
    private String code;
    private String name;
}
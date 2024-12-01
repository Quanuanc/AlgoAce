package dev.cheng.algoace.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CheckResult(
        String state,
        Integer status_code,
        Boolean run_success,
        String status_msg,
        String status_runtime,
        String status_memory,
        Double runtime_percentile,
        Double memory_percentile
) {
}

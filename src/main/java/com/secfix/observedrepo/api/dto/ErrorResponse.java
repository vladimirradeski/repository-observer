package com.secfix.observedrepo.api.dto;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record ErrorResponse(
        int status,
        List<String> errors
) {
}

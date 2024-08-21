package com.secfix.observedrepo.api.dto;

import com.secfix.observedrepo.domain.entity.ObservedRepoStatus;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.hibernate.validator.constraints.URL;

@Builder
public record UpdateObservedRepoInput(
        @Size(max = 1000, message = "The owner must have max 1000 characters.")
        String owner,
        @Size(max = 1000, message = "The name must have max 1000 characters.")
        String name,
        @URL
        String url,
        Integer stars,
        Integer openIssues,
        @Size(max = 100, message = "The name must have max 100 characters.")
        String license,
        ObservedRepoStatus status
) {
}

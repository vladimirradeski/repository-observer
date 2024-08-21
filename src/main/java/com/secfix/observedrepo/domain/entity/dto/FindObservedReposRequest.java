package com.secfix.observedrepo.domain.entity.dto;

import com.secfix.observedrepo.domain.entity.ObservedRepoStatus;
import lombok.Builder;

@Builder
public record FindObservedReposRequest(
        String owner,
        String name,
        ObservedRepoStatus status,
        String license,
        String cursor,
        Integer pageSize,
        boolean next
) {

    public FindObservedReposRequest withCursor(String newCursor) {
        return new FindObservedReposRequest(owner, name, status, license, newCursor, pageSize, next);
    }
}

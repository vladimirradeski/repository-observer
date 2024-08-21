package com.secfix.observedrepo.domain.event.model;

import com.secfix.observedrepo.domain.entity.ObservedRepo;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record ObservedRepoSyncedEvent(
        ObservedRepo observedRepo
) implements Serializable {
}

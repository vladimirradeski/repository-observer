package com.secfix.observedrepo.domain.repository;

import com.secfix.observedrepo.domain.entity.ObservedRepo;
import com.secfix.observedrepo.domain.entity.ObservedRepoStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ObservedRepoRepository {

    ObservedRepo save(ObservedRepo observedRepo);

    ObservedRepo findById(UUID id);

    ObservedRepo findByOwnerAndName(String owner, String name);

    List<ObservedRepo> findNextByCursor(
            String owner,
            String name,
            ObservedRepoStatus status,
            String license,
            Instant cursor,
            Integer pageSize);

    List<ObservedRepo> findPreviousByCursor(
            String owner,
            String name,
            ObservedRepoStatus status,
            String license,
            Instant cursor,
            Integer pageSize);
}

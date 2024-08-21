package com.secfix.observedrepo.domain.usecase;

import com.secfix.observedrepo.domain.entity.ObservedRepo;
import com.secfix.observedrepo.domain.entity.exception.ObservedRepoNotFoundException;
import com.secfix.observedrepo.domain.repository.ObservedRepoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetObservedRepoById {

    private static final Logger log = LoggerFactory.getLogger(GetObservedRepoById.class);
    private final ObservedRepoRepository observedRepoRepository;

    public GetObservedRepoById(ObservedRepoRepository observedRepoRepository) {
        this.observedRepoRepository = observedRepoRepository;
    }

    public ObservedRepo execute(UUID id) {
        ObservedRepo observedRepo = observedRepoRepository.findById(id);

        if (observedRepo == null) {
            log.warn("Observed repo by id not found | id: {}", id);
            throw new ObservedRepoNotFoundException("Observed repo not found");
        }

        return observedRepo;
    }
}

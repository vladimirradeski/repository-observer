package com.secfix.observedrepo.domain.usecase;

import com.secfix.observedrepo.domain.entity.ObservedRepo;
import com.secfix.observedrepo.domain.repository.ObservedRepoRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteObservedRepo {
    private final ObservedRepoRepository observedRepoRepository;
    private final GetObservedRepoById getObservedRepoById;

    public DeleteObservedRepo(ObservedRepoRepository observedRepoRepository, GetObservedRepoById getObservedRepoById) {
        this.observedRepoRepository = observedRepoRepository;
        this.getObservedRepoById = getObservedRepoById;
    }

    public ObservedRepo execute(UUID id) {
        ObservedRepo observedRepo = getObservedRepoById.execute(id);

        ObservedRepo deletedObservedRepo = observedRepo.delete();

        return observedRepoRepository.save(deletedObservedRepo);
    }
}

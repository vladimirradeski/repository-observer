package com.secfix.observedrepo.domain.usecase;

import com.secfix.observedrepo.domain.entity.ObservedRepo;
import com.secfix.observedrepo.domain.entity.ObservedRepoStatus;
import com.secfix.observedrepo.domain.entity.dto.CreateObservedRepoRequest;
import com.secfix.observedrepo.domain.entity.exception.DuplicateObservedRepoException;
import com.secfix.observedrepo.domain.repository.ObservedRepoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CreateObservedRepo {

    private static final Logger log = LoggerFactory.getLogger(CreateObservedRepo.class);
    private final ObservedRepoRepository observedRepoRepository;

    public CreateObservedRepo(ObservedRepoRepository observedRepoRepository) {
        this.observedRepoRepository = observedRepoRepository;
    }

    public ObservedRepo execute(CreateObservedRepoRequest request) {
        checkDuplicate(request);

        ObservedRepo observedRepo = ObservedRepo.builder()
                .name(request.name())
                .owner(request.owner())
                .status(ObservedRepoStatus.ACTIVE)
                .build();

        return observedRepoRepository.save(observedRepo);
    }

    private void checkDuplicate(CreateObservedRepoRequest request) {
        ObservedRepo foundObservedRepo = observedRepoRepository.findByOwnerAndName(request.owner(), request.name());

        if (foundObservedRepo != null) {
            log.warn("Can't create repo, duplicate observed repo by owner and name found | owner: {}, name: {}", request.owner(), request.name());
            throw new DuplicateObservedRepoException("Can't create repo, duplicate observed repo by owner and name found");
        }

    }
}

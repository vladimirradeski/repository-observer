package com.secfix.observedrepo.provider.data;

import com.secfix.observedrepo.domain.entity.ObservedRepo;
import com.secfix.observedrepo.domain.entity.ObservedRepoStatus;
import com.secfix.observedrepo.domain.repository.ObservedRepoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ObservedRepoRepositoryAdapter implements ObservedRepoRepository {

    private static final Logger log = LoggerFactory.getLogger(ObservedRepoRepositoryAdapter.class);

    private final ObservedRepoJpaRepository observedRepoJpaRepository;

    public ObservedRepoRepositoryAdapter(ObservedRepoJpaRepository observedRepoJpaRepository) {
        this.observedRepoJpaRepository = observedRepoJpaRepository;
    }

    @Override
    public ObservedRepo save(ObservedRepo observedRepo) {
        log.info("SAVING_OBSERVED_REPO | data: {}", observedRepo);
        ObservedRepo saved = observedRepoJpaRepository.save(ObservedRepoModel.fromDomain(observedRepo)).toDomain();
        log.info("SAVED_OBSERVED_REPO | data: {}", saved);
        return saved;
    }

    @Override
    public ObservedRepo findById(UUID id) {
        log.info("RETRIEVE_OBSERVED_REPO_BY_ID | id: {}", id);
        ObservedRepo observedRepo = observedRepoJpaRepository.findById(id)
                .map(ObservedRepoModel::toDomain)
                .orElse(null);
        log.info("RETRIEVED_OBSERVED_REPO_BY_ID | data: {}", observedRepo);
        return observedRepo;
    }

    @Override
    public ObservedRepo findByOwnerAndName(String owner, String name) {
        log.info("RETRIEVE_OBSERVED_REPO_BY_OWNER_AND_NAME | owner: {}, name: {}", owner, name);
        ObservedRepo observedRepo = observedRepoJpaRepository.findFirstByOwnerAndName(owner, name)
                .map(ObservedRepoModel::toDomain)
                .orElse(null);
        log.info("RETRIEVED_OBSERVED_REPO_BY_OWNER_AND_NAME | data: {}", observedRepo);
        return observedRepo;
    }

    @Override
    public List<ObservedRepo> findNextByCursor(String owner, String name, ObservedRepoStatus status, String license, Instant cursor, Integer pageSize) {
        log.info("FIND_NEXT_OBSERVED_REPOS | owner: {}, name: {}, status: {}, license: {}, cursor: {}, pageSize: {}", owner, name, status, license, cursor, pageSize);
        Pageable pageable = PageRequest.of(0, pageSize);
        List<ObservedRepoModel> result = observedRepoJpaRepository.findNextByCursor(owner, name, ObservedRepoStatusModel.fromDomain(status), license, cursor, pageable);


        log.info("FOUND_NEXT_OBSERVED_REPOS | count: {}, cursor: {}, pageSize: {}", result.stream().toList().size(), cursor, pageSize);

        return result.stream().map(ObservedRepoModel::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ObservedRepo> findPreviousByCursor(String owner, String name, ObservedRepoStatus status, String license, Instant cursor, Integer pageSize) {
        log.info("FIND_PREVIOUS_OBSERVED_REPOS | owner: {}, name: {}, status: {}, license: {}, cursor: {}, pageSize: {}", owner, name, status, license, cursor, pageSize);
        Pageable pageable = PageRequest.of(0, pageSize);
        List<ObservedRepoModel> result = observedRepoJpaRepository.findPrevByCursor(owner, name, ObservedRepoStatusModel.fromDomain(status), license, cursor, pageable);


        log.info("FOUND_PREVIOUS_OBSERVED_REPOS | count: {}, cursor: {}, pageSize: {}", result.stream().toList().size(), cursor, pageSize);

        return result.stream().map(ObservedRepoModel::toDomain)
                .collect(Collectors.toList());
    }
}

package com.secfix.observedrepo.domain.usecase;

import com.secfix.observedrepo.domain.entity.ObservedRepo;
import com.secfix.observedrepo.domain.entity.dto.UpdateObservedRepoRequest;
import com.secfix.observedrepo.domain.repository.ObservedRepoRepository;
import org.springframework.stereotype.Service;

@Service
public class UpdateObservedRepo {

    private final ObservedRepoRepository observedRepoRepository;
    private final GetObservedRepoById getObservedRepoById;

    public UpdateObservedRepo(ObservedRepoRepository observedRepoRepository, GetObservedRepoById getObservedRepoById) {
        this.observedRepoRepository = observedRepoRepository;
        this.getObservedRepoById = getObservedRepoById;
    }

    public ObservedRepo execute(UpdateObservedRepoRequest request) {
        ObservedRepo existingObservedRepo = getObservedRepoById.execute(request.id());

        ObservedRepo updatedObservedRepo = ObservedRepo.builder()
                .id(existingObservedRepo.id())
                .createdAt(existingObservedRepo.createdAt())
                .owner(updateIfNotNull(existingObservedRepo.owner(), request.owner()))
                .name(updateIfNotNull(existingObservedRepo.name(), request.name()))
                .stars(updateIfNotNull(existingObservedRepo.stars(), request.stars()))
                .openIssues(updateIfNotNull(existingObservedRepo.openIssues(), request.openIssues()))
                .license(updateIfNotNull(existingObservedRepo.license(), request.license()))
                .url(updateIfNotNull(existingObservedRepo.url(), request.url()))
                .status(updateIfNotNull(existingObservedRepo.status(), request.status()))
                .version(existingObservedRepo.version())
                .build();

        return observedRepoRepository.save(updatedObservedRepo);
    }

    private <T> T updateIfNotNull(T currentValue, T newValue) {
        return newValue != null ? newValue : currentValue;
    }
}

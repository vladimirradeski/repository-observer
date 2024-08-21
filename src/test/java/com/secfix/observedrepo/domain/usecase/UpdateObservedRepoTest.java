package com.secfix.observedrepo.domain.usecase;

import com.secfix.observedrepo.domain.entity.ObservedRepo;
import com.secfix.observedrepo.domain.entity.ObservedRepoStatus;
import com.secfix.observedrepo.domain.entity.dto.UpdateObservedRepoRequest;
import com.secfix.observedrepo.domain.repository.ObservedRepoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UpdateObservedRepoTest {

    @Mock
    private ObservedRepoRepository observedRepoRepository;

    @Mock
    private GetObservedRepoById getObservedRepoById;

    @InjectMocks
    private UpdateObservedRepo updateObservedRepo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldUpdateRepo() {
        UUID id = UUID.randomUUID();
        ObservedRepo existingObservedRepo = ObservedRepo.builder()
                .id(id)
                .status(ObservedRepoStatus.ACTIVE)
                .owner("Owner")
                .name("Repo")
                .build();
        UpdateObservedRepoRequest request = UpdateObservedRepoRequest.builder()
                .id(id)
                .owner("Updated Owner")
                .name("Updated Repo")
                .stars(100)
                .openIssues(5)
                .license("Apache License 2.0")
                .url("https://github.com/updated")
                .status(ObservedRepoStatus.ACTIVE)
                .build();

        when(getObservedRepoById.execute(id)).thenReturn(existingObservedRepo);

        when(observedRepoRepository.save(any(ObservedRepo.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ObservedRepo updatedObservedRepo = updateObservedRepo.execute(request);

        assertEquals(id, updatedObservedRepo.id());
        assertEquals(request.owner(), updatedObservedRepo.owner());
        assertEquals(request.name(), updatedObservedRepo.name());
        assertEquals(request.stars(), updatedObservedRepo.stars());
        assertEquals(request.openIssues(), updatedObservedRepo.openIssues());
        assertEquals(request.license(), updatedObservedRepo.license());
        assertEquals(request.url(), updatedObservedRepo.url());
        assertEquals(request.status(), updatedObservedRepo.status());

        verify(getObservedRepoById, times(1)).execute(id);
        verify(observedRepoRepository, times(1)).save(any(ObservedRepo.class));
    }
}
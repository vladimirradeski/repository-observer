package com.secfix.observedrepo.domain.usecase;

import com.secfix.observedrepo.domain.entity.ObservedRepo;
import com.secfix.observedrepo.domain.entity.ObservedRepoStatus;
import com.secfix.observedrepo.domain.entity.dto.CreateObservedRepoRequest;
import com.secfix.observedrepo.domain.entity.exception.DuplicateObservedRepoException;
import com.secfix.observedrepo.domain.repository.ObservedRepoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CreateObservedRepoTest {

    @Mock
    private ObservedRepoRepository observedRepoRepository;

    @InjectMocks
    private CreateObservedRepo createObservedRepo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldCreateNewRepo() {
        CreateObservedRepoRequest request = CreateObservedRepoRequest.builder()
                .name("New Repo")
                .owner("New Owner")
                .build();

        ObservedRepo observedRepoToSave = ObservedRepo.builder()
                .id(UUID.randomUUID())
                .status(ObservedRepoStatus.ACTIVE)
                .owner("New Owner")
                .name("New Repo")
                .build();

        when(observedRepoRepository.findByOwnerAndName(request.owner(), request.name()))
                .thenReturn(null);
        when(observedRepoRepository.save(any(ObservedRepo.class))).thenReturn(observedRepoToSave);

        ObservedRepo observedRepo = createObservedRepo.execute(request);

        assertNotNull(observedRepo);
        assertEquals(request.name(), observedRepo.name());
        assertEquals(request.owner(), observedRepo.owner());
        assertEquals(ObservedRepoStatus.ACTIVE, observedRepo.status());

        verify(observedRepoRepository, times(1)).findByOwnerAndName(request.owner(), request.name());
        verify(observedRepoRepository, times(1)).save(any(ObservedRepo.class));
    }

    @Test
    public void shouldThrowDuplicateException() {
        CreateObservedRepoRequest request = CreateObservedRepoRequest.builder()
                .name("Duplicate Repo")
                .owner("Duplicate Owner")
                .build();

        ObservedRepo foundObservedRepo = ObservedRepo.builder()
                .id(UUID.randomUUID())
                .status(ObservedRepoStatus.ACTIVE)
                .owner("New Owner")
                .name("New Repo")
                .build();

        when(observedRepoRepository.findByOwnerAndName(request.owner(), request.name())).thenReturn(foundObservedRepo);

        assertThrows(DuplicateObservedRepoException.class, () -> {
            createObservedRepo.execute(request);
        });

        verify(observedRepoRepository, times(1)).findByOwnerAndName(request.owner(), request.name());
        verify(observedRepoRepository, never()).save(any(ObservedRepo.class));
    }
}
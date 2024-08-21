package com.secfix.observedrepo.domain.usecase;

import com.secfix.observedrepo.domain.entity.ObservedRepo;
import com.secfix.observedrepo.domain.entity.ObservedRepoStatus;
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

public class DeleteObservedRepoTest {

    @Mock
    private ObservedRepoRepository observedRepoRepository;

    @Mock
    private GetObservedRepoById getObservedRepoById;

    @InjectMocks
    private DeleteObservedRepo deleteObservedRepo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldDeleteRepo() {
        UUID id = UUID.randomUUID();
        ObservedRepo observedRepo = ObservedRepo.builder()
                .id(id)
                .status(ObservedRepoStatus.ACTIVE)
                .owner("Owner")
                .name("Repo")
                .build();

        when(getObservedRepoById.execute(id)).thenReturn(observedRepo);

        when(observedRepoRepository.save(any(ObservedRepo.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ObservedRepo deletedObservedRepo = deleteObservedRepo.execute(id);

        assertEquals(id, deletedObservedRepo.id());
        assertEquals(ObservedRepoStatus.DELETED, deletedObservedRepo.status());

        verify(getObservedRepoById, times(1)).execute(id);
        verify(observedRepoRepository, times(1)).save(deletedObservedRepo);
    }
}
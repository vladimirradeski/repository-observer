package com.secfix.observedrepo.domain.usecase;

import com.secfix.observedrepo.domain.entity.ObservedRepo;
import com.secfix.observedrepo.domain.entity.ObservedRepoStatus;
import com.secfix.observedrepo.domain.entity.exception.ObservedRepoNotFoundException;
import com.secfix.observedrepo.domain.repository.ObservedRepoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class GetObservedRepoByIdTest {

    @Mock
    private ObservedRepoRepository observedRepoRepository;

    @InjectMocks
    private GetObservedRepoById getObservedRepoById;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldReturnFoundRepo() {
        UUID id = UUID.randomUUID();
        ObservedRepo observedRepo = ObservedRepo.builder()
                .id(id)
                .status(ObservedRepoStatus.ACTIVE)
                .owner("Owner")
                .name("Repo")
                .build();

        when(observedRepoRepository.findById(id)).thenReturn(observedRepo);

        ObservedRepo result = getObservedRepoById.execute(id);

        assertEquals(id, result.id());
        assertEquals(observedRepo.name(), result.name());
        assertEquals(observedRepo.owner(), result.owner());

        verify(observedRepoRepository, times(1)).findById(id);
    }

    @Test
    public void shouldThrowObservedRepoNotFoundException() {
        UUID id = UUID.randomUUID();

        when(observedRepoRepository.findById(id)).thenReturn(null);

        assertThrows(ObservedRepoNotFoundException.class, () -> {
            getObservedRepoById.execute(id);
        });

        verify(observedRepoRepository, times(1)).findById(id);
    }
}
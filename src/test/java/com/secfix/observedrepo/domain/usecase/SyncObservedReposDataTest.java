package com.secfix.observedrepo.domain.usecase;

import com.secfix.observedrepo.domain.entity.ObservedRepo;
import com.secfix.observedrepo.domain.entity.ObservedRepoStatus;
import com.secfix.observedrepo.domain.entity.dto.FindObservedReposRequest;
import com.secfix.observedrepo.domain.entity.dto.FindObservedReposResponse;
import com.secfix.observedrepo.domain.entity.exception.ObservedRepoExternalDataNotFoundException;
import com.secfix.observedrepo.domain.event.model.ObservedRepoSyncedEvent;
import com.secfix.observedrepo.domain.event.publisher.ObservedRepoSyncedEventPublisher;
import com.secfix.observedrepo.domain.gateway.GetObservedRepoLatestDataProvider;
import com.secfix.observedrepo.domain.repository.ObservedRepoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.OptimisticLockingFailureException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SyncObservedReposDataTest {

    @Mock
    private ObservedRepoRepository observedRepoRepository;

    @Mock
    private GetObservedRepoLatestDataProvider getObservedRepoLatestDataProvider;

    @Mock
    private FindObservedRepos findObservedRepos;

    @Mock
    private ObservedRepoSyncedEventPublisher observedRepoSyncedEventPublisher;

    @InjectMocks
    private SyncObservedReposData syncObservedReposData;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessObservedRepoEntry_DataChanged() {
        UUID repoId = UUID.randomUUID();
        ObservedRepo observedRepo = ObservedRepo.builder()
                .id(repoId)
                .name("test-repo")
                .owner("test-owner")
                .status(ObservedRepoStatus.ACTIVE)
                .build();

        ObservedRepo latestData = ObservedRepo.builder()
                .id(repoId)
                .name("test-repo")
                .owner("test-owner")
                .url("https://github.com/test/test-repo")
                .status(ObservedRepoStatus.ACTIVE)
                .build();

        FindObservedReposRequest expectedRequest = FindObservedReposRequest.builder()
                .next(true)
                .status(ObservedRepoStatus.ACTIVE)
                .pageSize(20)
                .build();

        ObservedRepoSyncedEvent expectedEvent = ObservedRepoSyncedEvent.builder()
                .observedRepo(latestData)
                .build();

        FindObservedReposResponse response = FindObservedReposResponse.builder()
                .observedRepos(Collections.singletonList(observedRepo))
                .nextCursor(null)
                .build();

        when(findObservedRepos.execute(any())).thenReturn(response);
        when(getObservedRepoLatestDataProvider.get(observedRepo)).thenReturn(latestData);

        syncObservedReposData.execute();

        verify(findObservedRepos, times(1)).execute(expectedRequest);
        verify(getObservedRepoLatestDataProvider, times(1)).get(observedRepo);
        verify(observedRepoRepository, times(1)).save(latestData);
        verify(observedRepoSyncedEventPublisher, times(1)).publish(expectedEvent);
    }

    @Test
    void testProcessObservedRepoEntry_DataUnchanged() {
        UUID repoId = UUID.randomUUID();
        ObservedRepo observedRepo = ObservedRepo.builder()
                .id(repoId)
                .name("test-repo")
                .owner("test-owner")
                .url("https://github.com/test/test-repo")
                .build();

        FindObservedReposRequest expectedRequest = FindObservedReposRequest.builder()
                .next(true)
                .status(ObservedRepoStatus.ACTIVE)
                .pageSize(20)
                .build();

        FindObservedReposResponse response = FindObservedReposResponse.builder()
                .observedRepos(Collections.singletonList(observedRepo))
                .nextCursor(null)
                .build();

        when(findObservedRepos.execute(any())).thenReturn(response);
        when(getObservedRepoLatestDataProvider.get(observedRepo)).thenReturn(observedRepo);

        syncObservedReposData.execute();

        verify(findObservedRepos, times(1)).execute(expectedRequest);
        verify(getObservedRepoLatestDataProvider, times(1)).get(observedRepo);
        verify(observedRepoRepository, never()).save(any());
        verify(observedRepoSyncedEventPublisher, never()).publish(any());
    }

    @Test
    void testProcessObservedRepoEntry_NotFound() {
        UUID repoId = UUID.randomUUID();
        ObservedRepo observedRepo = ObservedRepo.builder()
                .id(repoId)
                .name("test-repo")
                .owner("test-owner")
                .build();

        FindObservedReposRequest expectedRequest = FindObservedReposRequest.builder()
                .next(true)
                .status(ObservedRepoStatus.ACTIVE)
                .pageSize(20)
                .build();

        FindObservedReposResponse response = FindObservedReposResponse.builder()
                .observedRepos(Collections.singletonList(observedRepo))
                .nextCursor(null)
                .build();

        when(findObservedRepos.execute(any())).thenReturn(response);
        when(getObservedRepoLatestDataProvider.get(observedRepo)).thenThrow(ObservedRepoExternalDataNotFoundException.class);

        syncObservedReposData.execute();

        verify(findObservedRepos, times(1)).execute(expectedRequest);
        verify(getObservedRepoLatestDataProvider, times(1)).get(observedRepo);
        verify(observedRepoRepository, times(1)).save(any());
        verify(observedRepoSyncedEventPublisher, never()).publish(any());
    }

    @Test
    void testProcessObservedRepoEntry_OptimisticLockingFailure() {
        UUID repoId = UUID.randomUUID();
        ObservedRepo observedRepo = ObservedRepo.builder()
                .id(repoId)
                .name("test-repo")
                .owner("test-owner")
                .build();

        FindObservedReposRequest expectedRequest = FindObservedReposRequest.builder()
                .next(true)
                .status(ObservedRepoStatus.ACTIVE)
                .pageSize(20)
                .build();

        FindObservedReposResponse response = FindObservedReposResponse.builder()
                .observedRepos(Collections.singletonList(observedRepo))
                .nextCursor(null)
                .build();

        when(findObservedRepos.execute(any())).thenReturn(response);
        when(getObservedRepoLatestDataProvider.get(observedRepo)).thenThrow(OptimisticLockingFailureException.class);

        syncObservedReposData.execute();

        verify(findObservedRepos, times(1)).execute(expectedRequest);
        verify(getObservedRepoLatestDataProvider, times(1)).get(observedRepo);
        verify(observedRepoRepository, never()).save(any());
        verify(observedRepoSyncedEventPublisher, never()).publish(any());
    }

    @Test
    void testProcessObservedRepoEntry_GeneralException() {
        UUID repoId = UUID.randomUUID();
        ObservedRepo observedRepo = ObservedRepo.builder()
                .id(repoId)
                .name("test-repo")
                .owner("test-owner")
                .build();

        FindObservedReposRequest expectedRequest = FindObservedReposRequest.builder()
                .next(true)
                .status(ObservedRepoStatus.ACTIVE)
                .pageSize(20)
                .build();

        FindObservedReposResponse response = FindObservedReposResponse.builder()
                .observedRepos(Collections.singletonList(observedRepo))
                .nextCursor(null)
                .build();

        when(findObservedRepos.execute(any())).thenReturn(response);
        when(getObservedRepoLatestDataProvider.get(observedRepo)).thenThrow(RuntimeException.class);

        syncObservedReposData.execute();

        verify(findObservedRepos, times(1)).execute(expectedRequest);
        verify(getObservedRepoLatestDataProvider, times(1)).get(observedRepo);
        verify(observedRepoRepository, never()).save(any());
        verify(observedRepoSyncedEventPublisher, never()).publish(any());
    }

    @Test
    void testProcessAllObservedRepos_Recursive() {
        List<ObservedRepo> firstPageEntries = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            ObservedRepo repo = ObservedRepo.builder()
                    .id(UUID.randomUUID())
                    .name("repo-" + i)
                    .owner("owner-" + i)
                    .build();
            firstPageEntries.add(repo);
        }
        FindObservedReposResponse firstPageResponse = FindObservedReposResponse.builder()
                .observedRepos(firstPageEntries)
                .nextCursor("nextCursor")
                .build();

        List<ObservedRepo> secondPageEntries = new ArrayList<>();
        for (int i = 6; i <= 10; i++) {
            ObservedRepo repo = ObservedRepo.builder()
                    .id(UUID.randomUUID())
                    .name("repo-" + i)
                    .owner("owner-" + i)
                    .build();
            secondPageEntries.add(repo);
        }
        FindObservedReposResponse secondPageResponse = FindObservedReposResponse.builder()
                .observedRepos(secondPageEntries)
                .nextCursor(null)
                .build();

        when(findObservedRepos.execute(any()))
                .thenReturn(firstPageResponse)
                .thenReturn(secondPageResponse);
        when(getObservedRepoLatestDataProvider.get(any())).thenAnswer(invocation -> invocation.getArgument(0));

        syncObservedReposData.execute();

        verify(findObservedRepos, atLeast(2)).execute(any());
        verify(getObservedRepoLatestDataProvider, times(10)).get(any());
    }
}

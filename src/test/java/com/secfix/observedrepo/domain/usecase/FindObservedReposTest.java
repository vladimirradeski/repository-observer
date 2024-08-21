package com.secfix.observedrepo.domain.usecase;

import com.secfix.observedrepo.domain.entity.ObservedRepo;
import com.secfix.observedrepo.domain.entity.ObservedRepoStatus;
import com.secfix.observedrepo.domain.entity.dto.FindObservedReposRequest;
import com.secfix.observedrepo.domain.entity.dto.FindObservedReposResponse;
import com.secfix.observedrepo.domain.repository.ObservedRepoRepository;
import com.secfix.observedrepo.domain.util.PageCursorUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FindObservedReposTest {

    @Mock
    private ObservedRepoRepository observedRepoRepository;

    @Mock
    private PageCursorUtils pageCursorUtils;

    @InjectMocks
    private FindObservedRepos findObservedRepos;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private List<ObservedRepo> createObservedRepos(Instant cursorInstant, int count) {
        List<ObservedRepo> observedRepos = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            observedRepos.add(ObservedRepo.builder()
                    .id(UUID.randomUUID())
                    .createdAt(cursorInstant.minusSeconds(60L * i))
                    .license("license")
                    .name("repoName" + (i + 1))
                    .openIssues(10 * (i + 1))
                    .owner("owner")
                    .stars(100 * (i + 1))
                    .status(ObservedRepoStatus.ACTIVE)
                    .updatedAt(cursorInstant.minusSeconds(30L * i))
                    .url("http://repo" + (i + 1) + ".url")
                    .build());
        }
        return observedRepos;
    }

    @Test
    void testExecuteNext() {
        Instant cursorInstant = Instant.now();
        String cursor = "testCursor";
        int pageSize = 2;
        FindObservedReposRequest request = FindObservedReposRequest.builder()
                .owner("owner")
                .name("repoName")
                .status(ObservedRepoStatus.ACTIVE)
                .license("license")
                .cursor(cursor)
                .pageSize(pageSize)
                .next(true)
                .build();

        List<ObservedRepo> observedRepos = createObservedRepos(cursorInstant, pageSize + 1);

        when(pageCursorUtils.decodePageCursor(cursor)).thenReturn(cursorInstant);
        when(observedRepoRepository.findNextByCursor(
                "owner", "repoName", ObservedRepoStatus.ACTIVE, "license", cursorInstant, pageSize + 1
        )).thenReturn(observedRepos);
        when(pageCursorUtils.encodePageCursor(any(Instant.class))).thenReturn("encodedCursor");

        FindObservedReposResponse response = findObservedRepos.execute(request);

        assertNotNull(response);
        assertEquals(pageSize, response.observedRepos().size());
        assertEquals("encodedCursor", response.nextCursor());
        assertEquals("encodedCursor", response.previousCursor());
        verify(pageCursorUtils).decodePageCursor(cursor);
        verify(observedRepoRepository).findNextByCursor(
                "owner", "repoName", ObservedRepoStatus.ACTIVE, "license", cursorInstant, pageSize + 1
        );
        verify(pageCursorUtils,atLeast(2)).encodePageCursor(any(Instant.class));
    }

    @Test
    void testExecutePrevious() {
        Instant cursorInstant = Instant.now();
        String cursor = "testCursor";
        int pageSize = 2;
        FindObservedReposRequest request = FindObservedReposRequest.builder()
                .owner("owner")
                .name("repoName")
                .status(ObservedRepoStatus.ACTIVE)
                .license("license")
                .cursor(cursor)
                .pageSize(pageSize)
                .next(false)
                .build();

        List<ObservedRepo> observedRepos = createObservedRepos(cursorInstant, pageSize + 1);

        when(pageCursorUtils.decodePageCursor(cursor)).thenReturn(cursorInstant);
        when(observedRepoRepository.findPreviousByCursor(
                "owner", "repoName", ObservedRepoStatus.ACTIVE, "license", cursorInstant, pageSize + 1
        )).thenReturn(observedRepos);
        when(pageCursorUtils.encodePageCursor(any(Instant.class))).thenReturn("encodedCursor");

        FindObservedReposResponse response = findObservedRepos.execute(request);

        assertNotNull(response);
        assertEquals(pageSize, response.observedRepos().size());
        assertEquals("encodedCursor", response.nextCursor());
        assertEquals("encodedCursor", response.previousCursor());
        verify(pageCursorUtils).decodePageCursor(cursor);
        verify(observedRepoRepository).findPreviousByCursor(
                "owner", "repoName", ObservedRepoStatus.ACTIVE, "license", cursorInstant, pageSize + 1
        );
        verify(pageCursorUtils,atLeast(2)).encodePageCursor(any(Instant.class));

    }
}
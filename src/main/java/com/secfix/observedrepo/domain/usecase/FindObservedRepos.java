package com.secfix.observedrepo.domain.usecase;

import com.secfix.observedrepo.domain.entity.ObservedRepo;
import com.secfix.observedrepo.domain.entity.dto.FindObservedReposRequest;
import com.secfix.observedrepo.domain.entity.dto.FindObservedReposResponse;
import com.secfix.observedrepo.domain.repository.ObservedRepoRepository;
import com.secfix.observedrepo.domain.util.PageCursorUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class FindObservedRepos {

    private final ObservedRepoRepository observedRepoRepository;
    private final PageCursorUtils pageCursorUtils;

    public FindObservedRepos(ObservedRepoRepository observedRepoRepository, PageCursorUtils pageCursorUtils) {
        this.observedRepoRepository = observedRepoRepository;
        this.pageCursorUtils = pageCursorUtils;
    }

    public FindObservedReposResponse execute(FindObservedReposRequest request) {
        Instant decodedCursor = pageCursorUtils.decodePageCursor(request.cursor());

        List<ObservedRepo> observedRepos;
        boolean hasNext = false;
        boolean hasPrevious = false;

        if (request.next()) {
            observedRepos = observedRepoRepository.findNextByCursor(
                    request.owner(),
                    request.name(),
                    request.status(),
                    request.license(),
                    decodedCursor,
                    request.pageSize() + 1
            );

            hasPrevious = request.cursor() != null;
            if (!observedRepos.isEmpty() && observedRepos.size() > request.pageSize()) {
                hasNext = true;
                observedRepos.removeLast();
            }
        } else {
            observedRepos = observedRepoRepository.findPreviousByCursor(
                    request.owner(),
                    request.name(),
                    request.status(),
                    request.license(),
                    decodedCursor,
                    request.pageSize() + 1
            );

            hasNext = true;
            if (!observedRepos.isEmpty() && observedRepos.size() > request.pageSize()) {
                hasPrevious = true;
                observedRepos.removeFirst();
            }

        }

        return FindObservedReposResponse.builder()
                .nextCursor(getNextCursor(observedRepos, hasNext))
                .previousCursor(getPreviousCursor(observedRepos, hasPrevious))
                .observedRepos(observedRepos)
                .build();
    }

    private String getNextCursor(List<ObservedRepo> observedRepos, boolean hasNext) {
        if (hasNext) {
            ObservedRepo cursorEntry = observedRepos.getLast();
            return pageCursorUtils.encodePageCursor(cursorEntry.createdAt());
        }
        return null;
    }

    private String getPreviousCursor(List<ObservedRepo> observedRepos, boolean hasPrevious) {
        if (hasPrevious && !observedRepos.isEmpty()) {
            ObservedRepo cursorEntry = observedRepos.getFirst();
            return pageCursorUtils.encodePageCursor(cursorEntry.createdAt());
        }
        return null;
    }
}

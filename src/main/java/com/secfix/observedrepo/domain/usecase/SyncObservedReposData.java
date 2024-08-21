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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class SyncObservedReposData {

    private static final Logger log = LoggerFactory.getLogger(SyncObservedReposData.class);
    private final ObservedRepoRepository observedRepoRepository;
    private final GetObservedRepoLatestDataProvider getObservedRepoLatestDataProvider;
    private final FindObservedRepos findObservedRepos;
    private final ObservedRepoSyncedEventPublisher observedRepoSyncedEventPublisher;
    private int syncedCount;
    private int unchangedCount;
    private int skippedCount;
    private int invalidCount;

    public SyncObservedReposData(ObservedRepoRepository observedRepoRepository, GetObservedRepoLatestDataProvider getObservedRepoLatestDataProvider, FindObservedRepos findObservedRepos, ObservedRepoSyncedEventPublisher observedRepoSyncedEventPublisher) {
        this.observedRepoRepository = observedRepoRepository;
        this.getObservedRepoLatestDataProvider = getObservedRepoLatestDataProvider;
        this.findObservedRepos = findObservedRepos;
        this.observedRepoSyncedEventPublisher = observedRepoSyncedEventPublisher;
    }

    public void execute() {
        log.info("OBSERVED_REPOS_SYNC_STARTED");

        syncedCount = 0;
        unchangedCount = 0;
        skippedCount = 0;
        invalidCount = 0;

        FindObservedReposRequest request = FindObservedReposRequest.builder()
                .next(true)
                .pageSize(20)
                .status(ObservedRepoStatus.ACTIVE)
                .build();

        processAllObservedRepos(request);

        log.info("OBSERVED_REPOS_SYNC_FINISHED | synced: {}, unchanged: {}, skipped: {}, invalid: {}", syncedCount, unchangedCount, skippedCount, invalidCount);

    }

    public void processAllObservedRepos(FindObservedReposRequest request) {
        FindObservedReposResponse response = findObservedRepos.execute(request);
        List<ObservedRepo> observedReposEntries = response.observedRepos();

        for (ObservedRepo entry : observedReposEntries) {
            processObservedRepoEntry(entry);
        }

        String nextCursor = response.nextCursor();
        if (nextCursor != null) {
            FindObservedReposRequest nextRequest = request.withCursor(nextCursor);
            processAllObservedRepos(nextRequest);
        }
    }

    public void processObservedRepoEntry(ObservedRepo observedRepo) {
        try {
            ObservedRepo observedRepoLatestData = getObservedRepoLatestDataProvider.get(observedRepo);
            log.info("Syncing observed repo | incomingData: {}", observedRepoLatestData);
            if (!isRepoDataUnchanged(observedRepo, observedRepoLatestData)) {
                ObservedRepo updatedObservedRepo = ObservedRepo.builder()
                        .id(observedRepo.id())
                        .url(observedRepoLatestData.url())
                        .owner(observedRepoLatestData.owner())
                        .name(observedRepoLatestData.name())
                        .stars(observedRepoLatestData.stars())
                        .openIssues(observedRepoLatestData.openIssues())
                        .license(observedRepoLatestData.license())
                        .createdAt(observedRepo.createdAt())
                        .updatedAt(observedRepo.updatedAt())
                        .status(observedRepo.status())
                        .version(observedRepo.version())
                        .build();

                observedRepoRepository.save(updatedObservedRepo);
                publishEvent(updatedObservedRepo);
                log.info("Observed repo data synced | previousData: {}, updatedData: {}", observedRepo, updatedObservedRepo);
                syncedCount++;
            } else {
                log.info("Observer repo data unchanged | previousData: {}, incomingData: {}", observedRepo, observedRepoLatestData);
                unchangedCount++;
            }
        } catch (ObservedRepoExternalDataNotFoundException e) {
            log.info("Observer repo data not found, will be marked as invalid | name: {}, owner: {}", observedRepo.name(), observedRepo.owner());
            ObservedRepo invalidObservedRepo = observedRepo.invalidate();
            observedRepoRepository.save(invalidObservedRepo);
            invalidCount++;
        } catch (OptimisticLockingFailureException e) {
            log.warn("Optimistic locking failure, skipping. It will be retried next cycle | name: {}, owner: {}", observedRepo.name(), observedRepo.owner());
            skippedCount++;
        } catch (Exception e) {
            log.error("Observer repo failed to be synced, will be skipped | name: {}, owner: {}, error: {}", observedRepo.name(), observedRepo.owner(), e.getMessage());
            skippedCount++;
        }
    }

    private void publishEvent(ObservedRepo observedRepo) {
        ObservedRepoSyncedEvent event = ObservedRepoSyncedEvent
                .builder()
                .observedRepo(observedRepo)
                .build();

        observedRepoSyncedEventPublisher.publish(event);
    }

    private boolean isRepoDataUnchanged(ObservedRepo currentRepoData, ObservedRepo latestRepoData) {
        return Objects.equals(currentRepoData.url(), latestRepoData.url())
                && Objects.equals(currentRepoData.owner(), latestRepoData.owner())
                && Objects.equals(currentRepoData.name(), latestRepoData.name())
                && Objects.equals(currentRepoData.stars(), latestRepoData.stars())
                && Objects.equals(currentRepoData.openIssues(), latestRepoData.openIssues())
                && Objects.equals(currentRepoData.license(), latestRepoData.license());
    }
}

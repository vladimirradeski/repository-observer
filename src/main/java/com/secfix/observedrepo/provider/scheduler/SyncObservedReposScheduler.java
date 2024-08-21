package com.secfix.observedrepo.provider.scheduler;

import com.secfix.observedrepo.domain.usecase.SyncObservedReposData;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class SyncObservedReposScheduler {

    private final SyncObservedReposData syncObservedReposData;

    public SyncObservedReposScheduler(SyncObservedReposData syncObservedReposData) {
        this.syncObservedReposData = syncObservedReposData;
    }

    @Scheduled(fixedRateString = "${secfix.scheduler.fixedRate.minutes}", timeUnit = TimeUnit.MINUTES)
    @SchedulerLock(name = "SyncObservedReposScheduler.scheduledTask", lockAtMostFor = "PT60M")
    public void scheduledTask() {
        syncObservedReposData.execute();
    }
}

package com.shop.order.jobs;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

@Slf4j
@Component
public class CelebrationJob {

    @Scheduled(cron = "0 0 14 * * *")
    @SchedulerLock(name = "celebrationJob", lockAtMostFor = "1m", lockAtLeastFor = "5s")
    public void runDailyAt2PM() throws InterruptedException {
        log.info("It's again the time to celebrate the works of great builders of Java & Spring Boot. URA!");
    }
}

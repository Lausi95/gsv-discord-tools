package de.lausi.gsvdiscordtools.rankedwatcher.adapter.scheduling

import de.lausi.gsvdiscordtools.rankedwatcher.application.MatchApplicationService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
private class ScheduledTasks(private val matchApplicationService: MatchApplicationService) {

  private val log = LoggerFactory.getLogger(ScheduledTasks::class.java)

  @Scheduled(fixedDelay = 5, initialDelay = 1, timeUnit = TimeUnit.MINUTES)
  fun crawlMatches() {
    log.info("Crawling Matches")
    matchApplicationService.crawlMatches()
  }
}

package de.lausi.gsvdiscordtools.rankedwatcher.application

import de.lausi.gsvdiscordtools.rankedwatcher.domain.model.match.Match
import de.lausi.gsvdiscordtools.rankedwatcher.domain.model.match.MatchReporter
import de.lausi.gsvdiscordtools.rankedwatcher.domain.model.match.MatchRepository
import de.lausi.gsvdiscordtools.rankedwatcher.domain.model.match.MatchTracker
import de.lausi.gsvdiscordtools.rankedwatcher.domain.model.player.PlayerRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class MatchApplicationService(
  private val matchRepository: MatchRepository,
  private val matchTracker: MatchTracker,
  private val playerRepository: PlayerRepository,
  private val matchReporter: MatchReporter,
) {

  private val log = LoggerFactory.getLogger(MatchApplicationService::class.java)

  fun crawlMatches() {
    val summonerIds = playerRepository.findAll().map { it.summonerId }
    summonerIds.asSequence()
      .flatMap { matchRepository.getLatestMatchIdsBySummonerId(it) }
      .filter { !matchTracker.isTracked(it) }
      .onEach { matchTracker.markTracked(it) }
      .map { matchRepository.getMatch(it, summonerIds) }
      .filter { checkRankedMatch(it) }
      .forEach { matchReporter.reportMatch(it) }
  }

  private fun checkRankedMatch(match: Match): Boolean {
    val isRanked = match.isRankedMatch()
    log.info("Match with ${match.matchId} is ranked match: $isRanked {}", if (isRanked) "" else " -> skipped.")
    return isRanked
  }
}

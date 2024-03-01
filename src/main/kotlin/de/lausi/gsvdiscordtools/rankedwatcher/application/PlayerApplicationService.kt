package de.lausi.gsvdiscordtools.rankedwatcher.application

import de.lausi.gsvdiscordtools.rankedwatcher.domain.model.match.MatchRepository
import de.lausi.gsvdiscordtools.rankedwatcher.domain.model.match.MatchTracker
import de.lausi.gsvdiscordtools.rankedwatcher.domain.model.player.*
import org.springframework.stereotype.Component

@Component
class PlayerApplicationService(
  val playerRepository: PlayerRepository,
  val playerResolver: PlayerResolver,
  val matchRepository: MatchRepository,
  val matchTracker: MatchTracker,
) {

  fun getPlayers(): List<Player> {
    return playerRepository.findAll()
  }

  fun addPlayer(summonerName: SummonerName, tagLine: TagLine): Player {
    val player = playerResolver.getPlayer(summonerName, tagLine).let { playerRepository.save(it) }

    val matchIds = matchRepository.getLatestMatchIdsBySummonerId(player.summonerId)
    matchIds.forEach { matchTracker.markTracked(it) }

    return player
  }

  fun deletePlayer(summonerName: SummonerName, tagLine: TagLine) {
    val player = playerRepository.findBySummonerNameAndTagLine(summonerName, tagLine)
    player?.let { playerRepository.delete(it) }
  }
}

package de.lausi.gsvdiscordtools.rankedwatcher.application

import de.lausi.gsvdiscordtools.rankedwatcher.domain.model.player.*
import org.springframework.stereotype.Component

@Component
class PlayerApplicationService(val playerRepository: PlayerRepository, val playerResolver: PlayerResolver) {

  fun getPlayers(): List<Player> {
    return playerRepository.findAll()
  }

  fun addPlayer(summonerName: SummonerName, tagLine: TagLine): Player? {
    return playerResolver.getPlayer(summonerName, tagLine)?.let { playerRepository.save(it) }
  }

  fun deletePlayer(summonerName: SummonerName, tagLine: TagLine) {
    playerRepository.findBySummonerNameAndTagLine(summonerName, tagLine)?.let { playerRepository.delete(it) }
  }
}

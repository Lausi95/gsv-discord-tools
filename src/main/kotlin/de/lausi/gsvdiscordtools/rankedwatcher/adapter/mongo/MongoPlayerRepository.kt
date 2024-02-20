package de.lausi.gsvdiscordtools.rankedwatcher.adapter.mongo

import de.lausi.gsvdiscordtools.rankedwatcher.domain.model.player.*
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component

@Document("player")
private data class PlayerEntity(
  @Id val id: String,
  val summonerName: String,
  val tagLine: String,
) {

  constructor(player: Player): this(
    player.summonerId.value,
    player.summonerName.value,
    player.tagLine.value,
  )

  fun toPlayer() = Player(
    SummonerId(id),
    SummonerName(summonerName),
    TagLine(tagLine),
  )
}

private interface PlayerEntityRepository : MongoRepository<PlayerEntity, String> {

  fun findBySummonerNameAndTagLine(summonerName: String, tagLine: String): PlayerEntity?
}

@Component
private class MongoPlayerRepository(val playerEntityRepository: PlayerEntityRepository): PlayerRepository {

  override fun findAll(): List<Player> {
    return playerEntityRepository.findAll().map { it.toPlayer() }
  }

  override fun save(player: Player): Player {
    return playerEntityRepository.save(PlayerEntity(player)).toPlayer()
  }

  override fun findBySummonerNameAndTagLine(summonerName: SummonerName, tagLine: TagLine): Player? {
    return playerEntityRepository.findBySummonerNameAndTagLine(summonerName.value, tagLine.value)?.toPlayer()
  }

  override fun delete(player: Player) {
    playerEntityRepository.deleteById(player.summonerId.value)
  }
}

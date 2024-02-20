package de.lausi.gsvdiscordtools.rankedwatcher.domain.model.player

data class SummonerId(val value: String)

data class SummonerName(val value: String)

data class TagLine(val value: String)

data class Player(
  val summonerId: SummonerId,
  val summonerName: SummonerName,
  val tagLine: TagLine,
)

interface PlayerRepository {

  fun findAll(): List<Player>

  fun save(player: Player): Player

  fun findBySummonerNameAndTagLine(summonerName: SummonerName, tagLine: TagLine): Player?

  fun delete(player: Player)
}

interface PlayerResolver {

  fun getPlayer(summonerName: SummonerName, tagLine: TagLine): Player?
}

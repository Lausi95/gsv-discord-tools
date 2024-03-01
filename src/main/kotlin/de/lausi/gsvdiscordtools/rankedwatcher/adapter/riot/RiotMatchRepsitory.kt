package de.lausi.gsvdiscordtools.rankedwatcher.adapter.riot

import de.lausi.gsvdiscordtools.rankedwatcher.domain.model.match.*
import de.lausi.gsvdiscordtools.rankedwatcher.domain.model.player.SummonerId
import de.lausi.gsvdiscordtools.rankedwatcher.domain.model.player.SummonerName
import org.slf4j.LoggerFactory
import org.springframework.web.client.RestTemplate

private data class MatchDto(
  val info: InfoDto
)

private data class InfoDto(
  val gameId: Long,
  val gameDuration: Long,
  val gameEndTimestamp: Long? = null,
  val gameStartTimestamp: Long,
  val participants: List<ParticipantDto>,
  val queueId: Int,
)

private data class ParticipantDto(
  val win: Boolean,
  val summonerId: String,
  val puuid: String,
  val summonerName: String,
  val championName: String,
  val teamPosition: String,
  val kills: Int,
  val deaths: Int,
  val assists: Int,
  val pentaKills: Int,
  val quadraKills: Int
)

class RiotMatchRepsitory(private val restTemplate: RestTemplate): MatchRepository {

  private val log = LoggerFactory.getLogger(RiotMatchRepsitory::class.java)

  override fun getLatestMatchIdsBySummonerId(summonerId: SummonerId, count: Int): List<MatchId> {
    try {
      val url = "/lol/match/v5/matches/by-puuid/${summonerId.value}/ids?count=$count"
      val response = restTemplate.getForEntity(url, Array<String>::class.java)
      val matchIds = response.body?.map { MatchId(it) }

      return requireNotNull(matchIds) { "Could not load matchIds by $summonerId. Repsonse is Empty." }
    } catch (ex: Exception) {
      log.warn("Riot API reponded with error: {}", ex.message)
      return emptyList()
    }
  }

  override fun getMatch(matchId: MatchId, summonerIds: List<SummonerId>): Match {
    val url = "/lol/match/v5/matches/${matchId.value}"
    val response = restTemplate.getForEntity(url, MatchDto::class.java)
    val match = response.body?.mapToMatch(summonerIds)

    return requireNotNull(match) { "Could not load match by $matchId. Response is Empty." }
  }
}

private val QUEUE_MAP = mapOf(
  420 to MatchType.SOLOQ,
  440 to MatchType.FLEX,
)

private val POSITION_MAP = mapOf(
  "TOP" to Position.TOP,
  "JUNGLE" to Position.JUNGLE,
  "MIDDLE" to Position.MID,
  "BOTTOM" to Position.BOTTON,
  "UTILITY" to Position.SUPPORT,
)

private fun MatchDto.mapToMatch(summonerIds: List<SummonerId>): Match {
  val summonerIdValues = summonerIds.map { it.value }
  val participantDtos = info.participants.filter {
    summonerIdValues.contains(it.puuid)
  }

  val win = participantDtos.any { it.win }

  val participants = participantDtos.map {
    val position = POSITION_MAP[it.teamPosition] ?: Position.UNKNOWN
    Participant(
      SummonerId(it.summonerId),
      SummonerName(it.summonerName),
      Champion(it.championName),
      position,
      it.kills,
      it.deaths,
      it.assists,
      it.pentaKills > 0,
      it.quadraKills > 0
    )
  }

  val matchId = MatchId(info.gameId.toString())
  val matchType = QUEUE_MAP[info.queueId] ?: MatchType.UNKNOWN
  return Match(matchId, win, participants, matchType)
}

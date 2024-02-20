package de.lausi.gsvdiscordtools.rankedwatcher.adapter.riot

import de.lausi.gsvdiscordtools.rankedwatcher.domain.model.player.*
import org.slf4j.LoggerFactory
import org.springframework.web.client.RestTemplate

private data class RiotPlayerResource(
  val puuid: String,
  val gameName: String,
  val tagLine: String,
)

internal class RiotPlayerResolver(val restTemplate: RestTemplate) : PlayerResolver {

  private val log = LoggerFactory.getLogger(RiotPlayerResolver::class.java)

  override fun getPlayer(summonerName: SummonerName, tagLine: TagLine): Player? {
    val url = "/riot/account/v1/accounts/by-riot-id/${summonerName.value}/${tagLine.value}"
    log.info("Fetching player from riot: $url")
    val response = restTemplate.getForEntity(url, RiotPlayerResource::class.java)

    val player = response.body?.let {
      val summonerId = SummonerId(it.puuid)
      Player(summonerId, summonerName, tagLine)
    }
    log.info("Loaded player from riot: $player")

    return player
  }
}

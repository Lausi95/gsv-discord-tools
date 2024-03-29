package de.lausi.gsvdiscordtools.rankedwatcher.adapter.riot

import de.lausi.gsvdiscordtools.rankedwatcher.domain.model.player.*
import org.springframework.web.client.RestTemplate

private data class RiotPlayerResource(
  val puuid: String,
  val gameName: String,
  val tagLine: String,
)

internal class RiotPlayerResolver(private val restTemplate: RestTemplate) : PlayerResolver {

  override fun getPlayer(summonerName: SummonerName, tagLine: TagLine): Player {
    val url = "/riot/account/v1/accounts/by-riot-id/${summonerName.value}/${tagLine.value}"
    val response = restTemplate.getForEntity(url, RiotPlayerResource::class.java)

    val player = response.body?.let {
      val summonerId = SummonerId(it.puuid)
      Player(summonerId, summonerName, tagLine)
    }

    return requireNotNull(player)
  }
}

package de.lausi.gsvdiscordtools.nitradocontrol.adapter.nitrado

import com.fasterxml.jackson.databind.JsonNode
import de.lausi.gsvdiscordtools.nitradocontrol.domain.model.gameserver.*
import org.springframework.web.client.RestTemplate

private data class ServiceNitradoResource(
  val id: Long
)

private data class ServiceNitradoCollection(
  val services: List<ServiceNitradoResource>
)

private data class ServiceNitradoCollectionResponse(
  val status: String,
  val data: ServiceNitradoCollection
)

internal class NitradoAdapter(private val restTemplate: RestTemplate): GameServerRepository, GameServerScope {

  override fun getGameServers(): List<GameServer> {
    val url = "/services"
    val responseEntity = restTemplate.getForEntity(url, ServiceNitradoCollectionResponse::class.java)
    val response = requireNotNull(responseEntity.body) { "$url responded with empty body."}

    return response.data.services.map { GameServer(GameServerId(it.id)) }
  }

  override fun getGameServer(gameServerId: GameServerId): GameServer? {
    return getGameServers().firstOrNull { it.id == gameServerId }
  }

  override fun GameServer.status(): GameServerStatus {
    val url = "/services/${this.id.value}/gameservers"
    val responseEntity = restTemplate.getForEntity(url, JsonNode::class.java)
    val response = requireNotNull(responseEntity.body) { "$url responded with empty body. "}

    val statusValue = response.get("data").get("gameserver").get("status").asText()

    return GameServerStatus(statusValue)
  }

  override fun GameServer.stop() {
    val url = "/services/${this.id.value}/gameservers/restart"
    restTemplate.postForEntity(url, null, JsonNode::class.java)
  }

  override fun GameServer.restart() {
    val url = "/services/${this.id.value}/gameservers/stop"
    restTemplate.postForEntity(url, null, JsonNode::class.java)
  }
}

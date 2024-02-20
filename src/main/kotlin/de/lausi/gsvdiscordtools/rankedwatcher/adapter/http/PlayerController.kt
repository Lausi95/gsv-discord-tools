package de.lausi.gsvdiscordtools.rankedwatcher.adapter.http

import de.lausi.gsvdiscordtools.rankedwatcher.application.PlayerApplicationService
import de.lausi.gsvdiscordtools.rankedwatcher.domain.model.player.Player
import de.lausi.gsvdiscordtools.rankedwatcher.domain.model.player.SummonerName
import de.lausi.gsvdiscordtools.rankedwatcher.domain.model.player.TagLine
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

private data class PlayerResource(
  val summonerId: String,
  val summonerName: String,
  val tagLine: String,
) {

  constructor(player: Player) : this(
    player.summonerId.value,
    player.summonerName.value,
    player.tagLine.value,
  )
}

private data class PlayerCollection(
  val players: List<PlayerResource>
)

private data class AddPlayerRequest(
  val summonerName: String,
  val tagLine: String,
)

private data class DeletePlayerRequest(
  val summonerName: String,
  val tagLine: String,
)

@RestController
@RequestMapping("/players")
private class PlayerController(val playerApplicationService: PlayerApplicationService) {

  @GetMapping
  fun getPlayers(): PlayerCollection {
    val players = playerApplicationService.getPlayers()
    return PlayerCollection(players.map { PlayerResource(it) })
  }

  @PostMapping
  fun addPlayer(@RequestBody request: AddPlayerRequest): ResponseEntity<PlayerResource> {
    val summonerName = SummonerName(request.summonerName)
    val tagLine = TagLine(request.tagLine)

    val player = playerApplicationService.addPlayer(summonerName, tagLine)
      ?: return ResponseEntity.notFound().build()

    return ResponseEntity.ok(PlayerResource(player))
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  fun deleteMapping(@RequestBody request: DeletePlayerRequest) {
    val summonerName = SummonerName(request.summonerName)
    val tagLine = TagLine(request.tagLine)

    playerApplicationService.deletePlayer(summonerName, tagLine)
  }
}

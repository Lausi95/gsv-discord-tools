package de.lausi.gsvdiscordtools.nitradocontrol.domain.model.gameserver

interface GameServerRepository {

  fun getGameServers(): List<GameServer>

  fun getGameServer(gameServerId: GameServerId): GameServer?
}

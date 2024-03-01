package de.lausi.gsvdiscordtools.nitradocontrol.domain.model.gameserver

data class GameServerId(val value: Long)

data class GameServerStatus(val value: String) {

  companion object {
    val STARTED = GameServerStatus("started")
  }
}

enum class StartGameServerResult {
  ALREADY_RUNNING,
  STARTED,
}

class GameServer(val id: GameServerId)

interface GameServerScope {

  fun GameServer.status(): GameServerStatus

  fun GameServer.stop()

  fun GameServer.restart()

  fun GameServer.start(): StartGameServerResult {
    if (this.status() == GameServerStatus.STARTED) {
      return StartGameServerResult.ALREADY_RUNNING
    }

    this.restart()

    return StartGameServerResult.STARTED
  }
}

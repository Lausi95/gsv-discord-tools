package de.lausi.gsvdiscordtools.nitradocontrol.adapter.discord

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import de.lausi.gsvdiscordtools.nitradocontrol.domain.model.gameserver.GameServerId
import de.lausi.gsvdiscordtools.nitradocontrol.domain.model.gameserver.GameServerRepository
import de.lausi.gsvdiscordtools.nitradocontrol.domain.model.gameserver.GameServerScope
import de.lausi.gsvdiscordtools.nitradocontrol.domain.model.gameserver.StartGameServerResult
import org.springframework.stereotype.Component

@Component
internal class ServerControlDiscordExtension(
  private val gameServerRepository: GameServerRepository,
  private val gameServerScope: GameServerScope,
  private val serverControlDiscordProperties: ServerControlDiscordProperties,
) : Extension() {

  override val name = "nitrado-server-control"

  override suspend fun setup() {
    publicSlashCommand {
      name = "palworld-status"
      description = "Ueberprueft den Status des Palworld Servers"
      guild(serverControlDiscordProperties.guildId)

      action {
        with(gameServerScope) {
          val gameServer = gameServerRepository.getGameServer(GameServerId(serverControlDiscordProperties.palworldServerId))
          if (gameServer == null) {
            respond {
              content = "Palworld server konnte nicht gefunden werden."
            }
            return@action
          }

          val status = gameServer.status()
          respond {
            content = "Server Status: ${status.value}"
          }
        }
      }
    }

    publicSlashCommand {
      name = "palworld-start"
      description = "Startet den Palworld Server"
      guild(serverControlDiscordProperties.guildId)

      action {
        with(gameServerScope) {
          val gameServer = gameServerRepository.getGameServer(GameServerId(serverControlDiscordProperties.palworldServerId))
          if (gameServer == null) {
            respond {
              content = "Palworld Server konnte nicht gefunden werden."
            }
            return@action
          }

          val result = gameServer.start()
          when (result) {
            StartGameServerResult.STARTED -> respond {
              content = "Palworld Server wird gestartet..."
            }

            StartGameServerResult.ALREADY_RUNNING -> respond {
              content = "Palworld Server laeuft bereits..."
            }
          }
        }
      }
    }

    publicSlashCommand {
      name = "palworld-restart"
      description = "Startet den Palworld Server neu"
      guild(serverControlDiscordProperties.guildId)

      action {
        with(gameServerScope) {

          val gameServer = gameServerRepository.getGameServer(GameServerId(serverControlDiscordProperties.palworldServerId))
          if (gameServer == null) {
            respond {
              content = "Palworld Server konnte nicht gefunden werden."
            }
            return@action
          }

          gameServer.restart()

          respond {
            content = "Palworld server wird neu gestartet..."
          }
        }
      }
    }

    publicSlashCommand {
      name = "palworld-stop"
      description = "Stoppt den palworld server"
      guild(serverControlDiscordProperties.guildId)

      action {
        with(gameServerScope) {
          val gameServer = gameServerRepository.getGameServer(GameServerId(serverControlDiscordProperties.palworldServerId))
          if (gameServer == null) {
            respond {
              content = "Palworld Server konnte nicht gefunden werden"
            }
            return@action
          }

          gameServer.stop()

          respond {
            content = "Palworld Server wird gestoppt..."
          }
        }
      }
    }
  }
}

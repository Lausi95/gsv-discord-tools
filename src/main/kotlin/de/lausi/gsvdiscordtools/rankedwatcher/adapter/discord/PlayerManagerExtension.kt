package de.lausi.gsvdiscordtools.rankedwatcher.adapter.discord

import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.converters.impl.string
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import de.lausi.gsvdiscordtools.discordbot.DiscordBotProperties
import de.lausi.gsvdiscordtools.rankedwatcher.application.PlayerApplicationService
import de.lausi.gsvdiscordtools.rankedwatcher.domain.model.player.SummonerName
import de.lausi.gsvdiscordtools.rankedwatcher.domain.model.player.TagLine
import org.springframework.stereotype.Component

@Component
private class PlayerManagerExtension(
  val playerApplicationService: PlayerApplicationService,
  val discordBotProperties: DiscordBotProperties,
) : Extension() {

  override val name = "player-manager"

  override suspend fun setup() {
    publicSlashCommand(::AddPlayerArgs) {
      name = "add_player"
      description = "Adds a player to the ranked watcher"
      guild(discordBotProperties.guildId)

      action {
        val summonerName = SummonerName(arguments.summonerName)
        val tagLine = TagLine(arguments.tagLine)

        try {
          playerApplicationService.addPlayer(summonerName, tagLine)
          respond {
            content = "Summoner with summoner name ${summonerName.value}#${summonerName.value} was added to the watcher!"
          }
        } catch (ex: Exception) {
          respond {
            content = "Could not add summoner with summoner name ${summonerName.value}#${tagLine.value} to the watcher. Reason: ${ex.message}"
          }
        }
      }
    }

    publicSlashCommand(::RemovePlayerArgs) {
      name = "remove_player"
      description = "Removes a player from the ranked watcher"
      guild(discordBotProperties.guildId)

      action {
        val summonerName = SummonerName(arguments.summonerName)
        val tagLine = TagLine(arguments.tagLine)

        try {
          playerApplicationService.deletePlayer(summonerName, tagLine)
          respond {
            content = "Summoner '${summonerName.value}#${tagLine.value}' as requested to be removed from the ranked watcher"
          }
        } catch (ex: Exception) {
          respond {
            content = "Could not remove summoner with summoner name ${summonerName.value}#${tagLine.value}. Reason: ${ex.message}"
          }
        }
      }
    }
  }

  inner class AddPlayerArgs: Arguments() {
    val summonerName by string {
      name = "summoner_name"
      description = "Name of the summoner to track"
    }
    val tagLine by string {
      name = "tag_line"
      description = "Player Tag-Line"
    }
  }

  inner class RemovePlayerArgs: Arguments() {
    val summonerName by string {
      name = "summoner_name"
      description = "Name of the summoner to track"
    }
    val tagLine by string {
      name = "tag_line"
      description = "Player Tag-Line"
    }
  }
}

package de.lausi.gsvdiscordtools.nitradocontrol.adapter.discord

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@ConfigurationProperties(prefix = "discord.server-control")
internal data class ServerControlDiscordProperties(
  val guildId: Long,
  val palworldServerId: Long,
)

@Configuration
@EnableConfigurationProperties(ServerControlDiscordProperties::class)
private class ServerControlDiscordConfiguration

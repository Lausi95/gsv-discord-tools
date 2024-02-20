package de.lausi.gsvdiscordtools.rankedwatcher.adapter.discord

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@ConfigurationProperties("discord.ranked-watcher")
internal data class RankedWatcherDiscordProperties(
  val guildId: Long,
  val webhookUrl: String,
  val avatarUrl: String,
)

@Configuration
@EnableConfigurationProperties(RankedWatcherDiscordProperties::class)
private class RankedWatcherDiscordConfiguration {
}

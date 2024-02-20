package de.lausi.gsvdiscordtools.discordbot

import com.kotlindiscord.kord.extensions.ExtensibleBot
import com.kotlindiscord.kord.extensions.extensions.Extension
import kotlinx.coroutines.runBlocking
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener

@ConfigurationProperties("discord")
data class DiscordBotProperties(
  val botToken: String,
)

@Configuration
@EnableConfigurationProperties(DiscordBotProperties::class)
private class DiscordBotConfiguration(
  private val discordBotProperties: DiscordBotProperties,
  private val discordBotExtension: List<Extension>,
) {

  @EventListener(ApplicationStartedEvent::class)
  fun startDiscordBot() {
    runBlocking {
      val discordBot = ExtensibleBot(discordBotProperties.botToken) {
        chatCommands { enabled = true }
        extensions {
          discordBotExtension.forEach { add { it }}
        }
      }
      discordBot.start()
    }
  }
}

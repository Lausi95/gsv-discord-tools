package de.lausi.gsvdiscordtools.rankedwatcher.adapter.discord

import club.minnced.discord.webhook.WebhookClient
import club.minnced.discord.webhook.send.WebhookEmbed
import club.minnced.discord.webhook.send.WebhookEmbedBuilder
import club.minnced.discord.webhook.send.WebhookMessageBuilder
import de.lausi.gsvdiscordtools.rankedwatcher.domain.model.match.Match
import de.lausi.gsvdiscordtools.rankedwatcher.domain.model.match.MatchReporter
import org.springframework.stereotype.Component

enum class Color(val value: Int) {
  RED(0xFF0000),
  GREEN(0x00FF00);

  companion object {

    fun fromWin(win: Boolean): Color = if (win) GREEN else RED
  }
}

enum class Emote(val value: String) {
  POG("<:pog:853266160232431646>"),
  SADGE("<:sadge:934050600696573952>");

  companion object {

    fun fromWin(win: Boolean): Emote = if (win) POG else SADGE
  }

  private val emoteRegex = "<:.+?:\\d+?>".toRegex()

  init {
    require(emoteRegex.matches(value)) { "Emote does not match format." }
  }
}

private fun Match.formatTitle(): String {
  val participantNames = formatParticipantNames()
  val pronouns = formatPronoun()
  val win = formatWin()
  val emoteValue = Emote.fromWin(this.win).value

  return "$participantNames played a match, and $pronouns $win $emoteValue"
}

fun Match.createLeagueOfGraphsLink(): String {
  return "https://www.leagueofgraphs.com/de/match/euw/${matchId.value}"
}

@Component
private class DiscordMatchReporter(
  private val properties: RankedWatcherDiscordProperties,
) : MatchReporter {

  private val webhookClient = WebhookClient.withUrl(properties.webhookUrl)

  override fun reportMatch(match: Match) {
    val titleText = match.formatTitle()
    val body = match.formatParticipants()

    val leagueOfGraphsLink = match.createLeagueOfGraphsLink()
    val embedTitle = WebhookEmbed.EmbedTitle(titleText, leagueOfGraphsLink)
    val embed = WebhookEmbedBuilder()
      .setColor(Color.fromWin(match.win).value)
      .setTitle(embedTitle)
      .setDescription(body)
      .build()

    val message = WebhookMessageBuilder()
      .setUsername("Ranked Watcher")
      .setAvatarUrl(properties.avatarUrl)
      .setContent(titleText)
      .addEmbeds(embed)
      .build()

    webhookClient.send(message)
  }
}

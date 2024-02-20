package de.lausi.gsvdiscordtools.rankedwatcher.domain.model.match

import de.lausi.gsvdiscordtools.rankedwatcher.domain.model.player.SummonerId
import de.lausi.gsvdiscordtools.rankedwatcher.domain.model.player.SummonerName

data class MatchId(val value: String)

enum class MatchType {
  SOLOQ,
  FLEX,
  UNKNOWN
}

data class Champion(val name: String)

enum class Position(val formatted: String) {
  TOP("Top"),
  JUNGLE("Jungle"),
  MID("Mid"),
  BOTTON("Bottom"),
  SUPPORT("Support"),
  UNKNOWN("Unknown");
}

data class Participant(
  val summonerId: SummonerId,
  val summonerName: SummonerName,
  val champion: Champion,
  val position: Position,
  val kills: Int,
  val deaths: Int,
  val assists: Int,
  val pentaKill: Boolean,
  val quadraKill: Boolean
) {

  fun format(): String {
    return """
      **${summonerName.value}**
      Champion: ${champion.name}
      Position: ${position.formatted}
      KDA: $kills/$deaths/$assists
    """.trimIndent()
  }
}

data class Match(
  val matchId: MatchId,
  val win: Boolean,
  val participants: List<Participant>,
  val type: MatchType,
) {

  fun isRankedMatch() : Boolean = type != MatchType.UNKNOWN

  fun formatParticipantNames(): String {
    return participants.map { it.summonerName }.formatParticipantNames()
  }

  fun formatParticipants(): String {
    return participants.joinToString("\n\n") { it.format() }
  }

  fun formatWin(): String {
    return if (win) "won" else "lost"
  }

  fun formatPronoun() : String {
    return if (participants.size > 1) "they" else "he/she"
  }
}

fun List<SummonerName>.formatParticipantNames(): String {
  if (isEmpty())
    return ""

  val participantNamesAsString = map { it.value }

  return listOf(participantNamesAsString.dropLast(1).joinToString(", "), participantNamesAsString.last())
    .filter { it.isNotBlank() }
    .joinToString(" and ")
}

package de.lausi.gsvdiscordtools.rankedwatcher.domain.model.match

interface MatchTracker {

  fun isTracked(matchId: MatchId): Boolean

  fun markTracked(matchId: MatchId)
}

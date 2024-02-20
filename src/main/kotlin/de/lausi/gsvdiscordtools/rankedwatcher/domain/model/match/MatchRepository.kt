package de.lausi.gsvdiscordtools.rankedwatcher.domain.model.match

import de.lausi.gsvdiscordtools.rankedwatcher.domain.model.player.SummonerId

interface MatchRepository {

  fun getLatestMatchIdsBySummonerId(summonerId: SummonerId, count: Int = 1): List<MatchId>

  fun getMatch(matchId: MatchId, summonerIds: List<SummonerId>): Match
}

package de.lausi.gsvdiscordtools.rankedwatcher.domain.model.match

interface MatchReporter {

  fun reportMatch(match: Match)
}

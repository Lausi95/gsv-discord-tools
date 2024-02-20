package de.lausi.gsvdiscordtools.rankedwatcher.adapter.mongo

import de.lausi.gsvdiscordtools.rankedwatcher.domain.model.match.MatchId
import de.lausi.gsvdiscordtools.rankedwatcher.domain.model.match.MatchTracker
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component

@Document("tracked-match")
private data class TrackedMatchEntity(
  @Id val id: String
)

private interface TrackedMatchEntityRepository: MongoRepository<TrackedMatchEntity, String> {
}

@Component
private class MongoMatchTracker(private val trackedMatchEntityRepository: TrackedMatchEntityRepository): MatchTracker {

  override fun isTracked(matchId: MatchId): Boolean {
    return trackedMatchEntityRepository.existsById(matchId.value)
  }

  override fun markTracked(matchId: MatchId) {
    trackedMatchEntityRepository.save(TrackedMatchEntity(matchId.value))
  }
}

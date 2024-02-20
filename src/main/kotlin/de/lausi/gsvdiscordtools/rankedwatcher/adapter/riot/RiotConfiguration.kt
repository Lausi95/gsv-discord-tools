package de.lausi.gsvdiscordtools.rankedwatcher.adapter.riot

import de.lausi.gsvdiscordtools.rankedwatcher.domain.model.match.MatchRepository
import de.lausi.gsvdiscordtools.rankedwatcher.domain.model.player.PlayerResolver
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@ConfigurationProperties("riot")
private data class RiotProperties(
  val region: String,
  val apiKey: String,
)

@Configuration
@EnableConfigurationProperties(RiotProperties::class)
private class RiotConfiguration {

  @Bean
  fun playerResolver(riotRestTemplate: RestTemplate): PlayerResolver {
    return RiotPlayerResolver(riotRestTemplate)
  }

  @Bean
  fun matchRepository(riotRestTemplate: RestTemplate): MatchRepository {
    return RiotMatchRepsitory(riotRestTemplate)
  }

  @Bean
  fun riotRestTemplate(riotProperties: RiotProperties): RestTemplate {
    return RestTemplateBuilder()
      .rootUri("https://${riotProperties.region}.api.riotgames.com")
      .defaultHeader("X-Riot-Token", riotProperties.apiKey)
      .build()
  }
}

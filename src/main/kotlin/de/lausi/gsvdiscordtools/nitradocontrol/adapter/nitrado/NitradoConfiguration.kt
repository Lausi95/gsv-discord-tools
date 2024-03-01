package de.lausi.gsvdiscordtools.nitradocontrol.adapter.nitrado

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.web.client.RestTemplate

@ConfigurationProperties(prefix = "nitrado")
private data class NitradoProperties(
  val apiHost: String,
  val accessToken: String,
)

@Configuration
@EnableConfigurationProperties(NitradoProperties::class)
private class NitradoConfiguration {

  @Bean
  fun nitradoAdapter(nitradoRestTemplate: RestTemplate): NitradoAdapter {
    return NitradoAdapter(nitradoRestTemplate)
  }

  @Bean
  fun nitradoRestTemplate(nitradoProperties: NitradoProperties): RestTemplate {
    return RestTemplateBuilder()
      .rootUri(nitradoProperties.apiHost)
      .additionalInterceptors(bearerTokenInterceptor(nitradoProperties.accessToken))
      .build()
  }

  private fun bearerTokenInterceptor(bearerToken: String): ClientHttpRequestInterceptor {
    return ClientHttpRequestInterceptor { request, body, execution ->
      request.headers.add("Authorization", "Bearer $bearerToken")
      execution.execute(request, body)
    }
  }
}

package com.scoring.github.popularity_service.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties(WebClientBufferProperties.class)
public class GithubWebClientConfig {

    @Bean
    public WebClient githubWebClient(WebClientBufferProperties props) {

        DataSize size = props.getSize();

        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer ->
                        configurer.defaultCodecs().maxInMemorySize((int) size.toBytes())
                )
                .build();

        return WebClient.builder()
                .baseUrl("https://api.github.com")
                .exchangeStrategies(strategies)
                .defaultHeader("Accept", "application/vnd.github+json")
                .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
                .build();
    }
}

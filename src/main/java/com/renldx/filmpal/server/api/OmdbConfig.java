package com.renldx.filmpal.server.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class OmdbConfig {

    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        return builder.baseUrl("http://www.omdbapi.com").build();
    }

}

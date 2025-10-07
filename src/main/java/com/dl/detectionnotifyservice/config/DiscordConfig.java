package com.dl.detectionnotifyservice.config;

import com.dl.detectionnotifyservice.properties.DiscordProperties;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DiscordConfig {

    private final DiscordProperties discordProperties;

    @Bean
    public GatewayDiscordClient gatewayDiscordClient() {
        return DiscordClientBuilder.create(discordProperties.getToken())
                .build()
                .login()
                .block();
    }

    @Bean
    public Snowflake gatewayDiscordChannel() {
        return Snowflake.of(discordProperties.getChannelId());
    }

}

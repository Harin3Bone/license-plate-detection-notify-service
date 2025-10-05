package com.dl.detectionnotifyservice.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "discord")
public class DiscordProperties {

    private String channelId;
    private String token;

}

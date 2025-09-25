package com.dl.detectionnotifyservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

@Slf4j
@Configuration
@EnableConfigurationProperties
@ConfigurationPropertiesScan(basePackages = "com.dl.detectionnotifyservice.properties")
public class AppConfig {

    @Value("${server.timezone}")
    private String timezone;

    @Bean
    public Clock systemClock() {
        log.info("Initialize system timezone: {}", timezone);
        return Clock.system(ZoneId.of(this.timezone));
    }
}

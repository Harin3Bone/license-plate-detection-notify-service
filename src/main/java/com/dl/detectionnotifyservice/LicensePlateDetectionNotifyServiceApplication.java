package com.dl.detectionnotifyservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
@EnableConfigurationProperties
@ConfigurationPropertiesScan(basePackages = "com.dl.detectionnotifyservice.properties")
public class LicensePlateDetectionNotifyServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LicensePlateDetectionNotifyServiceApplication.class, args);
	}

}

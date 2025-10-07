package com.dl.detectionnotifyservice.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.minio")
public class MinioProperties {

    private String url;
    private String accessKey;
    private String secretKey;
    private String bucket;

}

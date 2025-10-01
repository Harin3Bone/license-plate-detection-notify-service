package com.dl.detectionnotifyservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String NOTIFY_QUEUE = "notify-queue";

    @Bean("notifyQueue")
    public Queue notifyQueue() {
        return new Queue(NOTIFY_QUEUE, true);
    }

    @Bean
    public MessageConverter jsonToMapMessageConverter() {
        DefaultClassMapper defaultClassMapper = new DefaultClassMapper();
        defaultClassMapper.setTrustedPackages("com.dl.detectionnotifyservice.model.payload"); // trusted packages
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        jackson2JsonMessageConverter.setClassMapper(defaultClassMapper);
        return jackson2JsonMessageConverter;
    }

}

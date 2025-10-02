package com.dl.detectionnotifyservice.handler;

import com.dl.detectionnotifyservice.model.payload.NotifyPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.dl.detectionnotifyservice.config.RabbitMQConfig.MEDIA_QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class UploadHandler implements BaseHandler<NotifyPayload> {

    @RabbitHandler
    @RabbitListener(queues = MEDIA_QUEUE)
    public void consume(NotifyPayload payload) {

    }
}

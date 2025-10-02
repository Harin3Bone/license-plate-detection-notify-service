package com.dl.detectionnotifyservice.handler;

import com.dl.detectionnotifyservice.constant.Status;
import com.dl.detectionnotifyservice.entity.NotifyHistory;
import com.dl.detectionnotifyservice.model.payload.NotifyPayload;
import com.dl.detectionnotifyservice.service.NotifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.dl.detectionnotifyservice.config.RabbitMQConfig.NOTIFY_QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotifyHandler implements  BaseHandler<NotifyPayload> {

    private final NotifyService notifyService;

    @RabbitHandler
    @RabbitListener(queues = NOTIFY_QUEUE)
    public void consume(NotifyPayload payload) {
        log.info("Starting notify message.");
        log.debug("Save notify history to database.");
        NotifyHistory history = notifyService.saveNotifyHistory(payload);

        log.debug("Begin push notification to server.");
        Status notifyStatus = notifyService.pushNotification(payload.getNotifyMessage());

        log.debug("Update notify history status to {}.", notifyStatus);
        notifyService.updateNotifyHistoryStatus(history, notifyStatus);

        log.info("Notify message complete.");
    }

}

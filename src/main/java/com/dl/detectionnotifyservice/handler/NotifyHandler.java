package com.dl.detectionnotifyservice.handler;

import com.dl.detectionnotifyservice.constant.Status;
import com.dl.detectionnotifyservice.entity.MediaEvidence;
import com.dl.detectionnotifyservice.entity.NotifyHistory;
import com.dl.detectionnotifyservice.model.payload.NotifyPayload;
import com.dl.detectionnotifyservice.service.NotifyService;
import com.dl.detectionnotifyservice.service.UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

import static com.dl.detectionnotifyservice.config.RabbitMQConfig.NOTIFY_QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotifyHandler implements  BaseHandler<NotifyPayload> {

    private final NotifyService notifyService;
    private final UploadService uploadService;

    @RabbitHandler
    @RabbitListener(queues = NOTIFY_QUEUE)
    public void consume(NotifyPayload payload) {
        log.info("Starting notify message.");
        log.debug("Save notify history to database.");
        NotifyHistory history = notifyService.saveNotifyHistory(payload);

        log.debug("Retrieve notify history: {}", history);
        List<MediaEvidence> evidences = uploadService.getMediaEvidences(payload.getUploadId());

        // Limitation send only 1 file due to Discord API limit
        File delegateFile = uploadService.getFileFromMinIO(evidences.getFirst().getFilePath());

        log.debug("Begin push notification to server.");
        Status notifyStatus = notifyService.pushNotification(history.getNotifyMessage(), delegateFile);

        log.debug("Update notify history status to {}.", notifyStatus);
        notifyService.updateNotifyHistoryStatus(history, notifyStatus);

        log.info("Notify message complete.");
    }

}

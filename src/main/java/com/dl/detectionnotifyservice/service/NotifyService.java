package com.dl.detectionnotifyservice.service;

import com.dl.detectionnotifyservice.entity.Status;
import com.dl.detectionnotifyservice.model.NotifyResponse;
import org.springframework.stereotype.Service;

@Service
public class NotifyService {

    public NotifyResponse sendNotification() {
        return new NotifyResponse(
                Status.PENDING.name(),
                "Notification sent in-progress",
                "Discord"
        );
    }

}

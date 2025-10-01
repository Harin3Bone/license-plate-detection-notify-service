package com.dl.detectionnotifyservice.service;

import com.dl.detectionnotifyservice.constant.Status;
import com.dl.detectionnotifyservice.constant.VehicleType;
import com.dl.detectionnotifyservice.model.payload.NotifyPayload;
import com.dl.detectionnotifyservice.model.rest.NotifyRequest;
import com.dl.detectionnotifyservice.model.rest.NotifyResponse;
import com.dl.detectionnotifyservice.util.DateFormatUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotifyService {

    private static final String NOTIFY_MSG_TEMPLATE = "License Plate %s has been detected illegally parked at %s";

    private final RabbitTemplate rabbitTemplate;
    private final Queue notifyQueue;
    private final Clock systemClock;

    public NotifyResponse sendNotification(NotifyRequest request) {
        // Build message payload
        NotifyPayload payload = buildPayload(request);

        // Send message to RabbitMQ
        rabbitTemplate.convertAndSend(notifyQueue.getName(), payload);

        // Populate API response
        return new NotifyResponse(payload.getNotifyId(), payload.getStatus(), payload.getNotifyMessage());
    }

    private NotifyPayload buildPayload(NotifyRequest request) {
        ZonedDateTime currentDateTime = ZonedDateTime.now(systemClock);

        NotifyPayload payload = new NotifyPayload();
        payload.setNotifyId(UUID.randomUUID());
        payload.setLicensePlate(request.licensePlate());
        payload.setImageId(request.uploadId());
        payload.setRemark(request.remark());
        payload.setVehicleType(ObjectUtils.isEmpty(request.vehicleType()) ? VehicleType.CAR.name() : VehicleType.fromString(request.vehicleType()).name());
        payload.setStatus(Status.PENDING.name());
        payload.setNotifyMessage(String.format(NOTIFY_MSG_TEMPLATE, request.licensePlate(), DateFormatUtil.zonedDateTimeToString(currentDateTime, systemClock.getZone())));
        payload.setCurrentDateTime(currentDateTime);

        return payload;
    }
}

package com.dl.detectionnotifyservice.service;

import com.dl.detectionnotifyservice.constant.Status;
import com.dl.detectionnotifyservice.constant.VehicleType;
import com.dl.detectionnotifyservice.entity.NotifyHistory;
import com.dl.detectionnotifyservice.model.payload.NotifyPayload;
import com.dl.detectionnotifyservice.model.rest.NotifyRequest;
import com.dl.detectionnotifyservice.model.rest.NotifyResponse;
import com.dl.detectionnotifyservice.repository.NotifyHistoryRepository;
import com.dl.detectionnotifyservice.util.DateFormatUtil;
import jakarta.transaction.Transactional;
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

    private static final String NOTIFY_MSG_TEMPLATE = "License Plate \"%s\" has been detected illegally parked at %s";

    private final RabbitTemplate rabbitTemplate;
    private final Queue notifyQueue;

    private final NotifyHistoryRepository notifyHistoryRepository;
    private final Clock systemClock;

    public NotifyResponse publishNotifyPayload(NotifyRequest request) {
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
        payload.setUploadId(UUID.fromString(request.uploadId()));
        payload.setRemark(request.remark());
        payload.setVehicleType(ObjectUtils.isEmpty(request.vehicleType()) ? VehicleType.CAR.name() : VehicleType.fromString(request.vehicleType()).name());
        payload.setStatus(Status.PENDING.name());
        payload.setNotifyMessage(String.format(NOTIFY_MSG_TEMPLATE, request.licensePlate(), DateFormatUtil.zonedDateTimeToString(currentDateTime, systemClock.getZone())));
        payload.setCurrentDateTime(currentDateTime);

        return payload;
    }

    @Transactional
    public NotifyHistory saveNotifyHistory(NotifyPayload payload) {
        NotifyHistory history = buildNotifyHistory(payload);
        notifyHistoryRepository.save(history);

        return history;
    }

    @Transactional
    public void updateNotifyHistoryStatus(NotifyHistory history, Status status) {
        // Update status and last updated timestamp
        history.setStatus(status.name());
        history.setLastUpdatedTimestamp(ZonedDateTime.now(systemClock).withZoneSameInstant(systemClock.getZone()));

        notifyHistoryRepository.save(history);
    }

    private NotifyHistory buildNotifyHistory(NotifyPayload payload) {
        NotifyHistory history = new NotifyHistory();
        history.setHistoryId(payload.getNotifyId());
        history.setLicensePlate(payload.getLicensePlate());
        history.setNotifyMessage(payload.getNotifyMessage());
        history.setUploadId(payload.getUploadId());
        history.setRemark(payload.getRemark());
        history.setStatus(payload.getStatus());
        history.setVehicleType(payload.getVehicleType());
        history.setCreateTimestamp(payload.getCurrentDateTime());
        history.setLastUpdatedTimestamp(payload.getCurrentDateTime());

        return history;
    }

    public Status pushNotification(String message) {
        Status status;
        try {
            status = Status.SUCCESS;
        } catch (Exception e) {
            status = Status.FAILURE;
        }

        return status;
    }
}

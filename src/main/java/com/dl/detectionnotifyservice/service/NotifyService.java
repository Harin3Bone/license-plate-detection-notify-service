package com.dl.detectionnotifyservice.service;

import com.dl.detectionnotifyservice.constant.Status;
import com.dl.detectionnotifyservice.constant.VehicleType;
import com.dl.detectionnotifyservice.entity.NotifyHistory;
import com.dl.detectionnotifyservice.model.payload.NotifyPayload;
import com.dl.detectionnotifyservice.model.rest.NotifyRequest;
import com.dl.detectionnotifyservice.model.rest.NotifyResponse;
import com.dl.detectionnotifyservice.repository.NotifyHistoryRepository;
import com.dl.detectionnotifyservice.util.DateFormatUtil;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.Channel;
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

    private static final String NOTIFY_MSG_TEMPLATE_TH = "Vehicle license plate \"%s\" has been detected illegally parked at %s";
    private static final String NOTIFY_MSG_TEMPLATE_EN = "พาหนะป้ายทะเบียน \"%s\" ถูกตรวจพบว่ามีการจอดในพื้นที่ผิดกฏหมาย ณ เวลา %s";

    private final Queue notifyQueue;
    private final RabbitTemplate rabbitTemplate;
    private final GatewayDiscordClient discordClient;
    private final Snowflake gatewayDiscordChannel;

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
        String notifyMessage = ObjectUtils.isEmpty(request.language()) || request.language().equalsIgnoreCase("th")
                ? String.format(NOTIFY_MSG_TEMPLATE_TH, request.licensePlate(), DateFormatUtil.zonedDateTimeToString(currentDateTime, systemClock.getZone()))
                : String.format(NOTIFY_MSG_TEMPLATE_EN, request.licensePlate(), DateFormatUtil.zonedDateTimeToString(currentDateTime, systemClock.getZone()));

        NotifyPayload payload = new NotifyPayload();
        payload.setNotifyId(UUID.randomUUID());
        payload.setLicensePlate(request.licensePlate());
        payload.setUploadId(UUID.fromString(request.uploadId()));
        payload.setRemark(request.remark());
        payload.setVehicleType(ObjectUtils.isEmpty(request.vehicleType()) ? VehicleType.CAR.name() : VehicleType.fromString(request.vehicleType()).name());
        payload.setStatus(Status.PENDING.name());
        payload.setNotifyMessage(notifyMessage);
        payload.setCurrentDateTime(currentDateTime);

        return payload;
    }

    @Transactional
    public NotifyHistory saveNotifyHistory(NotifyPayload payload) {
        NotifyHistory history = mapToEntity(payload);
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

    private NotifyHistory mapToEntity(NotifyPayload payload) {
        NotifyHistory entity = new NotifyHistory();
        entity.setHistoryId(payload.getNotifyId());
        entity.setLicensePlate(payload.getLicensePlate());
        entity.setNotifyMessage(payload.getNotifyMessage());
        entity.setUploadId(payload.getUploadId());
        entity.setRemark(payload.getRemark());
        entity.setStatus(payload.getStatus());
        entity.setVehicleType(payload.getVehicleType());
        entity.setCreateTimestamp(payload.getCurrentDateTime());
        entity.setLastUpdatedTimestamp(payload.getCurrentDateTime());

        return entity;
    }

    public Status pushNotification(String message) {
        Status status;
        try {
            Channel channel = discordClient.getChannelById(gatewayDiscordChannel).block();
            if (channel != null) {
                channel.getRestChannel().createMessage(message).block();
            }

            status = Status.SUCCESS;
        } catch (Exception e) {
            log.error("Failed to push notification to Discord: {}", e.getMessage(), e);
            status = Status.FAILURE;
        }

        return status;
    }
}

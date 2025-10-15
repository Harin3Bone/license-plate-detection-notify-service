package com.dl.detectionnotifyservice.service;

import com.dl.detectionnotifyservice.constant.Status;
import com.dl.detectionnotifyservice.constant.VehicleType;
import com.dl.detectionnotifyservice.entity.Camera;
import com.dl.detectionnotifyservice.entity.NotifyHistory;
import com.dl.detectionnotifyservice.exception.InvalidException;
import com.dl.detectionnotifyservice.model.payload.NotifyPayload;
import com.dl.detectionnotifyservice.model.rest.NotifyRequest;
import com.dl.detectionnotifyservice.model.rest.NotifyResponse;
import com.dl.detectionnotifyservice.repository.CameraRepository;
import com.dl.detectionnotifyservice.repository.NotifyHistoryRepository;
import com.dl.detectionnotifyservice.util.DateFormatUtil;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.entity.channel.TextChannel;
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

    private static final String NOTIFY_MSG_TEMPLATE_TH = "พาหนะป้ายทะเบียน '%s' ถูกตรวจพบว่ามีการจอดในพื้นที่ผิดกฏหมาย ณ สถานที่ '%s' เวลา %s";
    private static final String ADDRESS_PLACEHOLDER = "%s %s %s %s";

    private final Queue notifyQueue;
    private final RabbitTemplate rabbitTemplate;
    private final GatewayDiscordClient discordClient;
    private final Snowflake gatewayDiscordChannel;

    private final NotifyHistoryRepository notifyHistoryRepository;
    private final CameraRepository cameraRepository;
    private final Clock systemClock;

    public void verifyRequest(NotifyRequest request) {
        // Validate required fields
        if (ObjectUtils.isEmpty(request.licensePlate())) {
            log.error("License plate is required.");
            throw new InvalidException("License plate is required.");
        }

        if (ObjectUtils.isEmpty(request.cameraId())) {
            log.error("Camera ID is required.");
            throw new InvalidException("Camera ID is required.");
        }

        // Verify camera ID exists
        if (!cameraRepository.existsById(UUID.fromString(request.cameraId()))) {
            log.error("Camera ID not found: {}", request.cameraId());
            throw new InvalidException("Camera ID not found: " + request.cameraId());
        }
    }

    public NotifyResponse publishNotifyPayload(NotifyRequest request) {
        // Build message payload
        NotifyPayload payload = buildPayload(request);

        // Send message to RabbitMQ
        rabbitTemplate.convertAndSend(notifyQueue.getName(), payload);

        // Populate API response
        return new NotifyResponse(payload.getNotifyId(), payload.getStatus());
    }

    private NotifyPayload buildPayload(NotifyRequest request) {
        ZonedDateTime currentDateTime = ZonedDateTime.now(systemClock);

        NotifyPayload payload = new NotifyPayload();
        payload.setNotifyId(UUID.randomUUID());
        payload.setLicensePlate(request.licensePlate());
        payload.setUploadId(ObjectUtils.isEmpty(request.uploadId()) ? null : UUID.fromString(request.uploadId()));
        payload.setCameraId(UUID.fromString(request.cameraId()));
        payload.setRemark(request.remark());
        payload.setVehicleType(ObjectUtils.isEmpty(request.vehicleType()) ? VehicleType.CAR.name() : VehicleType.fromString(request.vehicleType()).name());
        payload.setStatus(Status.PENDING.name());
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
        entity.setNotifyMessage(buildNotifyMessage(payload));
        entity.setUploadId(payload.getUploadId());
        entity.setRemark(payload.getRemark());
        entity.setStatus(payload.getStatus());
        entity.setVehicleType(payload.getVehicleType());
        entity.setCreateTimestamp(payload.getCurrentDateTime());
        entity.setLastUpdatedTimestamp(payload.getCurrentDateTime());

        return entity;
    }

    private String buildNotifyMessage(NotifyPayload payload) {
        // Format address from camera details
        Camera camera = cameraRepository.findById(payload.getCameraId())
                .orElseThrow(() -> new InvalidException("Camera ID not found: " + payload.getCameraId()));
        String address = String.format(ADDRESS_PLACEHOLDER, camera.getAddress(), camera.getSubDistrict(), camera.getDistrict(), camera.getProvince());

        // Clean up license plate string
        String formatLicensePlate = payload.getLicensePlate()
                .replaceAll("[\\[\\],']", "")
                .trim();

        // Format notification time
        String notifyTime = DateFormatUtil.zonedDateTimeToString(payload.getCurrentDateTime(), systemClock.getZone());

        return String.format(NOTIFY_MSG_TEMPLATE_TH, formatLicensePlate, address, notifyTime);
    }


    public Status pushNotification(String message) {
        Status status;
        try {
            Channel channel = discordClient.getChannelById(gatewayDiscordChannel).block();
            if (channel instanceof TextChannel textChannel) {
                textChannel.createMessage(message).block();
            } else {
                log.error("The specified channel is not a text channel. Cannot send message.");
                return Status.FAILURE;
            }

            status = Status.SUCCESS;
        } catch (Exception e) {
            log.error("Failed to push notification to Discord: {}", e.getMessage(), e);
            status = Status.FAILURE;
        }

        return status;
    }
}

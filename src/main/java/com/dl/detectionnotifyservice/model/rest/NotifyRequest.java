package com.dl.detectionnotifyservice.model.rest;

public record NotifyRequest(
        String licensePlate,
        String uploadId,
        String cameraId,
        String remark,
        String vehicleType,
        String language
) {
}
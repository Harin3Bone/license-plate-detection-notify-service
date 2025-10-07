package com.dl.detectionnotifyservice.model.rest;

public record NotifyRequest(
        String licensePlate,
        String uploadId,
        String remark,
        String vehicleType,
        String language
) {
}
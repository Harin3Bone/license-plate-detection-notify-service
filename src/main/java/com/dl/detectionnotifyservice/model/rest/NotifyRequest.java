package com.dl.detectionnotifyservice.model.rest;

public record NotifyRequest(
        String licensePlate,
        String imageId,
        String remark,
        String vehicleType
) {
}
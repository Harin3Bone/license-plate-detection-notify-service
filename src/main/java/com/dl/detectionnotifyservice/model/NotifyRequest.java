package com.dl.detectionnotifyservice.model;

public record NotifyRequest(
        String licensePlate,
        String imageId,
        String remark,
        String vehicleType
) {

}
package com.dl.detectionnotifyservice.model.rest;

import com.dl.detectionnotifyservice.constant.CameraStatus;
import com.dl.detectionnotifyservice.entity.Camera;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

public record CameraResponse(
        UUID cameraId,
        String cameraName,
        CameraStatus status,
        String remark,
        String province,
        String district,
        String subDistrict,
        String address,
        BigDecimal latitude,
        BigDecimal longitude,
        ZonedDateTime createTimestamp,
        UUID createdBy,
        ZonedDateTime lastUpdatedTimestamp,
        UUID lastUpdatedBy
) {
    public CameraResponse(Camera camera) {
        this(
                camera.getCameraId(),
                camera.getCameraName(),
                CameraStatus.valueOf(camera.getStatus()),
                camera.getRemark(),
                camera.getProvince(),
                camera.getDistrict(),
                camera.getSubDistrict(),
                camera.getAddress(),
                camera.getLatitude(),
                camera.getLongitude(),
                camera.getCreateTimestamp(),
                camera.getCreatedBy(),
                camera.getLastUpdatedTimestamp(),
                camera.getLastUpdatedBy()
        );
    }
}

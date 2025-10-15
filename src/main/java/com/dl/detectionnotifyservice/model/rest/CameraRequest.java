package com.dl.detectionnotifyservice.model.rest;

public record CameraRequest(
        String name,
        String status,
        String remark,
        String province,
        String district,
        String subDistrict,
        String address,
        Double latitude,
        Double longitude,
        String modifiedBy
) {}

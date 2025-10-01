package com.dl.detectionnotifyservice.model.rest;

public record UploadResponse(
        String uploadId,
        String path,
        String status
) {
}

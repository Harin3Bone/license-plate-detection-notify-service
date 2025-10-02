package com.dl.detectionnotifyservice.model.rest;

import java.util.UUID;

public record UploadResponse(
        UUID uploadId,
        UUID fileId,
        String contentType,
        String status
) {
}

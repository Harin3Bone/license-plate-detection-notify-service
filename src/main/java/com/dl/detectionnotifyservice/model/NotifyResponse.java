package com.dl.detectionnotifyservice.model;

public record NotifyResponse(
        String status,
        String message,
        String server
) {
}

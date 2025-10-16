package com.dl.detectionnotifyservice.model.rest;

import java.util.UUID;

public record NotifyResponse(
        UUID notifyId,
        String status
) {
}

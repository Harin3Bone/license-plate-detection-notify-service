package com.dl.detectionnotifyservice.model.rest;

public record ExceptionResponse(
        int statusCode,
        String message
) {
}

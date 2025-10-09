package com.dl.detectionnotifyservice.controller;

import com.dl.detectionnotifyservice.exception.InvalidException;
import com.dl.detectionnotifyservice.exception.MediaUploadException;
import com.dl.detectionnotifyservice.model.rest.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleAllExceptions(Exception ex) {
        return new ExceptionResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred: " + ex.getMessage()
        );
    }

    @ExceptionHandler(value = {InvalidException.class, MediaUploadException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleInvalidException(RuntimeException ex) {
        return new ExceptionResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
    }

}

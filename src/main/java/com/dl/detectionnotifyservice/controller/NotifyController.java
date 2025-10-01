package com.dl.detectionnotifyservice.controller;

import com.dl.detectionnotifyservice.model.rest.NotifyRequest;
import com.dl.detectionnotifyservice.model.rest.NotifyResponse;
import com.dl.detectionnotifyservice.service.NotifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/notify")
public class NotifyController {

    private final NotifyService notifyService;

    @PostMapping("/v1/send")
    public NotifyResponse sendNotify(@RequestBody NotifyRequest request) {
        log.info("Received request to send notification");
        return notifyService.sendNotification(request);
    }
}

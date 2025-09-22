package com.dl.detectionnotifyservice.controller;

import com.dl.detectionnotifyservice.model.NotifyResponse;
import com.dl.detectionnotifyservice.service.NotifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/notify")
public class NotifyController {

    private final NotifyService notifyService;

    @PostMapping("/v1/send")
    public NotifyResponse sendNotify() {
        return notifyService.sendNotification();
    }
}

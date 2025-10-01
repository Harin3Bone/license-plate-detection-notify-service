package com.dl.detectionnotifyservice.controller;

import com.dl.detectionnotifyservice.model.rest.UploadResponse;
import com.dl.detectionnotifyservice.service.UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/media")
public class UploadController {

    private final UploadService uploadService;

    @PostMapping(value = "/v1/upload/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadResponse uploadImage(
            @RequestPart(required = false) String directoryId,
            @RequestPart MultipartFile image
    ) {
        return uploadService.uploadFile(directoryId, image);
    }
}

package com.dl.detectionnotifyservice.controller;

import com.dl.detectionnotifyservice.constant.CameraStatus;
import com.dl.detectionnotifyservice.entity.Camera;
import com.dl.detectionnotifyservice.model.rest.CameraRequest;
import com.dl.detectionnotifyservice.model.rest.CameraResponse;
import com.dl.detectionnotifyservice.service.CameraService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/camera")
public class CameraController {

    private final CameraService cameraService;

    @GetMapping("/v1/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CameraResponse getById(
            @PathVariable("id") String cameraId,
            @RequestParam(value = "status", required = false) String status
    ) {
        log.info("Received request to get camera by id: {}", cameraId);
        Camera camera;
        if (ObjectUtils.isEmpty(status)) {
            camera = cameraService.findById(cameraId);
        } else {
            CameraStatus cameraStatus = CameraStatus.valueOf(status.toUpperCase());
            camera = cameraService.findByIdAndStatus(cameraId, cameraStatus);
        }

        return new CameraResponse(camera);
    }

    @PostMapping("/v1/register")
    @ResponseStatus(HttpStatus.CREATED)
    public CameraResponse register(@RequestBody CameraRequest cameraRequest) {
        log.info("Received request to register camera");
        Camera camera = cameraService.save(cameraRequest);

        return new CameraResponse(camera);
    }

    @PutMapping("/v1/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CameraResponse update(@PathVariable("id") String cameraId, @RequestBody CameraRequest cameraRequest) {
        log.info("Received request to update camera by id: {}", cameraId);
        Camera camera = cameraService.update(cameraId, cameraRequest);

        return new CameraResponse(camera);
    }

    @DeleteMapping("/v1/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String cameraId) {
        log.info("Received request to delete camera by id: {}", cameraId);
        cameraService.softDelete(cameraId);
    }

    @DeleteMapping("/v1/hard/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void hardDelete(@PathVariable("id") String cameraId) {
        log.info("Received request to hard delete camera by id: {}", cameraId);
        cameraService.hardDelete(cameraId);
    }

}

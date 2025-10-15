package com.dl.detectionnotifyservice.service;

import com.dl.detectionnotifyservice.constant.CameraStatus;
import com.dl.detectionnotifyservice.entity.Camera;
import com.dl.detectionnotifyservice.exception.NotFoundException;
import com.dl.detectionnotifyservice.model.rest.CameraRequest;
import com.dl.detectionnotifyservice.repository.CameraRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CameraService {

    private final CameraRepository cameraRepository;
    private final Clock systemClock;

    public Camera findById(String cameraId) {
        return cameraRepository.findById(UUID.fromString(cameraId))
                .orElseThrow(() -> {
                    log.error("Camera not found by id: {}", cameraId);
                    return new NotFoundException("Camera not found by id: " + cameraId);
                });
    }

    public Camera findByIdAndStatus(String cameraId, CameraStatus status) {
        return cameraRepository.findByCameraIdAndStatus(UUID.fromString(cameraId), status.name())
                .orElseThrow(() -> {
                    log.error("Camera not found by id: {} and status: {}", cameraId, status);
                    return new NotFoundException("Camera not found by id: " + cameraId + " and status: " + status);
                });
    }

    @Transactional
    public Camera save(CameraRequest cameraRequest) {
        Camera camera = mapToEntity(null, cameraRequest);
        return cameraRepository.save(camera);
    }

    @Transactional
    public Camera update(String cameraId, CameraRequest cameraRequest) {
        Camera existingCamera = findById(cameraId);
        existingCamera = mapToEntity(existingCamera, cameraRequest);

        return cameraRepository.save(existingCamera);
    }

    @Transactional
    public void softDelete(String cameraId) {
        Camera existingCamera = findById(cameraId);
        existingCamera.setStatus(CameraStatus.DELETED.name());
        cameraRepository.save(existingCamera);
    }

    @Transactional
    public void hardDelete(String cameraId) {
        Camera existingCamera = findById(cameraId);
        cameraRepository.delete(existingCamera);
    }

    private Camera mapToEntity(Camera existing, CameraRequest cameraRequest) {
        ZonedDateTime currentTime = ZonedDateTime.now(systemClock);
        boolean isNew = existing == null;

        Camera camera = existing == null ? new Camera() : existing;
        camera.setCameraId(isNew ? UUID.randomUUID() : existing.getCameraId());
        camera.setCameraName(cameraRequest.name());
        camera.setStatus(CameraStatus.valueOf(cameraRequest.status().toUpperCase()).name());
        camera.setRemark(cameraRequest.remark());
        camera.setProvince(cameraRequest.province());
        camera.setDistrict(cameraRequest.district());
        camera.setSubDistrict(cameraRequest.subDistrict());
        camera.setAddress(cameraRequest.address());
        camera.setLatitude(BigDecimal.valueOf(cameraRequest.latitude()));
        camera.setLongitude(BigDecimal.valueOf(cameraRequest.longitude()));
        camera.setCreateTimestamp(isNew ? currentTime : existing.getCreateTimestamp());
        camera.setLastUpdatedTimestamp(currentTime);

        // Temporary not verify user Id, because this service won't have web-ui for user yet
        UUID modifiedBy = UUID.fromString(cameraRequest.modifiedBy());
        camera.setCreatedBy(isNew ? modifiedBy : existing.getCreatedBy());
        camera.setLastUpdatedBy(modifiedBy);

        return camera;
    }

}

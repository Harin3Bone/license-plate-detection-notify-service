package com.dl.detectionnotifyservice.repository;

import com.dl.detectionnotifyservice.entity.Camera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CameraRepository extends JpaRepository<Camera, UUID> {

    Optional<Camera> findByCameraIdAndStatus(UUID id, String status);

}

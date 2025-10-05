package com.dl.detectionnotifyservice.repository;

import com.dl.detectionnotifyservice.entity.NotifyHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotifyHistoryRepository extends JpaRepository<NotifyHistory, UUID> {
}

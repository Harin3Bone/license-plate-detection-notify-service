package com.dl.detectionnotifyservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(schema = "notify_history")
public class NotifyHistory {

    @Id
    private UUID history_id;

    private String license_plate;

    private String notify_message;

    private String remark;

    private String status;

    private String province;

    private String vehicle_type;

    private String crated_timestamp;

    private String last_updated_timestamp;
}

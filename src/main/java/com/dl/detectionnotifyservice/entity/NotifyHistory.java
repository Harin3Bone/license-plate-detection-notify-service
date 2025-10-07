package com.dl.detectionnotifyservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "notify_history", schema = "public")
public class NotifyHistory {

    @Id
    @Column(name = "history_id", nullable = false)
    private UUID historyId;

    @Column(name = "license_plate")
    private String licensePlate;

    @Column(name = "notify_message")
    private String notifyMessage;

    @Column(name = "upload_id")
    private UUID uploadId;

    @Column(name = "remark")
    private String remark;

    @Column(name = "status")
    private String status;

    @Column(name = "vehicle_type")
    private String vehicleType;

    @Column(name = "created_timestamp")
    private ZonedDateTime createTimestamp;

    @Column(name = "last_updated_timestamp")
    private ZonedDateTime lastUpdatedTimestamp;
}

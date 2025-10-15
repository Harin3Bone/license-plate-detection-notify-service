package com.dl.detectionnotifyservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "camera", schema = "public")
public class Camera {

    @Id
    @Column(name = "camera_id", nullable = false)
    private UUID cameraId;

    @Column(name = "camera_name")
    private String cameraName;

    @Column(name = "status")
    private String status;

    @Column(name = "remark")
    private String remark;

    @Column(name = "province")
    private String province;

    @Column(name = "district")
    private String district;

    @Column(name = "sub_district")
    private String subDistrict;

    @Column(name = "address")
    private String address;

    @Column (name = "latitude")
    private BigDecimal latitude;

    @Column (name = "longitude")
    private BigDecimal longitude;

    @Column(name = "created_timestamp")
    private ZonedDateTime createTimestamp;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "last_updated_timestamp")
    private ZonedDateTime lastUpdatedTimestamp;

    @Column(name = "last_updated_by")
    private UUID lastUpdatedBy;
}

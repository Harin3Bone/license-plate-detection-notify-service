package com.dl.detectionnotifyservice.model.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotifyPayload implements Serializable {

    private UUID notifyId;
    private String licensePlate;
    private UUID uploadId;
    private UUID cameraId;
    private String remark;
    private String vehicleType;
    private String status;
    private String notifyMessage;
    private ZonedDateTime currentDateTime;

}

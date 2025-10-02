package com.dl.detectionnotifyservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class MediaEvidencePK {

    @Column(name = "upload_id")
    private UUID uploadId;

    @Column(name = "file_id")
    private UUID fileId;

}

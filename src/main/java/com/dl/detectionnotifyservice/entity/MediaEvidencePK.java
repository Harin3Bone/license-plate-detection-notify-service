package com.dl.detectionnotifyservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class MediaEvidencePK {

    @Column(name = "upload_id")
    private UUID uploadId;

    @Column(name = "file_id")
    private UUID fileId;

}

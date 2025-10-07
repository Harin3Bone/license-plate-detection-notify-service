package com.dl.detectionnotifyservice.model.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UploadPayload implements Serializable {

    private UUID uploadId;
    private UUID fileId;
    private String filePath;
    private String contentType;

}

package com.dl.detectionnotifyservice.service;

import com.dl.detectionnotifyservice.constant.Status;
import com.dl.detectionnotifyservice.model.rest.UploadResponse;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadService {

    private static final String FILE_URL_TEMPLATE = "%s/%s.%s";

    @Value("${spring.minio.bucket}")
    private String bucketName;

    private final MinioClient minioClient;

    public UploadResponse uploadFile(String directory, MultipartFile fileData){
        UploadResponse uploadResponse;
        try {
            // Prepare object path
            UploadContext context = getObjectPath(directory, fileData.getOriginalFilename());

            // Extract file input stream
            InputStream inputStream = fileData.getInputStream();

            // Begin upload file to MinIO
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(context.getObjectPath())
                    .contentType(fileData.getContentType())
                    .stream(inputStream, inputStream.available(), -1)
                    .build()
            );
            uploadResponse = new UploadResponse(
                    context.getUploadId(), context.getFileId(), context.getContentType(), Status.SUCCESS.name()
            );
        } catch (Exception e) {
            log.error("Failed to upload file to MinIO: {}", e.getMessage(), e);
            uploadResponse = new UploadResponse(null, null, null, Status.FAILURE.name());
        }

        return uploadResponse;
    }

    private UploadContext getObjectPath(String directory, String fileName) {
        // Prepare directory name as upload ID
        UUID uploadId;
        if (ObjectUtils.isEmpty(directory)) {
            uploadId = UUID.randomUUID();
        } else {
            uploadId = UUID.fromString(directory);
        }

        // Prepare file name as file ID
        UUID fileId = UUID.randomUUID();

        // Extract file extension
        String fileExtension = StringUtils.getFilenameExtension(fileName);

        // Build object path
        String objectPath = FILE_URL_TEMPLATE.formatted(uploadId, fileId, fileExtension);

        return new UploadContext(uploadId, fileId, fileExtension, objectPath);
    }

    @Getter
    @AllArgsConstructor
    private static class UploadContext {
        UUID uploadId;
        UUID fileId;
        String contentType;
        String objectPath;
    }

}

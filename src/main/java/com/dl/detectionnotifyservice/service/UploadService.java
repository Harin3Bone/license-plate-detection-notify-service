package com.dl.detectionnotifyservice.service;

import com.dl.detectionnotifyservice.constant.Status;
import com.dl.detectionnotifyservice.entity.MediaEvidence;
import com.dl.detectionnotifyservice.entity.MediaEvidencePK;
import com.dl.detectionnotifyservice.exception.MediaUploadException;
import com.dl.detectionnotifyservice.model.payload.UploadPayload;
import com.dl.detectionnotifyservice.model.rest.UploadResponse;
import com.dl.detectionnotifyservice.repository.MediaEvidenceRepository;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadService {

    private static final String FILE_PATH_TEMPLATE = "%s/%s.%s";

    @Value("${spring.minio.bucket}")
    private String bucketName;

    private final Queue mediaQueue;
    private final MinioClient minioClient;
    private final RabbitTemplate rabbitTemplate;

    private final MediaEvidenceRepository mediaEvidenceRepository;
    private final Clock systemClock;

    public UploadResponse uploadFile(String directory, MultipartFile fileData) {
        try {
            // Prepare object path
            UploadContext context = getUploadContext(directory, fileData.getOriginalFilename());

            // Extract file input stream
            InputStream inputStream = fileData.getInputStream();

            // Begin upload file to MinIO
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(context.getFilePath())
                    .contentType(fileData.getContentType())
                    .stream(inputStream, inputStream.available(), -1)
                    .build()
            );

            // Populate API response
            return new UploadResponse(
                    context.getUploadId(), context.getFileId(), context.getFilePath(), context.getContentType(), Status.SUCCESS.name()
            );
        } catch (Exception e) {
            log.error("Failed to upload file to MinIO: {}", e.getMessage(), e);
            throw new MediaUploadException("Failed to upload file: " + e.getMessage());
        }
    }

    private UploadContext getUploadContext(String directory, String fileName) {
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
        String contentType = StringUtils.getFilenameExtension(fileName);

        // Build object path
        String filePath = FILE_PATH_TEMPLATE.formatted(uploadId, fileId, contentType);

        return new UploadContext(uploadId, fileId, filePath, contentType);
    }

    public void publishMediaPayload(UploadResponse response) {
        // Build message payload
        UploadPayload payload = buildPayload(response);

        // Send message to RabbitMQ
        rabbitTemplate.convertAndSend(mediaQueue.getName(), payload);
    }

    public void saveMediaEvidence(UploadPayload payload) {
        MediaEvidence mediaEvidence = mapToEntity(payload);
        mediaEvidenceRepository.save(mediaEvidence);
    }

    public List<MediaEvidence> getMediaEvidences(UUID uploadId) {
        return mediaEvidenceRepository.findMediaEvidenceByUploadId(uploadId);
    }

    public File getFileFromMinIO(String filePath) {
        try {
            GetObjectResponse response = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(filePath)
                    .build()
            );

            File tempFile = File.createTempFile("minio-", "-" + filePath.substring(filePath.lastIndexOf('.')));
            try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = response.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            return tempFile;
        } catch (Exception e) {
            log.error("Failed to get file from MinIO: {}", e.getMessage(), e);
            return null;
        }
    }

    private UploadPayload buildPayload(UploadResponse response) {
        UploadPayload payload = new UploadPayload();
        payload.setUploadId(response.uploadId());
        payload.setFileId(response.fileId());
        payload.setFilePath(response.filePath());
        payload.setContentType(response.contentType());

        return payload;
    }

    private MediaEvidence mapToEntity(UploadPayload payload) {
        ZonedDateTime currentDateTime = ZonedDateTime.now(systemClock).withZoneSameInstant(systemClock.getZone());

        MediaEvidence entity = new MediaEvidence();
        entity.setEvidenceId(new MediaEvidencePK(payload.getUploadId(), payload.getFileId()));
        entity.setFilePath(payload.getFilePath());
        entity.setContentType(payload.getContentType());
        entity.setCreateTimestamp(currentDateTime);
        entity.setLastUpdatedTimestamp(currentDateTime);

        return entity;
    }

    @Getter
    @AllArgsConstructor
    private static class UploadContext {
        UUID uploadId;
        UUID fileId;
        String filePath;
        String contentType;
    }

}

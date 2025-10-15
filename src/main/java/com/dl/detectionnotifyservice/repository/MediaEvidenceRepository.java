package com.dl.detectionnotifyservice.repository;

import com.dl.detectionnotifyservice.entity.MediaEvidence;
import com.dl.detectionnotifyservice.entity.MediaEvidencePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MediaEvidenceRepository extends JpaRepository<MediaEvidence, MediaEvidencePK> {

    @Query("""
            SELECT me FROM MediaEvidence me
            WHERE me.evidenceId.uploadId = :uploadId
    """)
    List<MediaEvidence> findMediaEvidenceByUploadId(UUID uploadId);

}

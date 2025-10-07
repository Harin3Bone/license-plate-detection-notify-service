package com.dl.detectionnotifyservice.repository;

import com.dl.detectionnotifyservice.entity.MediaEvidence;
import com.dl.detectionnotifyservice.entity.MediaEvidencePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaEvidenceRepository extends JpaRepository<MediaEvidence, MediaEvidencePK> {

}

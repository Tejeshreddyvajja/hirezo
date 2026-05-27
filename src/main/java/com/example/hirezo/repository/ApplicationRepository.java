package com.example.hirezo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

import com.example.hirezo.entity.ApplicationEntity;
import com.example.hirezo.entity.ApplicationStatus;

public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Long> {

	List<ApplicationEntity> findByStudent_Id(Long studentId);

	List<ApplicationEntity> findByJob_Id(Long jobId);

	List<ApplicationEntity> findByJob_Recruiter_Id(Long recruiterId);

	Optional<ApplicationEntity> findByIdAndJob_Recruiter_Id(Long applicationId, Long recruiterId);

	Optional<ApplicationEntity> findByIdAndStudent_Id(Long applicationId, Long studentId);

	boolean existsByStudent_IdAndJob_Id(Long studentId, Long jobId);

	Page<ApplicationEntity> findByStatus(ApplicationStatus status, Pageable pageable);

}

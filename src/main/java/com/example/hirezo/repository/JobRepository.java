package com.example.hirezo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

import com.example.hirezo.entity.JobEntity;

public interface JobRepository extends JpaRepository<JobEntity, Long> {

	Optional<JobEntity> findByIdAndRecruiter_Id(Long id, Long recruiterId);

	Page<JobEntity> findByLocationContainingIgnoreCase(String location, Pageable pageable);

	Page<JobEntity> findByTitleContainingIgnoreCase(String title, Pageable pageable);

	Page<JobEntity> findByLocationContainingIgnoreCaseAndTitleContainingIgnoreCase(String location, String title, Pageable pageable);

}

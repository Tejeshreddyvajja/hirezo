package com.example.hirezo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.hirezo.dto.ApplicationDto;
import com.example.hirezo.dto.ApplicationResponseDto;
import com.example.hirezo.dto.UserResponseDto;
import com.example.hirezo.entity.ApplicationEntity;
import com.example.hirezo.entity.ApplicationStatus;



public interface ApplicationService {

    ApplicationEntity applyJob(ApplicationDto applicationDto);

    List<ApplicationResponseDto> getApplicationsByStudentId(Long studentId);

    List<UserResponseDto> getApplicantsByJobId(Long jobId);

    ApplicationResponseDto updateApplicationStatusByRecruiter(Long recruiterId, Long applicationId, ApplicationStatus status);

    List<ApplicationResponseDto> getApplicantsForRecruiterJobs(Long recruiterId);

    void deleteApplicationByStudent(Long studentId, Long applicationId);

    Page<ApplicationEntity> searchApplications(ApplicationStatus status, Pageable pageable);
}

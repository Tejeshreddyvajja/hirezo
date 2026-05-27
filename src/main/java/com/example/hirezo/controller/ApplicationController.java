package com.example.hirezo.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import com.example.hirezo.dto.ApplicationDto;
import com.example.hirezo.dto.ApplicationResponseDto;
import com.example.hirezo.dto.ApplicationStatusUpdateRequest;
import com.example.hirezo.dto.PageResponseDto;
import com.example.hirezo.dto.UserResponseDto;
import com.example.hirezo.entity.ApplicationEntity;
import com.example.hirezo.entity.ApplicationStatus;
import com.example.hirezo.service.ApplicationService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;
    private final ModelMapper modelMapper;

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApplicationResponseDto> applyJob(@Valid @RequestBody ApplicationDto applicationDto) {
        ApplicationEntity application = applicationService.applyJob(applicationDto);
        ApplicationResponseDto responseDto = toApplicationResponseDto(application);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PageResponseDto<ApplicationResponseDto>> getApplications(
            @RequestParam(required = false) ApplicationStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ApplicationResponseDto> applicationPage = applicationService.searchApplications(status, pageable)
                .map(this::toApplicationResponseDto);
        return ResponseEntity.status(HttpStatus.OK).body(PageResponseDto.from(applicationPage));
    }

    @GetMapping("/students/{studentId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<ApplicationResponseDto>> getApplicationsByStudent(@PathVariable Long studentId) {
        List<ApplicationResponseDto> applications = applicationService.getApplicationsByStudentId(studentId);
        return ResponseEntity.status(HttpStatus.OK).body(applications);
    }

    @GetMapping("/jobs/{jobId}/applicants")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<List<UserResponseDto>> getApplicantsForJob(@PathVariable Long jobId) {
        List<UserResponseDto> applicants = applicationService.getApplicantsByJobId(jobId);
        return ResponseEntity.status(HttpStatus.OK).body(applicants);
    }

    @PatchMapping("/recruiters/{recruiterId}/{applicationId}/status")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<ApplicationResponseDto> updateApplicationStatus(
            @PathVariable Long recruiterId,
            @PathVariable Long applicationId,
            @Valid @RequestBody ApplicationStatusUpdateRequest request) {
        ApplicationResponseDto updatedApplication = applicationService
                .updateApplicationStatusByRecruiter(recruiterId, applicationId, request.getStatus());
        return ResponseEntity.status(HttpStatus.OK).body(updatedApplication);
    }

    @GetMapping("/recruiters/{recruiterId}/applicants")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<List<ApplicationResponseDto>> getApplicantsForRecruiterJobs(@PathVariable Long recruiterId) {
        List<ApplicationResponseDto> applications = applicationService.getApplicantsForRecruiterJobs(recruiterId);
        return ResponseEntity.status(HttpStatus.OK).body(applications);
    }

    @DeleteMapping("/{applicationId}/students/{studentId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Void> deleteApplication(
            @PathVariable Long applicationId,
            @PathVariable Long studentId) {
        applicationService.deleteApplicationByStudent(studentId, applicationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private ApplicationResponseDto toApplicationResponseDto(ApplicationEntity application) {
        return new ApplicationResponseDto(
                application.getId(),
                application.getStudent().getId(),
                application.getStudent().getName(),
                application.getJob().getId(),
                application.getJob().getTitle(),
                application.getStatus());
    }
    
}

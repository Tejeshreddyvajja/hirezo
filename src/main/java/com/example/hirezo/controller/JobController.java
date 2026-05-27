package com.example.hirezo.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import jakarta.validation.Valid;

import com.example.hirezo.dto.JobDto;
import com.example.hirezo.dto.JobResponseDto;
import com.example.hirezo.dto.JobUpdateRequest;
import com.example.hirezo.dto.PageResponseDto;
import com.example.hirezo.entity.JobEntity;
import com.example.hirezo.service.JobService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;
    private final ModelMapper modelMapper;

    @PostMapping("/jobs")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<JobResponseDto> createjob(@Valid @RequestBody JobDto jobDto) {
        JobEntity createdJob = jobService.createjob(jobDto);
        JobResponseDto createdJobDto = toJobResponseDto(createdJob);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdJobDto);
    }

    @PutMapping("/jobs/{jobId}/recruiters/{recruiterId}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<JobResponseDto> updateJob(
            @PathVariable Long jobId,
            @PathVariable Long recruiterId,
            @Valid @RequestBody JobUpdateRequest request) {
        JobEntity updatedJob = jobService.updateJobByRecruiter(recruiterId, jobId, request);
        JobResponseDto updatedJobDto = toJobResponseDto(updatedJob);
        return ResponseEntity.status(HttpStatus.OK).body(updatedJobDto);
    }

    @DeleteMapping("/jobs/{jobId}/recruiters/{recruiterId}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<Void> deleteJob(
            @PathVariable Long jobId,
            @PathVariable Long recruiterId) {
        jobService.deleteJobByRecruiter(recruiterId, jobId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/jobs")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PageResponseDto<JobResponseDto>> getJobs(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<JobResponseDto> jobPage = jobService.searchJobs(location, title, pageable)
                .map(this::toJobResponseDto);
        return ResponseEntity.status(HttpStatus.OK).body(PageResponseDto.from(jobPage));
    }

    @GetMapping("/jobs/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<JobResponseDto> getJob(@PathVariable Long id) {
        JobEntity job = jobService.getJob(id);
        JobResponseDto jobDto = toJobResponseDto(job);
        return ResponseEntity.status(HttpStatus.OK).body(jobDto);
    }

    private JobResponseDto toJobResponseDto(JobEntity job) {
        return new JobResponseDto(
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.getLocation(),
                job.getSalary(),
                job.getRecruiter() != null ? job.getRecruiter().getId() : null,
                job.getRecruiter() != null ? job.getRecruiter().getName() : null);
    }
    
}

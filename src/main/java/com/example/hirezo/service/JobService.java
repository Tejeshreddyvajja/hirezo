package com.example.hirezo.service;

import com.example.hirezo.dto.JobDto;
import com.example.hirezo.dto.JobUpdateRequest;
import com.example.hirezo.entity.JobEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface JobService {
    
    JobEntity createjob(JobDto jobDto);
    List<JobEntity> getJobs();
    JobEntity getJob(Long id);
    Page<JobEntity> searchJobs(String location, String title, Pageable pageable);
    JobEntity updateJobByRecruiter(Long recruiterId, Long jobId, JobUpdateRequest request);
    void deleteJobByRecruiter(Long recruiterId, Long jobId);
}

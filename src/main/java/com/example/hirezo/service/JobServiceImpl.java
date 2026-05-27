package com.example.hirezo.service;

import java.util.NoSuchElementException;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hirezo.dto.JobDto;
import com.example.hirezo.dto.JobUpdateRequest;
import com.example.hirezo.entity.ApplicationEntity;
import com.example.hirezo.entity.JobEntity;
import com.example.hirezo.entity.UserEntity;
import com.example.hirezo.entity.UserRole;
import com.example.hirezo.repository.ApplicationRepository;
import com.example.hirezo.repository.JobRepository;
import com.example.hirezo.repository.UserRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final ModelMapper modelMapper;

    @Override
    public JobEntity createjob(JobDto jobDto) {
        UserEntity recruiter = userRepository.findById(jobDto.getRecruiterId())
                .orElseThrow(() -> new NoSuchElementException("Recruiter not found with id: " + jobDto.getRecruiterId()));

        if (recruiter.getRole() != UserRole.RECRUITER) {
            throw new InvalidUserRoleException("Only a recruiter can create a job.");
        }

        JobEntity job = modelMapper.map(jobDto, JobEntity.class);
        job.setRecruiter(recruiter);

        return jobRepository.save(job);
    }

    @Override
    public List<JobEntity> getJobs() {
        return jobRepository.findAll();
    }

    @Override
    public JobEntity getJob(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Job not found with id: " + id));
    }

    @Override
    public Page<JobEntity> searchJobs(String location, String title, Pageable pageable) {
        boolean hasLocation = location != null && !location.isBlank();
        boolean hasTitle = title != null && !title.isBlank();

        if (hasLocation && hasTitle) {
            return jobRepository.findByLocationContainingIgnoreCaseAndTitleContainingIgnoreCase(location, title, pageable);
        }

        if (hasLocation) {
            return jobRepository.findByLocationContainingIgnoreCase(location, pageable);
        }

        if (hasTitle) {
            return jobRepository.findByTitleContainingIgnoreCase(title, pageable);
        }

        return jobRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public JobEntity updateJobByRecruiter(Long recruiterId, Long jobId, JobUpdateRequest request) {
        validateRecruiter(recruiterId);

        JobEntity job = jobRepository.findByIdAndRecruiter_Id(jobId, recruiterId)
                .orElseThrow(() -> new NoSuchElementException(
                        "Job not found for recruiter with job id: " + jobId));

        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setLocation(request.getLocation());
        job.setSalary(request.getSalary());

        return jobRepository.save(job);
    }

    @Override
    @Transactional
    public void deleteJobByRecruiter(Long recruiterId, Long jobId) {
        validateRecruiter(recruiterId);

        JobEntity job = jobRepository.findByIdAndRecruiter_Id(jobId, recruiterId)
                .orElseThrow(() -> new NoSuchElementException(
                        "Job not found for recruiter with job id: " + jobId));

        List<ApplicationEntity> applications = applicationRepository.findByJob_Id(job.getId());
        applicationRepository.deleteAll(applications);
        jobRepository.delete(job);
    }

    private void validateRecruiter(Long recruiterId) {
        UserEntity recruiter = userRepository.findById(recruiterId)
                .orElseThrow(() -> new NoSuchElementException("Recruiter not found with id: " + recruiterId));

        if (recruiter.getRole() != UserRole.RECRUITER) {
            throw new InvalidUserRoleException("Only a recruiter can perform this action.");
        }
    }
}

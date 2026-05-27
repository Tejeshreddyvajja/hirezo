    package com.example.hirezo.service;

    import java.util.LinkedHashMap;
    import java.util.List;
    import java.util.NoSuchElementException;
    import java.util.stream.Collectors;

    import org.modelmapper.ModelMapper;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import com.example.hirezo.dto.ApplicationDto;
    import com.example.hirezo.dto.ApplicationResponseDto;
    import com.example.hirezo.dto.UserResponseDto;
    import com.example.hirezo.entity.ApplicationEntity;
    import com.example.hirezo.entity.ApplicationStatus;
    import com.example.hirezo.entity.JobEntity;
    import com.example.hirezo.entity.UserEntity;
    import com.example.hirezo.entity.UserRole;
    import com.example.hirezo.repository.ApplicationRepository;
    import com.example.hirezo.repository.JobRepository;
    import com.example.hirezo.repository.UserRepository;

    import lombok.RequiredArgsConstructor;


    @Service
    @RequiredArgsConstructor
    public class ApplicationServiceImpl implements ApplicationService {

        private final ApplicationRepository applicationRepository;
        private final UserRepository userRepository;
        private final JobRepository jobRepository;
        private final ModelMapper modelMapper;

        @Override
        @Transactional
        public ApplicationEntity applyJob(ApplicationDto applicationDto) {


            UserEntity student = userRepository.findById(applicationDto.getStudentId())
                    .orElseThrow(() -> new NoSuchElementException("Student not found with id: " + applicationDto.getStudentId()));

                if (student.getRole() != UserRole.STUDENT) {
                throw new InvalidUserRoleException("Only a student can apply for a job.");
                }

            JobEntity job = jobRepository.findById(applicationDto.getJobId())
                    .orElseThrow(() -> new NoSuchElementException("Job not found with id: " + applicationDto.getJobId()));

            if (applicationRepository.existsByStudent_IdAndJob_Id(student.getId(), job.getId())) {
                throw new DuplicateApplicationException("You have already applied for this job.");
            }
        
            ApplicationEntity application = new ApplicationEntity();
            application.setStudent(student);
            application.setJob(job);
            application.setStatus(ApplicationStatus.APPLIED);
            return applicationRepository.save(application);
        }

        @Override
        @Transactional(readOnly = true)
        public List<ApplicationResponseDto> getApplicationsByStudentId(Long studentId) {
            return applicationRepository.findByStudent_Id(studentId).stream()
                    .map(this::toApplicationResponseDto)
                    .toList();
        }

        @Override
        @Transactional(readOnly = true)
        public List<UserResponseDto> getApplicantsByJobId(Long jobId) {
            return applicationRepository.findByJob_Id(jobId).stream()
                    .map(ApplicationEntity::getStudent)
                    .collect(Collectors.toMap(
                    UserEntity::getId,
                    student -> modelMapper.map(student, UserResponseDto.class),
                            (existing, replacement) -> existing,
                            LinkedHashMap::new))
                    .values()
                    .stream()
                    .toList();
        }

        @Override
        @Transactional
        public ApplicationResponseDto updateApplicationStatusByRecruiter(Long recruiterId, Long applicationId,
                        ApplicationStatus status) {
            validateRecruiter(recruiterId);

            if (status == null) {
                throw new IllegalArgumentException("Application status is required.");
            }

            ApplicationEntity application = applicationRepository
                    .findByIdAndJob_Recruiter_Id(applicationId, recruiterId)
                    .orElseThrow(() -> new NoSuchElementException(
                            "Application not found for recruiter with application id: " + applicationId));

            application.setStatus(status);
            ApplicationEntity updatedApplication = applicationRepository.save(application);
            return toApplicationResponseDto(updatedApplication);
        }

        @Override
        @Transactional(readOnly = true)
        public List<ApplicationResponseDto> getApplicantsForRecruiterJobs(Long recruiterId) {
            validateRecruiter(recruiterId);
            return applicationRepository.findByJob_Recruiter_Id(recruiterId).stream()
                    .map(this::toApplicationResponseDto)
                    .toList();
        }

        @Override
        public Page<ApplicationEntity> searchApplications(ApplicationStatus status, Pageable pageable) {
            if (status != null) {
                return applicationRepository.findByStatus(status, pageable);
            }

            return applicationRepository.findAll(pageable);
        }

        @Override
        @Transactional
        public void deleteApplicationByStudent(Long studentId, Long applicationId) {
            UserEntity student = userRepository.findById(studentId)
                .orElseThrow(() -> new NoSuchElementException("Student not found with id: " + studentId));

            if (student.getRole() != UserRole.STUDENT) {
            throw new InvalidUserRoleException("Only a student can delete an application.");
            }

            ApplicationEntity application = applicationRepository.findByIdAndStudent_Id(applicationId, studentId)
                .orElseThrow(() -> new NoSuchElementException(
                    "Application not found for student with application id: " + applicationId));

            applicationRepository.delete(application);
        }

        private void validateRecruiter(Long recruiterId) {
            UserEntity recruiter = userRepository.findById(recruiterId)
                    .orElseThrow(() -> new NoSuchElementException("Recruiter not found with id: " + recruiterId));

            if (recruiter.getRole() != UserRole.RECRUITER) {
                throw new InvalidUserRoleException("Only a recruiter can perform this action.");
            }
        }

        private ApplicationResponseDto toApplicationResponseDto(ApplicationEntity application) {
            ApplicationResponseDto responseDto = new ApplicationResponseDto();
            responseDto.setId(application.getId());
            responseDto.setStudentId(application.getStudent().getId());
            responseDto.setStudentName(application.getStudent().getName());
            responseDto.setJobId(application.getJob().getId());
            responseDto.setJobTitle(application.getJob().getTitle());
            responseDto.setStatus(application.getStatus());
            return responseDto;
        }

    }

package com.example.hirezo.dto;

import com.example.hirezo.entity.ApplicationStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class ApplicationStatusUpdateRequest {

    @NotNull
    private ApplicationStatus status;
}
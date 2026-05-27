package com.example.hirezo.dto;

import com.example.hirezo.entity.ApplicationStatus;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationDto {

    @NotNull
    private Long StudentId;
    @NotNull
    private Long JobId;
    private ApplicationStatus status;
}

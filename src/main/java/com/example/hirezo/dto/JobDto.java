package com.example.hirezo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JobDto {

    @NotBlank
    @Size(min = 3, max = 100)
    private String title;

    @NotBlank
    @Size(min = 10, max = 500)
    private String description;

    @NotBlank
    @Size(min = 2, max = 100)
    private String location;

    private int salary;

    @NotNull
    private Long recruiterId;
    
}

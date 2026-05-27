package com.example.hirezo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobResponseDto {

    private Long id;
    private String title;
    private String description;
    private String location;
    private int salary;
    private Long recruiterId;
    private String recruiterName;
}
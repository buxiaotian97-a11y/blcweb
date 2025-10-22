package com.example.blcweb.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DataSetForm(
    @NotBlank @Size(max = 100) String displayName,
    @NotBlank @Size(max = 100) String departmentName,
    boolean notifyTitle
) {}

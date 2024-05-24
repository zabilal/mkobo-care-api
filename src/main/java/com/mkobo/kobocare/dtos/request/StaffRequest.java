package com.mkobo.kobocare.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class StaffRequest {
    @NotBlank(message = "Provide a Full Staff Name")
    private String name;
    private String uuid;
    private Timestamp registrationDate;
}

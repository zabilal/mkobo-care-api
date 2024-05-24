package com.mkobo.kobocare.dtos.response;

import com.mkobo.kobocare.entities.Patient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientResponse {
    private String id;
    private String name;
    private int age;
    private Timestamp lastVisitDate;

    public static PatientResponse toPatient(Patient patient) {
        return new PatientResponse(patient.getId(), patient.getName(), patient.getAge(), patient.getLastVisitDate());
    }
}

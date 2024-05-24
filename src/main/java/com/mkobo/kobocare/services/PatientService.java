package com.mkobo.kobocare.services;

import com.mkobo.kobocare.dtos.response.PatientResponse;
import com.mkobo.kobocare.entities.Patient;
import com.mkobo.kobocare.exceptions.ResourceNotFoundException;
import com.mkobo.kobocare.repositories.PatientRepository;
import com.mkobo.kobocare.utilities.CSVHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private StaffService staffService;

    public List<PatientResponse> getPatientsUpToTwoYearsOld(String uuid) {

        staffService.isValidStaff(uuid);

        List<Patient> patientsByAge = patientRepository.getPatientsByAge(2);
        if (patientsByAge.isEmpty()) {
            throw new ResourceNotFoundException("No Patients with age up to 2 years");
        }

        List<PatientResponse> patientResponses = new ArrayList<>();
        for (Patient patient : patientsByAge) {
            patientResponses.add(new PatientResponse(
                    patient.getId(),
                    patient.getName(),
                    patient.getAge(),
                    patient.getLastVisitDate()
            ));
        }
        return patientResponses;
    }

    public ByteArrayInputStream downloadPatientsProfile(String uuid, String id) {

        staffService.isValidStaff(uuid);

        Optional<Patient> optionalPatient = patientRepository.findById(id);
        if (optionalPatient.isEmpty()) {
            throw new ResourceNotFoundException("Patient not found");
        }
        Patient patient = optionalPatient.get();
        return CSVHelper.patientProfileToCSV(patient);
    }

    public void deleteProfileWithDateRange(String uuid, LocalDateTime initialDate, LocalDateTime endDate) {
        staffService.isValidStaff(uuid);

        patientRepository.deleteByDateRange(initialDate, endDate);
    }

}

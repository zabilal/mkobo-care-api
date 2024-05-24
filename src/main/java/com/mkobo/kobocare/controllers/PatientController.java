package com.mkobo.kobocare.controllers;

import com.mkobo.kobocare.dtos.Response;
import com.mkobo.kobocare.dtos.response.PatientResponse;
import com.mkobo.kobocare.services.PatientService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {
    @Autowired
    private PatientService patientService;

    @GetMapping("")
    public ResponseEntity<?> getPatientsProfile(HttpServletRequest request) {
        String uuid = request.getHeader("uuid");
        if (uuid == null || uuid.isEmpty()){
            Response<String> response = new Response<>(HttpStatus.UNAUTHORIZED.value(), "Missing Staff UUID", "");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        List<PatientResponse> responseList = patientService.getPatientsUpToTwoYearsOld(uuid);
        Response<List<PatientResponse>> response = new Response<>(HttpStatus.OK.value(), "Patient Profiles Retrieved", responseList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> downloadPatientProfile(@PathVariable("id")String id, HttpServletRequest request) {
        String filename = "patient_profile.csv";

        String staffUuid = request.getHeader("uuid");
        if (staffUuid == null || staffUuid.isEmpty()){
            Response<String> response = new Response<>(HttpStatus.UNAUTHORIZED.value(), "Missing Staff UUID", "");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        InputStreamResource file = new InputStreamResource(patientService.downloadPatientsProfile(staffUuid, id));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);

    }

    @DeleteMapping("/{initialDate}/{endDate}")
    public ResponseEntity<?> DeletePatientsProfiles(@PathVariable("initialDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime initialDate,
                                                    @PathVariable("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                                                    HttpServletRequest request) {


        String uuid = request.getHeader("uuid");
        if (uuid == null || uuid.isEmpty()){
            Response<String> response = new Response<>(HttpStatus.UNAUTHORIZED.value(), "Missing Staff UUID", "");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        if (Objects.isNull(initialDate) && Objects.isNull(endDate)){
            Response<String> response = new Response<>(HttpStatus.UNAUTHORIZED.value(), "Add date ranges", "");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        patientService.deleteProfileWithDateRange(uuid, initialDate, endDate);
        Response<?> response = new Response<>(HttpStatus.NO_CONTENT.value(), "Patient Profiles within Date Deleted", "");
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

}

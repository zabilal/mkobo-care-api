package com.mkobo.kobocare;

import com.mkobo.kobocare.controllers.PatientController;
import com.mkobo.kobocare.dtos.response.PatientResponse;
import com.mkobo.kobocare.services.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    private final String VALID_UUID = "valid_uuid";
    private final String INVALID_UUID = "";
    private final LocalDateTime INITIAL_DATE = LocalDateTime.of(2024, 1, 1, 0, 0);
    private final LocalDateTime END_DATE = LocalDateTime.of(2024, 12, 31, 23, 59);

    @Test
    public void testGetPatientsProfile_ValidUUID_ReturnsPatients() throws Exception {
        // Mock service response
        List<PatientResponse> patientList = Arrays.asList(new PatientResponse(), new PatientResponse());
        when(patientService.getPatientsUpToTwoYearsOld(VALID_UUID)).thenReturn(patientList);

        // Perform GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/patients")
                        .header("uuid", VALID_UUID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Patient Profiles Retrieved"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(patientList.size()));
    }

    @Test
    public void testGetPatientsProfile_InvalidUUID_ReturnsUnauthorized() throws Exception {
        // Perform GET request with invalid UUID
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/patients")
                        .header("uuid", INVALID_UUID))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(401))
                .andExpect(jsonPath("$.message").value("Missing Staff UUID"));
    }

    @Test
    public void testDownloadPatientProfile_ValidUUID_ReturnsFile() throws Exception {
        // Mock service response
        byte[] fileContent = "CSV Content".getBytes();
        when(patientService.downloadPatientsProfile(VALID_UUID, "patientId")).thenReturn(new ByteArrayInputStream(fileContent));

        // Perform GET request
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/patients/patientId")
                        .header("uuid", VALID_UUID))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/csv"))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=patient_profile.csv"))
                .andReturn().getResponse();

        // Verify file content
        assertEquals("CSV Content", response.getContentAsString());
    }

    @Test
    public void testDownloadPatientProfile_InvalidUUID_ReturnsUnauthorized() throws Exception {
        // Perform GET request with invalid UUID
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/patients/patientId")
                        .header("uuid", INVALID_UUID))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(401))
                .andExpect(jsonPath("$.message").value("Missing Staff UUID"));
    }

    // Additional test cases can be added to cover other scenarios for getPatientsProfile and downloadPatientProfile methods
}

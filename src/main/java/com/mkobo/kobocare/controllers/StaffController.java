package com.mkobo.kobocare.controllers;

import com.mkobo.kobocare.dtos.Response;
import com.mkobo.kobocare.dtos.request.StaffRequest;
import com.mkobo.kobocare.dtos.response.StaffResponse;
import com.mkobo.kobocare.services.StaffService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/staffs")
public class StaffController {
    @Autowired
    private StaffService staffService;

    @PostMapping()
    public ResponseEntity<?> addStaff(@RequestBody @Valid StaffRequest staff) {
        StaffResponse staffResponse = staffService.addStaff(staff);
        Response<StaffResponse> response = new Response<>(HttpStatus.CREATED.value(), "Staff created", staffResponse);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PutMapping("")
    public ResponseEntity<?> updateStaff(@RequestBody StaffRequest staff, HttpServletRequest request) {
        String uuid = request.getHeader("uuid");
        if (uuid == null || uuid.isEmpty()){
            Response<String> response = new Response<>(HttpStatus.UNAUTHORIZED.value(), "Missing Staff UUID", "");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        StaffResponse staffResponse = staffService.updateStaff(uuid, staff);
        Response<StaffResponse> response = new Response<>(HttpStatus.CREATED.value(), "Staff Profile Updated", staffResponse);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}

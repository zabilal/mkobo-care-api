package com.mkobo.kobocare.services;

import com.mkobo.kobocare.dtos.request.StaffRequest;
import com.mkobo.kobocare.dtos.response.StaffResponse;
import com.mkobo.kobocare.entities.Staff;
import com.mkobo.kobocare.exceptions.ResourceNotFoundException;
import com.mkobo.kobocare.exceptions.UnauthorizedException;
import com.mkobo.kobocare.repositories.StaffRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class StaffService {
  @Autowired private StaffRepository staffRepository;

  public StaffResponse addStaff(StaffRequest staffRequest) {
    Staff staff = new Staff();
    staff.setName(staffRequest.getName());
    staff.setUuid(UUID.randomUUID().toString());
    staff.setRegistrationDate(Timestamp.from(Instant.now()));

    Staff savedStaff = staffRepository.save(staff);

    return StaffResponse.toStaff(savedStaff);
  }

  public StaffResponse updateStaff(String uuid, StaffRequest staffRequest) {

    isValidStaff(uuid);

    Optional<Staff> optional = staffRepository.findByUuid(staffRequest.getUuid());
    if (optional.isEmpty()) {
      throw new ResourceNotFoundException("Staff not found");
    }
    Staff staff = optional.get();

    if (staffRequest.getName() != null) {
      staff.setName(staffRequest.getName());
    }
    if (staffRequest.getRegistrationDate() != null) {
      staff.setRegistrationDate(staffRequest.getRegistrationDate());
    }

    Staff savedStaff = staffRepository.save(staff);
    return StaffResponse.toStaff(staffRepository.save(staff));
  }

  // validate staff access uuid
  public void isValidStaff(String uuid) {
    Optional<Staff> staffOptional = staffRepository.findByUuid(uuid);
    if (staffOptional.isEmpty()) {
      throw new UnauthorizedException("Authorization failed");
    }
  }
}

package com.mkobo.kobocare.dtos.response;

import com.mkobo.kobocare.entities.Staff;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StaffResponse {
  private String id;
  private String name;
  private String uuid;
  private Timestamp registrationDate;

  public static StaffResponse toStaff(Staff staff) {
    return new StaffResponse(
        staff.getId(), staff.getName(), staff.getUuid(), staff.getRegistrationDate());
  }
}

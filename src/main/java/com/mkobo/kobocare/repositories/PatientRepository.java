package com.mkobo.kobocare.repositories;

import com.mkobo.kobocare.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {

  @Query("SELECT u FROM Patient u WHERE u.age = ?1")
  List<Patient> getPatientsByAge(int age);

  @Modifying
  @Transactional
  @Query("DELETE FROM Patient u WHERE u.lastVisitDate BETWEEN ?1 AND ?2")
  void deleteByDateRange(LocalDateTime initialDate, LocalDateTime endDate);
}

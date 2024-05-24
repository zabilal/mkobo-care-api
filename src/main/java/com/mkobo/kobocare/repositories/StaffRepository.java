package com.mkobo.kobocare.repositories;

import com.mkobo.kobocare.entities.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, String> {

  Optional<Staff> findByUuid(String id);
}



package com.a3.clinica.repository;

import com.a3.clinica.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByName(String name);

    List<Patient> findByUserId(Long userId);
}

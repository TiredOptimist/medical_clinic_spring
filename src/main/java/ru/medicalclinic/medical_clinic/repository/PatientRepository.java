package ru.medicalclinic.medical_clinic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.medicalclinic.medical_clinic.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
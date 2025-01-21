package ru.medicalclinic.medical_clinic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.medicalclinic.medical_clinic.entity.Medication;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
}
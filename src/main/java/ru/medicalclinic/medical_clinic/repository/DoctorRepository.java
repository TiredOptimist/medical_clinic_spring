package ru.medicalclinic.medical_clinic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.medicalclinic.medical_clinic.entity.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
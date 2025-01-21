package ru.medicalclinic.medical_clinic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.medicalclinic.medical_clinic.entity.Visit;

import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Long> {
}
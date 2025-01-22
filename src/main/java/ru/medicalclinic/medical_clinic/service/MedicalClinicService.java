package ru.medicalclinic.medical_clinic.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.medicalclinic.medical_clinic.entity.*;
import ru.medicalclinic.medical_clinic.repository.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class MedicalClinicService {
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final VisitRepository visitRepository;
    private final MedicationRepository medicationRepository;
    private final ActionService actionService; // Интеграция ActionService

    @Transactional(readOnly = true)
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Visit> getAllVisits() {
        return visitRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Medication> getAllMedications() {
        return medicationRepository.findAll();
    }

    @Transactional
    public void addDoctor(Doctor doctor) {
        doctorRepository.save(doctor);
    }

    @Transactional
    public void addPatient(Patient patient) {
        patientRepository.save(patient);
    }

    @Transactional
    public void addVisit(Visit visit) {
        visitRepository.save(visit);
    }

    @Transactional
    public void addMedication(Medication medication) {
        medicationRepository.save(medication);
    }

    public void updateDoctor(Long id, Doctor doctorDetails) {
        // Находим врача по ID
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Врач не найден: " + id));

        // Обновляем имя и фамилию врача
        doctor.setFirstName(doctorDetails.getFirstName());
        doctor.setLastName(doctorDetails.getLastName());

        // Сохраняем изменения имени и фамилии
        doctorRepository.save(doctor);

        try {
            // Создаем чекпоинт после обновления имени и фамилии
            actionService.saveCheckpoint();

            // Обновляем даты начала и окончания работы
            doctor.setWorkStartTime(doctorDetails.getWorkStartTime());
            doctor.setWorkEndTime(doctorDetails.getWorkEndTime());

            // Проверяем, что дата начала работы не позже даты окончания
            if (doctor.getWorkStartTime().isAfter(doctor.getWorkEndTime())) {
                // Если даты некорректны, откатываемся до точки сохранения
                actionService.performRollbackToLastCheckpoint();
                log.warn("Дата начала работы позже даты окончания. Откат до точки сохранения.");
            } else {
                // Сохраняем изменения дат
                doctorRepository.save(doctor);

                // Фиксируем транзакцию
                actionService.commitCheckpoint();
            }
        } catch (Exception e) {
            // Если произошла ошибка, откатываем всю транзакцию
            actionService.performRollbackToLastCheckpoint();
            log.error("Ошибка при обновлении врача. Откат всей транзакции.", e);
        }
    }

    @Transactional
    public void updatePatient(Long id, Patient patientDetails) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid patient Id:" + id));
        patient.setFirstName(patientDetails.getFirstName());
        patient.setLastName(patientDetails.getLastName());
        patient.setDateOfBirth(patientDetails.getDateOfBirth());
        patient.setGender(patientDetails.getGender());
        patient.setPhoneNumber(patientDetails.getPhoneNumber());
        patient.setInsuranceNumber(patientDetails.getInsuranceNumber());
        patient.setBloodType(patientDetails.getBloodType());
        patientRepository.save(patient);
    }

    @Transactional
    public void updateVisit(Long id, Visit visitDetails) {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid visit Id:" + id));
        visit.setDoctor(visitDetails.getDoctor());
        visit.setPatient(visitDetails.getPatient());
        visit.setVisitDate(visitDetails.getVisitDate());
        visit.setReasonForVisit(visitDetails.getReasonForVisit());
        visitRepository.save(visit);
    }


    @Transactional
    public void updateMedication(Long id, Medication medicationDetails) {
        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid medication Id:" + id));
        medication.setDiagnosis(medicationDetails.getDiagnosis());
        medication.setName(medicationDetails.getName());
        medication.setDosage(medicationDetails.getDosage());
        medicationRepository.save(medication);
    }

    @Transactional
    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }

    @Transactional
    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }

    @Transactional
    public void deleteVisit(Long id) {
        visitRepository.deleteById(id);
    }

    @Transactional
    public void deleteMedication(Long id) {
        medicationRepository.deleteById(id);
    }
}

package ru.medicalclinic.medical_clinic.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.medicalclinic.medical_clinic.entity.*;
import ru.medicalclinic.medical_clinic.repository.*;

import java.time.LocalDate;
import java.util.List;

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

    @Transactional
    public void updateDoctor(Long id, Doctor doctorDetails) {
        try {
            // Находим врача по ID
            Doctor doctor = doctorRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Врач не найден: " + id));

            // Обновляем имя и фамилию врача
            doctor.setFirstName(doctorDetails.getFirstName());
            doctor.setLastName(doctorDetails.getLastName());

            // Сохраняем точку сохранения
            actionService.saveCheckpoint();

            // Обновляем даты начала и окончания работы
            doctor.setWorkStartTime(doctorDetails.getWorkStartTime());
            doctor.setWorkEndTime(doctorDetails.getWorkEndTime());

            // Проверяем, что дата начала работы раньше даты окончания
            if (doctor.getWorkStartTime().isAfter(doctor.getWorkEndTime())) {
                throw new IllegalArgumentException("Дата начала работы должна быть раньше даты окончания");
            }

            // Сохраняем изменения
            doctorRepository.save(doctor);

            // Фиксируем транзакцию
            actionService.commitCheckpoint();

        } catch (IllegalArgumentException e) {
            // Если даты некорректны, откатываем до точки сохранения
            actionService.performRollbackToLastCheckpoint();

            // Логируем ошибку
            System.out.println("Даты работы не обновлены: " + e.getMessage());

        } catch (Exception e) {
            // Если произошла другая ошибка, откатываем всю транзакцию
            actionService.performRollbackToLastCheckpoint();
            throw new RuntimeException("Ошибка при обновлении врача", e);
        }
    }

    @Transactional
    public void updatePatient(Long id, Patient patientDetails) {
        try {
            // Находим пациента по ID
            Patient patient = patientRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Пациент не найден: " + id));

            // Обновляем имя и фамилию пациента
            patient.setFirstName(patientDetails.getFirstName());
            patient.setLastName(patientDetails.getLastName());

            // Сохраняем точку сохранения
            actionService.saveCheckpoint();

            // Обновляем номер страховки
            patient.setInsuranceNumber(patientDetails.getInsuranceNumber());

            // Проверяем, что номер страховки уникален
            if (patientRepository.existsByInsuranceNumber(patientDetails.getInsuranceNumber())) {
                throw new IllegalArgumentException("Номер страховки уже существует");
            }

            // Сохраняем изменения
            patientRepository.save(patient);

            // Фиксируем транзакцию
            actionService.commitCheckpoint();

        } catch (IllegalArgumentException e) {
            // Если номер страховки уже существует, откатываем до точки сохранения
            actionService.performRollbackToLastCheckpoint();

            // Логируем ошибку
            System.out.println("Номер страховки не обновлен: " + e.getMessage());

        } catch (Exception e) {
            // Если произошла другая ошибка, откатываем всю транзакцию
            actionService.performRollbackToLastCheckpoint();
            throw new RuntimeException("Ошибка при обновлении пациента", e);
        }
    }

    @Transactional
    public void updateVisit(Long id, Visit visitDetails) {
        try {
            // Находим визит по ID
            Visit visit = visitRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Визит не найден: " + id));

            // Обновляем причину визита
            visit.setReasonForVisit(visitDetails.getReasonForVisit());

            // Сохраняем точку сохранения
            actionService.saveCheckpoint();

            // Обновляем дату визита
            visit.setVisitDate(visitDetails.getVisitDate());

            // Проверяем, что дата визита не в прошлом
            if (visit.getVisitDate().isBefore(LocalDate.now())) { // Используем LocalDate.now()
                throw new IllegalArgumentException("Дата визита не может быть в прошлом");
            }

            // Сохраняем изменения
            visitRepository.save(visit);

            // Фиксируем транзакцию
            actionService.commitCheckpoint();

        } catch (IllegalArgumentException e) {
            // Если дата визита в прошлом, откатываем до точки сохранения
            actionService.performRollbackToLastCheckpoint();

            // Логируем ошибку
            System.out.println("Дата визита не обновлена: " + e.getMessage());

        } catch (Exception e) {
            // Если произошла другая ошибка, откатываем всю транзакцию
            actionService.performRollbackToLastCheckpoint();
            throw new RuntimeException("Ошибка при обновлении визита", e);
        }
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

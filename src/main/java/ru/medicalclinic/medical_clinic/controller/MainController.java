package ru.medicalclinic.medical_clinic.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.medicalclinic.medical_clinic.entity.*;
import ru.medicalclinic.medical_clinic.repository.*;

import java.util.Comparator;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final VisitRepository visitRepository;
    private final MedicationRepository medicationRepository;

    @GetMapping("/")
    public String index(Model model) {
        List<Visit> visits = visitRepository.findAll()
                .stream()
                .sorted(Comparator.comparingLong(Visit::getId)).toList();
        List<Doctor> doctors = doctorRepository.findAll()
                .stream()
                .sorted(Comparator.comparingLong(Doctor::getId)).toList();
        List<Patient> patients = patientRepository.findAll()
                .stream()
                .sorted(Comparator.comparingLong(Patient::getId)).toList();
        List<Medication> medications = medicationRepository.findAll()
                .stream()
                .sorted(Comparator.comparingLong(Medication::getId)).toList();
        model.addAttribute("doctors", doctors);
        model.addAttribute("patients", patients);
        model.addAttribute("visits", visits);
        model.addAttribute("medications", medications);

        return "index";
    }

    @GetMapping("/AllVisit")
    public String allVisit(Model model) {
        List<Visit> visits = visitRepository.findAll();
        model.addAttribute("visits", visits);
        return "AllVisit";
    }

    @GetMapping("/AllDoctor")
    public String allDoctor(Model model) {
        List<Doctor> doctors = doctorRepository.findAll();
        model.addAttribute("doctors", doctors);
        return "AllDoctor";
    }

    @GetMapping("/AllPatient")
    public String allPatient(Model model) {
        List<Patient> patients = patientRepository.findAll();
        model.addAttribute("patients", patients);
        return "AllPatient";
    }

    @GetMapping("/AllMedication")
    public String allMedication(Model model) {
        List<Medication> medications = medicationRepository.findAll();
        model.addAttribute("medications", medications);
        return "AllMedication";
    }

    // Add Patient
    @GetMapping("/AddPatient")
    public String showAddPatientForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "AddPatient";
    }

    @PostMapping("/AddPatient")
    public String addPatient(@Valid @ModelAttribute Patient patient, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "AddPatient";
        }
        patientRepository.save(patient);
        return "redirect:/AllPatient";
    }

    // Add Visit
    @GetMapping("/AddVisit")
    public String showAddVisitForm(Model model) {
        model.addAttribute("visit", new Visit());
        model.addAttribute("doctors", doctorRepository.findAll());
        model.addAttribute("patients", patientRepository.findAll());
        return "AddVisit";
    }

    @PostMapping("/AddVisit")
    public String addVisit(@Valid @ModelAttribute Visit visit, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "AddVisit";
        }
        visitRepository.save(visit);
        return "redirect:/AllVisit";
    }

    // Add Doctor
    @GetMapping("/AddDoctor")
    public String showAddDoctorForm(Model model) {
        model.addAttribute("doctor", new Doctor());
        return "AddDoctor";
    }

    @PostMapping("/AddDoctor")
    public String addDoctor(@Valid @ModelAttribute Doctor doctor, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "AddDoctor";
        }
        doctorRepository.save(doctor);
        return "redirect:/AllDoctor";
    }

    // Add Medication
    @GetMapping("/AddMedication")
    public String showAddMedicationForm(Model model) {
        model.addAttribute("medication", new Medication());
        return "AddMedication";
    }

    @PostMapping("/AddMedication")
    public String addMedication(@Valid @ModelAttribute Medication medication, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "AddMedication";
        }
        medicationRepository.save(medication);
        return "redirect:/AllMedication";
    }

    // Edit Patient
    @GetMapping("/editPatient/{id}")
    public String showEditPatientForm(@PathVariable Long id, Model model) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        model.addAttribute("patient", patient);
        return "editPatient";
    }

    @PostMapping("/editPatient/{id}")
    public String editPatient(@PathVariable Long id, @Valid @ModelAttribute Patient updatedPatient, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "editPatient";
        }
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        // Обновляем поля существующего пациента
        existingPatient.setFirstName(updatedPatient.getFirstName());
        existingPatient.setLastName(updatedPatient.getLastName());
        existingPatient.setDateOfBirth(updatedPatient.getDateOfBirth());
        existingPatient.setGender(updatedPatient.getGender());
        existingPatient.setPhoneNumber(updatedPatient.getPhoneNumber());
        existingPatient.setInsuranceNumber(updatedPatient.getInsuranceNumber());
        existingPatient.setBloodType(updatedPatient.getBloodType());
        existingPatient.setHeight(updatedPatient.getHeight());
        existingPatient.setWeight(updatedPatient.getWeight());
        existingPatient.setAttached(updatedPatient.getAttached());

        patientRepository.save(existingPatient);
        return "redirect:/AllPatient";
    }

    // Edit Visit
    @GetMapping("/editVisit/{id}")
    public String showEditVisitForm(@PathVariable Long id, Model model) {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Visit not found"));
        model.addAttribute("visit", visit);
        model.addAttribute("doctors", doctorRepository.findAll()); // Передаем список врачей
        model.addAttribute("patients", patientRepository.findAll()); // Передаем список пациентов
        return "editVisit";
    }

    @PostMapping("/editVisit/{id}")
    public String editVisit(@PathVariable Long id, @Valid @ModelAttribute Visit updatedVisit, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "editVisit";
        }
        Visit existingVisit = visitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Visit not found"));

        // Обновляем поля существующего визита
        existingVisit.setDoctor(updatedVisit.getDoctor());
        existingVisit.setPatient(updatedVisit.getPatient());
        existingVisit.setVisitDate(updatedVisit.getVisitDate());
        existingVisit.setReasonForVisit(updatedVisit.getReasonForVisit());

        visitRepository.save(existingVisit);
        return "redirect:/AllVisit";
    }

    // Edit Doctor
    @GetMapping("/editDoctor/{id}")
    public String showEditDoctorForm(@PathVariable Long id, Model model) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
        model.addAttribute("doctor", doctor);
        return "editDoctor";
    }

    @PostMapping("/editDoctor/{id}")
    public String editDoctor(@PathVariable Long id, @Valid @ModelAttribute Doctor updatedDoctor, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "editDoctor";
        }
        Doctor existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        // Обновляем поля существующего врача
        existingDoctor.setFirstName(updatedDoctor.getFirstName());
        existingDoctor.setLastName(updatedDoctor.getLastName());
        existingDoctor.setSpecialty(updatedDoctor.getSpecialty());
        existingDoctor.setPhoneNumber(updatedDoctor.getPhoneNumber());
        existingDoctor.setWorkStartTime(updatedDoctor.getWorkStartTime());
        existingDoctor.setWorkEndTime(updatedDoctor.getWorkEndTime());
        existingDoctor.setExperienceLevel(updatedDoctor.getExperienceLevel());
        existingDoctor.setRating(updatedDoctor.getRating());

        doctorRepository.save(existingDoctor);
        return "redirect:/AllDoctor";
    }

    // Edit Medication
    @GetMapping("/editMedication/{id}")
    public String showEditMedicationForm(@PathVariable Long id, Model model) {
        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Medication not found"));
        model.addAttribute("medication", medication);
        return "editMedication";
    }

    @PostMapping("/editMedication/{id}")
    public String editMedication(@PathVariable Long id, @Valid @ModelAttribute Medication updatedMedication, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "editMedication";
        }
        Medication existingMedication = medicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Medication not found"));

        // Обновляем поля существующего лекарства
        existingMedication.setDiagnosis(updatedMedication.getDiagnosis());
        existingMedication.setName(updatedMedication.getName());
        existingMedication.setDosage(updatedMedication.getDosage());

        medicationRepository.save(existingMedication);
        return "redirect:/AllMedication";
    }

    // Delete
    @PostMapping("/deletePatient/{id}")
    public String deletePatient(@PathVariable Long id) {
        patientRepository.deleteById(id);
        return "redirect:/AllPatient";
    }

    @PostMapping("/deleteVisit/{id}")
    public String deleteVisit(@PathVariable Long id) {
        visitRepository.deleteById(id);
        return "redirect:/AllVisit";
    }

    @PostMapping("/deleteDoctor/{id}")
    public String deleteDoctor(@PathVariable Long id) {
        doctorRepository.deleteById(id);
        return "redirect:/AllDoctor";
    }

    @PostMapping("/deleteMedication/{id}")
    public String deleteMedication(@PathVariable Long id) {
        medicationRepository.deleteById(id);
        return "redirect:/AllMedication";
    }
}

package ru.medicalclinic.medical_clinic.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.medicalclinic.medical_clinic.entity.*;
import ru.medicalclinic.medical_clinic.service.MedicalClinicService;

import java.util.Comparator;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MedicalClinicService medicalClinicService;

    @GetMapping("/")
    public String index(Model model) {
        List<Visit> visits = medicalClinicService.getAllVisits()
                .stream()
                .sorted(Comparator.comparingLong(Visit::getId)).toList();
        List<Doctor> doctors = medicalClinicService.getAllDoctors()
                .stream()
                .sorted(Comparator.comparingLong(Doctor::getId)).toList();
        List<Patient> patients = medicalClinicService.getAllPatients()
                .stream()
                .sorted(Comparator.comparingLong(Patient::getId)).toList();
        List<Medication> medications = medicalClinicService.getAllMedications()
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
        List<Visit> visits = medicalClinicService.getAllVisits();
        model.addAttribute("visits", visits);
        return "AllVisit";
    }

    @GetMapping("/AllDoctor")
    public String allDoctor(Model model) {
        List<Doctor> doctors = medicalClinicService.getAllDoctors();
        model.addAttribute("doctors", doctors);
        return "AllDoctor";
    }

    @GetMapping("/AllPatient")
    public String allPatient(Model model) {
        List<Patient> patients = medicalClinicService.getAllPatients();
        model.addAttribute("patients", patients);
        return "AllPatient";
    }

    @GetMapping("/AllMedication")
    public String allMedication(Model model) {
        List<Medication> medications = medicalClinicService.getAllMedications();
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
        medicalClinicService.addPatient(patient);
        return "redirect:/AllPatient";
    }

    // Add Visit
    @GetMapping("/AddVisit")
    public String showAddVisitForm(Model model) {
        model.addAttribute("visit", new Visit());
        model.addAttribute("doctors", medicalClinicService.getAllDoctors());
        model.addAttribute("patients", medicalClinicService.getAllPatients());
        return "AddVisit";
    }

    @PostMapping("/AddVisit")
    public String addVisit(@Valid @ModelAttribute Visit visit, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "AddVisit";
        }
        medicalClinicService.addVisit(visit);
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
        medicalClinicService.addDoctor(doctor);
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
        medicalClinicService.addMedication(medication);
        return "redirect:/AllMedication";
    }

    // Edit Patient
    @GetMapping("/editPatient/{id}")
    public String showEditPatientForm(@PathVariable Long id, Model model) {
        Patient patient = medicalClinicService.getAllPatients().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        model.addAttribute("patient", patient);
        return "editPatient";
    }

    @PostMapping("/editPatient/{id}")
    public String editPatient(@PathVariable Long id, @Valid @ModelAttribute Patient updatedPatient, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "editPatient";
        }
        medicalClinicService.updatePatient(id, updatedPatient);
        return "redirect:/AllPatient";
    }

    // Edit Visit
    @GetMapping("/editVisit/{id}")
    public String showEditVisitForm(@PathVariable Long id, Model model) {
        Visit visit = medicalClinicService.getAllVisits().stream()
                .filter(v -> v.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Visit not found"));
        model.addAttribute("visit", visit);
        model.addAttribute("doctors", medicalClinicService.getAllDoctors());
        model.addAttribute("patients", medicalClinicService.getAllPatients());
        return "editVisit";
    }

    @PostMapping("/editVisit/{id}")
    public String editVisit(@PathVariable Long id, @Valid @ModelAttribute Visit updatedVisit, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "editVisit";
        }
        medicalClinicService.updateVisit(id, updatedVisit);
        return "redirect:/AllVisit";
    }

    // Edit Doctor
    @GetMapping("/editDoctor/{id}")
    public String showEditDoctorForm(@PathVariable Long id, Model model) {
        Doctor doctor = medicalClinicService.getAllDoctors().stream()
                .filter(d -> d.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
        model.addAttribute("doctor", doctor);
        return "editDoctor";
    }

    @PostMapping("/editDoctor/{id}")
    public String editDoctor(@PathVariable Long id, @Valid @ModelAttribute Doctor updatedDoctor, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "editDoctor";
        }
        medicalClinicService.updateDoctor(id, updatedDoctor);
        return "redirect:/AllDoctor";
    }

    // Edit Medication
    @GetMapping("/editMedication/{id}")
    public String showEditMedicationForm(@PathVariable Long id, Model model) {
        Medication medication = medicalClinicService.getAllMedications().stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Medication not found"));
        model.addAttribute("medication", medication);
        return "editMedication";
    }

    @PostMapping("/editMedication/{id}")
    public String editMedication(@PathVariable Long id, @Valid @ModelAttribute Medication updatedMedication, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "editMedication";
        }
        medicalClinicService.updateMedication(id, updatedMedication);
        return "redirect:/AllMedication";
    }

    // Delete
    @PostMapping("/deletePatient/{id}")
    public String deletePatient(@PathVariable Long id) {
        medicalClinicService.deletePatient(id);
        return "redirect:/AllPatient";
    }

    @PostMapping("/deleteVisit/{id}")
    public String deleteVisit(@PathVariable Long id) {
        medicalClinicService.deleteVisit(id);
        return "redirect:/AllVisit";
    }

    @PostMapping("/deleteDoctor/{id}")
    public String deleteDoctor(@PathVariable Long id) {
        medicalClinicService.deleteDoctor(id);
        return "redirect:/AllDoctor";
    }

    @PostMapping("/deleteMedication/{id}")
    public String deleteMedication(@PathVariable Long id) {
        medicalClinicService.deleteMedication(id);
        return "redirect:/AllMedication";
    }
}
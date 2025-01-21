package ru.medicalclinic.medical_clinic.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.medicalclinic.medical_clinic.entity.Patient;
import ru.medicalclinic.medical_clinic.repository.PatientRepository;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class PatientController {

    private final PatientRepository patientRepository;

    private final ObjectMapper objectMapper;

    @GetMapping
    public PagedModel<Patient> getAll(Pageable pageable) {
        Page<Patient> patients = patientRepository.findAll(pageable);
        return new PagedModel<>(patients);
    }

    @GetMapping("/{id}")
    public Patient getOne(@PathVariable Long id) {
        Optional<Patient> patientOptional = patientRepository.findById(id);
        return patientOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    @GetMapping("/by-ids")
    public List<Patient> getMany(@RequestParam List<Long> ids) {
        return patientRepository.findAllById(ids);
    }

    @PostMapping
    public Patient create(@RequestBody Patient patient) {
        return patientRepository.save(patient);
    }

    @PatchMapping("/{id}")
    public Patient patch(@PathVariable Long id, @RequestBody JsonNode patchNode) throws IOException {
        Patient patient = patientRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        objectMapper.readerForUpdating(patient).readValue(patchNode);

        return patientRepository.save(patient);
    }

    @PatchMapping
    public List<Long> patchMany(@RequestParam List<Long> ids, @RequestBody JsonNode patchNode) throws IOException {
        Collection<Patient> patients = patientRepository.findAllById(ids);

        for (Patient patient : patients) {
            objectMapper.readerForUpdating(patient).readValue(patchNode);
        }

        List<Patient> resultPatients = patientRepository.saveAll(patients);
        return resultPatients.stream()
                .map(Patient::getId)
                .toList();
    }

    @DeleteMapping("/{id}")
    public Patient delete(@PathVariable Long id) {
        Patient patient = patientRepository.findById(id).orElse(null);
        if (patient != null) {
            patientRepository.delete(patient);
        }
        return patient;
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Long> ids) {
        patientRepository.deleteAllById(ids);
    }
}

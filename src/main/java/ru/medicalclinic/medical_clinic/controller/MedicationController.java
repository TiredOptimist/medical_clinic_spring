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
import ru.medicalclinic.medical_clinic.entity.Medication;
import ru.medicalclinic.medical_clinic.repository.MedicationRepository;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MedicationController {

    private final MedicationRepository medicationRepository;

    private final ObjectMapper objectMapper;

    @GetMapping
    public PagedModel<Medication> getAll(Pageable pageable) {
        Page<Medication> medications = medicationRepository.findAll(pageable);
        return new PagedModel<>(medications);
    }

    @GetMapping("/{id}")
    public Medication getOne(@PathVariable Long id) {
        Optional<Medication> medicationOptional = medicationRepository.findById(id);
        return medicationOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    @GetMapping("/by-ids")
    public List<Medication> getMany(@RequestParam List<Long> ids) {
        return medicationRepository.findAllById(ids);
    }

    @PostMapping
    public Medication create(@RequestBody Medication medication) {
        return medicationRepository.save(medication);
    }

    @PatchMapping("/{id}")
    public Medication patch(@PathVariable Long id, @RequestBody JsonNode patchNode) throws IOException {
        Medication medication = medicationRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        objectMapper.readerForUpdating(medication).readValue(patchNode);

        return medicationRepository.save(medication);
    }

    @PatchMapping
    public List<Long> patchMany(@RequestParam List<Long> ids, @RequestBody JsonNode patchNode) throws IOException {
        Collection<Medication> medications = medicationRepository.findAllById(ids);

        for (Medication medication : medications) {
            objectMapper.readerForUpdating(medication).readValue(patchNode);
        }

        List<Medication> resultMedications = medicationRepository.saveAll(medications);
        return resultMedications.stream()
                .map(Medication::getId)
                .toList();
    }

    @DeleteMapping("/{id}")
    public Medication delete(@PathVariable Long id) {
        Medication medication = medicationRepository.findById(id).orElse(null);
        if (medication != null) {
            medicationRepository.delete(medication);
        }
        return medication;
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Long> ids) {
        medicationRepository.deleteAllById(ids);
    }
}

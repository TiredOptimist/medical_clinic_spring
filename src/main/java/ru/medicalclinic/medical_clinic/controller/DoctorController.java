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
import ru.medicalclinic.medical_clinic.entity.Doctor;
import ru.medicalclinic.medical_clinic.repository.DoctorRepository;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorRepository doctorRepository;

    private final ObjectMapper objectMapper;

    @GetMapping
    public PagedModel<Doctor> getAll(Pageable pageable) {
        Page<Doctor> doctors = doctorRepository.findAll(pageable);
        return new PagedModel<>(doctors);
    }

    @GetMapping("/{id}")
    public Doctor getOne(@PathVariable Long id) {
        Optional<Doctor> doctorOptional = doctorRepository.findById(id);
        return doctorOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    @GetMapping("/by-ids")
    public List<Doctor> getMany(@RequestParam List<Long> ids) {
        return doctorRepository.findAllById(ids);
    }

    @PostMapping
    public Doctor create(@RequestBody Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @PatchMapping("/{id}")
    public Doctor patch(@PathVariable Long id, @RequestBody JsonNode patchNode) throws IOException {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        objectMapper.readerForUpdating(doctor).readValue(patchNode);

        return doctorRepository.save(doctor);
    }

    @PatchMapping
    public List<Long> patchMany(@RequestParam List<Long> ids, @RequestBody JsonNode patchNode) throws IOException {
        Collection<Doctor> doctors = doctorRepository.findAllById(ids);

        for (Doctor doctor : doctors) {
            objectMapper.readerForUpdating(doctor).readValue(patchNode);
        }

        List<Doctor> resultDoctors = doctorRepository.saveAll(doctors);
        return resultDoctors.stream()
                .map(Doctor::getId)
                .toList();
    }

    @DeleteMapping("/{id}")
    public Doctor delete(@PathVariable Long id) {
        Doctor doctor = doctorRepository.findById(id).orElse(null);
        if (doctor != null) {
            doctorRepository.delete(doctor);
        }
        return doctor;
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Long> ids) {
        doctorRepository.deleteAllById(ids);
    }
}

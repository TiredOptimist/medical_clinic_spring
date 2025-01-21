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
import ru.medicalclinic.medical_clinic.entity.Visit;
import ru.medicalclinic.medical_clinic.repository.VisitRepository;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class VisitController {

    private final VisitRepository visitRepository;

    private final ObjectMapper objectMapper;

    @GetMapping
    public PagedModel<Visit> getAll(Pageable pageable) {
        Page<Visit> visits = visitRepository.findAll(pageable);
        return new PagedModel<>(visits);
    }

    @GetMapping("/{id}")
    public Visit getOne(@PathVariable Long id) {
        Optional<Visit> visitOptional = visitRepository.findById(id);
        return visitOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    @GetMapping("/by-ids")
    public List<Visit> getMany(@RequestParam List<Long> ids) {
        return visitRepository.findAllById(ids);
    }

    @PostMapping
    public Visit create(@RequestBody Visit visit) {
        return visitRepository.save(visit);
    }

    @PatchMapping("/{id}")
    public Visit patch(@PathVariable Long id, @RequestBody JsonNode patchNode) throws IOException {
        Visit visit = visitRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        objectMapper.readerForUpdating(visit).readValue(patchNode);

        return visitRepository.save(visit);
    }

    @PatchMapping
    public List<Long> patchMany(@RequestParam List<Long> ids, @RequestBody JsonNode patchNode) throws IOException {
        Collection<Visit> visits = visitRepository.findAllById(ids);

        for (Visit visit : visits) {
            objectMapper.readerForUpdating(visit).readValue(patchNode);
        }

        List<Visit> resultVisits = visitRepository.saveAll(visits);
        return resultVisits.stream()
                .map(Visit::getId)
                .toList();
    }

    @DeleteMapping("/{id}")
    public Visit delete(@PathVariable Long id) {
        Visit visit = visitRepository.findById(id).orElse(null);
        if (visit != null) {
            visitRepository.delete(visit);
        }
        return visit;
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Long> ids) {
        visitRepository.deleteAllById(ids);
    }
}

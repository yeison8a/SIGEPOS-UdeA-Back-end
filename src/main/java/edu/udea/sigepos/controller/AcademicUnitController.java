package edu.udea.sigepos.controller;

import edu.udea.sigepos.model.AcademicUnit;
import edu.udea.sigepos.service.AcademicUnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/academic-units")
public class AcademicUnitController {

    private final AcademicUnitService service;

    @GetMapping
    public ResponseEntity<List<AcademicUnit>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AcademicUnit> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<AcademicUnit> create(AcademicUnit academicUnit) {
        return ResponseEntity.ok(service.save(academicUnit));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AcademicUnit> update(@PathVariable UUID id, @RequestBody AcademicUnit academicUnit) {
        return ResponseEntity.ok(service.update(id, academicUnit));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

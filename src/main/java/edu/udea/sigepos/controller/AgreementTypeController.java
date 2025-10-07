package edu.udea.sigepos.controller;

import edu.udea.sigepos.model.AgreementType;
import edu.udea.sigepos.service.AgreementTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/agreement-types")
public class AgreementTypeController {

    private final AgreementTypeService service;

    @GetMapping
    public ResponseEntity<List<AgreementType>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgreementType> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<AgreementType> create(@RequestBody AgreementType agreementType) {
        return ResponseEntity.ok(service.save(agreementType));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgreementType> update(@PathVariable UUID id, @RequestBody AgreementType agreementType) {
        return ResponseEntity.ok(service.update(id, agreementType));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

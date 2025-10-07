package edu.udea.sigepos.controller;

import edu.udea.sigepos.model.Program;
import edu.udea.sigepos.service.ProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/programs")
public class ProgramController {

    private final ProgramService service;

    @GetMapping
    public ResponseEntity<List<Program>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Program> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Program> create(@RequestBody Program program) {
        return ResponseEntity.ok(service.save(program));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Program> update(@PathVariable UUID id, @RequestBody Program program) {
        return ResponseEntity.ok(service.update(id, program));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

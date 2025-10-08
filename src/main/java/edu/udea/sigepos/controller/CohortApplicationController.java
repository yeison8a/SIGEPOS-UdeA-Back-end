package edu.udea.sigepos.controller;

import edu.udea.sigepos.model.CohortApplication;
import edu.udea.sigepos.repository.CohortApplicationRepository;
import edu.udea.sigepos.service.CohortApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cohort-applications")
public class CohortApplicationController {

    private final CohortApplicationService cohortApplicationService;

    @GetMapping
    public List<CohortApplication> getAll(){
        return cohortApplicationService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CohortApplication> getById(@PathVariable UUID id){
        return cohortApplicationService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public CohortApplication create(@RequestBody CohortApplication cohortApplication){
        return cohortApplicationService.save(cohortApplication);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CohortApplication> update(@PathVariable UUID id, @RequestBody CohortApplication updated){
        try{
            return ResponseEntity.ok(cohortApplicationService.update(id, updated));
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delate(@PathVariable UUID id){
        cohortApplicationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

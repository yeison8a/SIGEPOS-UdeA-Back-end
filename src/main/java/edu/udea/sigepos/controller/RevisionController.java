package edu.udea.sigepos.controller;

import edu.udea.sigepos.dto.CreateRevisionRequest;
import edu.udea.sigepos.model.Revision;
import edu.udea.sigepos.service.RevisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/revisions")
@RequiredArgsConstructor
public class RevisionController {

    private final RevisionService revisionService;

    @PostMapping
    public Revision create(
            @RequestBody CreateRevisionRequest request
    ) {
        return revisionService.create(request);
    }

    @GetMapping
    public List<Revision> findAll() {
        return revisionService.findAll();
    }

    @GetMapping("/{id}")
    public Revision findById(
            @PathVariable UUID id
    ) {
        return revisionService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable UUID id
    ) {
        revisionService.delete(id);
    }
}
package edu.udea.sigepos.controller;

import edu.udea.sigepos.model.Document;
import edu.udea.sigepos.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("user/{userId}/documents")
public class DocumentController {
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping
    public ResponseEntity<Document> subirDocumento(
            @PathVariable UUID userId,
            @RequestParam("archivo") MultipartFile archivo
    ) throws IOException {
        Document doc = documentService.subirDocumento(userId, archivo);
        return ResponseEntity.ok(doc);
    }

    @GetMapping
    public ResponseEntity<List<Document>> listarDocumentos(@PathVariable UUID userId) {
        return ResponseEntity.ok(documentService.listarDocumentosPorUsuario(userId));
    }
}

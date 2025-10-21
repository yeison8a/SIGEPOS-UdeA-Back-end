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
@RequestMapping("api/user/{userId}/documents")
public class DocumentController {
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    /*
    @PostMapping
    public ResponseEntity<Document> subirDocumento(
            @PathVariable UUID userId,
            @RequestParam("archivo") MultipartFile archivo
    ) throws IOException {
        Document doc = documentService.subirDocumento(userId, archivo);
        return ResponseEntity.ok(doc);
    }
    */

    @PostMapping
    public ResponseEntity<List<Document>> subirDocumentos(
            @PathVariable UUID userId,
            @RequestParam("archivos") MultipartFile[] archivos
    ) throws IOException {
        List<Document> documentosGuardados = documentService.subirDocumentos(userId, archivos);
        return ResponseEntity.ok(documentosGuardados);
    }

    @GetMapping
    public ResponseEntity<List<Document>> listarDocumentos(@PathVariable UUID userId) {
        return ResponseEntity.ok(documentService.listarDocumentosPorUsuario(userId));
    }

    @GetMapping("/{docId}")
    public ResponseEntity<Document> obtenerDocumento(
            @PathVariable UUID userId,
            @PathVariable UUID docId
    ) {
        return ResponseEntity.ok(documentService.obtenerDocumento(userId, docId));
    }

    @PutMapping("/{docId}")
    public ResponseEntity<Document> actualizarDocumento(
            @PathVariable UUID userId,
            @PathVariable UUID docId,
            @RequestParam(value = "archivo", required = false) MultipartFile archivo,
            @RequestParam(value = "nombre", required = false) String nuevoNombre
    ) throws IOException {
        return ResponseEntity.ok(documentService.actualizarDocumento(userId, docId, archivo, nuevoNombre));
    }

    @DeleteMapping("/{docId}")
    public ResponseEntity<Void> eliminarDocumento(
            @PathVariable UUID userId,
            @PathVariable UUID docId
    ) {
        documentService.eliminarDocumento(userId, docId);
        return ResponseEntity.noContent().build();
    }
}

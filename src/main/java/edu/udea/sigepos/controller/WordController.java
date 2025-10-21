package edu.udea.sigepos.controller;

import edu.udea.sigepos.service.WordTemplateService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/word")
public class WordController {

    private final WordTemplateService wordService;

    public WordController(WordTemplateService wordService) {
        this.wordService = wordService;
    }

    @GetMapping("/cohorte/{id}")
    public ResponseEntity<byte[]> generarWord(@PathVariable UUID id) throws IOException {
        File file = wordService.generarDocumento(id);
        byte[] contents = Files.readAllBytes(file.toPath());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"cohorte-" + id + ".docx\"")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
                .body(contents);
    }

    @PostMapping("/cohorte/{cohortId}/enviar/{userId}")
    public ResponseEntity<String> enviarWord(@PathVariable UUID cohortId, @PathVariable UUID userId) throws IOException {
        wordService.generarYEnviarDocumento(cohortId, userId);
        return ResponseEntity.ok("Documento enviado correctamente al correo del usuario.");
    }

}

package edu.udea.sigepos.controller;

import edu.udea.sigepos.service.ProgramImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/import")
public class ImportController {

    private final ProgramImportService programImportService;

    @PostMapping("/programs")
    public ResponseEntity<String> importPrograms(@RequestParam("file") MultipartFile file) {
        try{
            programImportService.importExcel(file);
            return ResponseEntity.ok().body("Importado correctamente");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("Error al importar: " + e.getMessage());
        }
    }
}

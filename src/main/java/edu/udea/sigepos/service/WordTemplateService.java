package edu.udea.sigepos.service;

import edu.udea.sigepos.model.CohortApplication;
import edu.udea.sigepos.model.Program;
import edu.udea.sigepos.model.AcademicUnit;
import edu.udea.sigepos.model.User;
import edu.udea.sigepos.repository.CohortApplicationRepository;
import edu.udea.sigepos.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WordTemplateService {

    private final CohortApplicationRepository cohortRepo;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public WordTemplateService(CohortApplicationRepository cohortRepo,
                               UserRepository userRepository,
                               EmailService emailService) {
        this.cohortRepo = cohortRepo;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Transactional
    public File generarDocumento(UUID cohortId) throws IOException {
        CohortApplication app = cohortRepo.findByIdWithProgramAndUnit(cohortId)
                .orElseThrow(() -> new IllegalArgumentException("Cohorte no encontrada"));
        return generarDocumentoInterno(app);
    }

    @Transactional
    public void generarYEnviarDocumento(UUID cohortId, UUID userId) throws IOException {
        CohortApplication app = cohortRepo.findByIdWithProgramAndUnit(cohortId)
                .orElseThrow(() -> new IllegalArgumentException("Cohorte no encontrada"));

        File documento = generarDocumentoInterno(app);

        User destinatario = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        emailService.enviarCorreoConAdjunto(
                destinatario.getCorreo(),
                "Solicitud de Cohorte",
                "Adjunto encontrarás el documento generado para tu solicitud de cohorte.",
                documento
        );
    }

    private File generarDocumentoInterno(CohortApplication app) throws IOException {
        Program programa = app.getPrograma();
        AcademicUnit unidad = (programa != null) ? programa.getUnidadAcademica() : null;

        Map<String, String> data = new HashMap<>();
        data.put("Programa", (programa != null) ? programa.getNombre() : "N/A");
        data.put("Unidad Académica", (unidad != null) ? unidad.getNombre() : "N/A");
        data.put("NumeroActa", Optional.ofNullable(app.getNumeroActa()).orElse("N/A"));
        data.put("FechaActaAprobacion",
                (app.getFechaActaAprobacion() != null)
                        ? new SimpleDateFormat("dd/MM/yyyy").format(app.getFechaActaAprobacion())
                        : "N/A");
        data.put("PerfilAspirante", Optional.ofNullable(app.getPerfilAspirante()).orElse("N/A"));
        data.put("CorreoDocumentacion", Optional.ofNullable(app.getCorreoDocumentacion()).orElse("N/A"));
        data.put("DiasHabilesRecepcion", String.valueOf(app.getDiasHabilesRecepcion()));
        data.put("PuntajeMinimoCorte", String.valueOf(app.getPuntajeMinimoCorte()));
        data.put("CupoMinCohorte", String.valueOf(app.getCupoMinCohorte()));
        data.put("CupoMaxCohorte", String.valueOf(app.getCupoMaxCohorte()));
        data.put("CupoEstudiantes", String.valueOf(app.getCupoEstudiantes()));
        data.put("PlazasDisponibles", app.isPlazasDisponibles() ? "Sí" : "No");

        data.put("Acuerdo que confiere facultades", (programa != null && programa.getAcuardosCreacion() != null)
                ? programa.getAcuardosCreacion()
                : "N/A");

        data.put("Acuerdo de creación",
                (programa != null && programa.getTipoAcuerdo() != null)
                        ? programa.getTipoAcuerdo().getNombre()
                        : "N/A");

        data.put("Acreditación alta calidad",
                (programa != null && programa.getAcreditacionAltaCalidad() != null)
                        ? programa.getAcreditacionAltaCalidad()
                        : "N/A");

        data.put("Registro calificado",
                (programa != null && programa.getRegistroCalificado() != null)
                        ? String.valueOf(programa.getRegistroCalificado())
                        : "N/A");

        data.put("SNIES",
                (programa != null && programa.getSnies() != null)
                        ? String.valueOf(programa.getSnies())
                        : "N/A");

        data.put("fecha de la reunión",
                app.getFechaActaAprobacion() != null
                        ? new SimpleDateFormat("dd/MM/yyyy").format(app.getFechaActaAprobacion())
                        : "N/A");

        data.put("número del acta",
                app.getNumeroActa() != null
                        ? app.getNumeroActa()
                        : "N/A");

        data.put("Indique el perfil del aspirante al programa",
                app.getPerfilAspirante() != null
                        ? app.getPerfilAspirante()
                        : "N/A");

        data.put("Indique el correo institucional al que el aspirante debe remitir la documentación faltante",
                app.getCorreoDocumentacion() != null
                        ? app.getCorreoDocumentacion()
                        : "N/A");

        data.put("Indique el número de días hábiles disponibles para la recepción de documentos una vez finalizado el período de inscripciones",
                String.valueOf(app.getDiasHabilesRecepcion()));

        data.put("Indique el puntaje mínimo requerido como punto de corte", String.valueOf(app.getPuntajeMinimoCorte()));

        data.put("Indique el cupo máximo para la cohorte", String.valueOf(app.getCupoMaxCohorte()));

        data.put("Indique el cupo mínimo para la cohorte según el estudio de costos avalado por la Vicerrectoría Administrativa", String.valueOf(app.getCupoMinCohorte()));

        data.put("Indique el número de plazas de estudiante instructor que el programa va a ofrecer en esta cohorte", String.valueOf(app.getCupoEstudiantes()));


        File templateFile = new ClassPathResource("templates/plantilla.docx").getFile();
        try (FileInputStream fis = new FileInputStream(templateFile);
             XWPFDocument doc = new XWPFDocument(fis)) {

            replaceTextPreserveStyle(doc.getParagraphs(), data);

            for (XWPFTable table : doc.getTables()) {
                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        replaceTextPreserveStyle(cell.getParagraphs(), data);
                    }
                }
            }

            File outputFile = File.createTempFile("cohorte_", ".docx");
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                doc.write(fos);
            }

            return outputFile;
        }
    }

    private void replaceTextPreserveStyle(List<XWPFParagraph> paragraphs, Map<String, String> data) {
        for (XWPFParagraph paragraph : paragraphs) {
            List<XWPFRun> runs = paragraph.getRuns();
            if (runs == null || runs.isEmpty()) continue;

            StringBuilder fullText = new StringBuilder();
            for (XWPFRun run : runs) {
                String t = run.getText(0);
                if (t != null) fullText.append(t);
            }

            String replacedText = fullText.toString();
            boolean hasChange = false;
            for (Map.Entry<String, String> entry : data.entrySet()) {
                if (replacedText.contains("{{" + entry.getKey() + "}}")) {
                    replacedText = replacedText.replace("{{" + entry.getKey() + "}}", entry.getValue());
                    hasChange = true;
                }
            }

            if (hasChange) {
                int currentIndex = 0;
                for (XWPFRun run : runs) {
                    String t = run.getText(0);
                    if (t != null) {
                        int endIndex = currentIndex + t.length();
                        if (endIndex > replacedText.length()) endIndex = replacedText.length();
                        run.setText(replacedText.substring(currentIndex, endIndex), 0);
                        currentIndex = endIndex;
                    }
                }
                if (currentIndex < replacedText.length()) {
                    XWPFRun lastRun = runs.get(runs.size() - 1);
                    lastRun.setText(lastRun.getText(0) + replacedText.substring(currentIndex), 0);
                }
            }
        }
    }
}

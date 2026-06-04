package edu.udea.sigepos.service;

import edu.udea.sigepos.dto.CreateRevisionRequest;
import edu.udea.sigepos.model.CohortApplication;
import edu.udea.sigepos.model.Revision;
import edu.udea.sigepos.model.User;
import edu.udea.sigepos.repository.CohortApplicationRepository;
import edu.udea.sigepos.repository.RevisionRepository;
import edu.udea.sigepos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RevisionService {

    private final RevisionRepository revisionRepository;
    private final CohortApplicationRepository cohortApplicationRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public Revision create(CreateRevisionRequest request) {

        CohortApplication application =
                cohortApplicationRepository.findById(
                        request.getCohortApplicationId()
                ).orElseThrow();

        User reviewer =
                userRepository.findById(
                        request.getReviewerId()
                ).orElseThrow();

        Revision revision = Revision.builder()
                .cohortApplication(application)
                .reviewer(reviewer)
                .priority(request.getPriority())
                .status(request.getStatus())
                .observations(request.getObservations())
                .reviewDate(new Date())
                .build();

        Revision savedRevision =
                revisionRepository.save(revision);

        try {

            User solicitante =
                    application.getUsuario();

            String asunto =
                    "Observaciones de la revisión de su solicitud";

            String mensaje =
                    """
                    Su solicitud ha sido revisada.
    
                    Estado: %s
                    Prioridad: %s
    
                    Observaciones:
                    %s
    
                    """.formatted(
                            request.getStatus(),
                            request.getPriority(),
                            request.getObservations()
                    );

            emailService.enviarCorreoConAdjunto(
                    solicitante.getCorreo(),
                    asunto,
                    mensaje,
                    null
            );

        } catch (Exception e) {
            e.printStackTrace();
        }

        return savedRevision;
    }

    public List<Revision> findAll() {
        return revisionRepository.findAll();
    }

    public Revision findById(UUID id) {
        return revisionRepository.findById(id)
                .orElseThrow();
    }

    public void delete(UUID id) {
        revisionRepository.deleteById(id);
    }
}
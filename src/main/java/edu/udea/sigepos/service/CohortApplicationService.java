package edu.udea.sigepos.service;

import edu.udea.sigepos.model.CohortApplication;
import edu.udea.sigepos.repository.CohortApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CohortApplicationService {

    private final CohortApplicationRepository cohortApplicationRepository;

    public List<CohortApplication> findAll(){
        return cohortApplicationRepository.findAll();
    }

    public Optional<CohortApplication> findById(UUID id){
        return cohortApplicationRepository.findById(id);
    }

    public CohortApplication save(CohortApplication cohortApplication){
        return cohortApplicationRepository.save(cohortApplication);
    }

    public CohortApplication update(UUID id, CohortApplication updated){
        return cohortApplicationRepository.findById(id)
                .map(existing -> {
                    existing.setNumeroActa(updated.getNumeroActa());
                    existing.setFechaActaAprobacion(updated.getFechaActaAprobacion());
                    existing.setUnidadAcademica(updated.getUnidadAcademica());
                    existing.setPerfilAspirante(updated.getPerfilAspirante());
                    existing.setCorreoDocumentacion(updated.getCorreoDocumentacion());
                    existing.setDiasHabilesRecepcion(updated.getDiasHabilesRecepcion());
                    existing.setPuntajeMinimoCorte(updated.getPuntajeMinimoCorte());
                    existing.setCupoMinCohorte(updated.getCupoMinCohorte());
                    existing.setCupoMaxCohorte(updated.getCupoMaxCohorte());
                    existing.setCupoEstudiantes(updated.getCupoEstudiantes());
                    existing.setPlazasDisponibles(updated.isPlazasDisponibles());
                    existing.setInstructor(updated.getInstructor());
                    existing.setPeriodoCohorte(updated.getPeriodoCohorte());
                    return cohortApplicationRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Solicitud de cohorte no encontrada"));
    }

    public void delete(UUID id){
        cohortApplicationRepository.deleteById(id);
    }
}

package edu.udea.sigepos.service;

import edu.udea.sigepos.model.AgreementType;
import edu.udea.sigepos.repository.AgreementTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgreementTypeService {

    private final AgreementTypeRepository agreementTypeRepository;


    public List<AgreementType> findAll() {
        return agreementTypeRepository.findAll();
    }

    public AgreementType findById(UUID id) {
        return agreementTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo de acuerdo no encontrado"));
    }

    public AgreementType save(AgreementType agreementType) {
        return agreementTypeRepository.save(agreementType);
    }

    public AgreementType update(UUID id, AgreementType updated) {
        AgreementType existing = findById(id);
        existing.setNombre(updated.getNombre());
        return agreementTypeRepository.save(existing);
    }

    public void delete(UUID id) {
        agreementTypeRepository.deleteById(id);
    }
}

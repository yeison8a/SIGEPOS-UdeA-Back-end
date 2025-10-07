package edu.udea.sigepos.service;

import edu.udea.sigepos.model.AcademicUnit;
import edu.udea.sigepos.repository.AcademicUnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AcademicUnitService {

    private final AcademicUnitRepository academicUnitRepository;

    public List<AcademicUnit> findAll() {
        return academicUnitRepository.findAll();
    }

    public AcademicUnit findById(UUID id) {
        return academicUnitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Unidad académica no encontrada"));
    }

    public AcademicUnit save(AcademicUnit academicUnit) {
        return academicUnitRepository.save(academicUnit);
    }

    public AcademicUnit update(UUID id, AcademicUnit updated) {
        AcademicUnit existing = findById(id);
        existing.setNombre(updated.getNombre());
        return academicUnitRepository.save(existing);
    }

    public void delete(UUID id) {
        academicUnitRepository.deleteById(id);
    }
}

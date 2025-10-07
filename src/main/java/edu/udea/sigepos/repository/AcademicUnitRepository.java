package edu.udea.sigepos.repository;

import edu.udea.sigepos.model.AcademicUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AcademicUnitRepository extends JpaRepository<AcademicUnit, UUID> {
}

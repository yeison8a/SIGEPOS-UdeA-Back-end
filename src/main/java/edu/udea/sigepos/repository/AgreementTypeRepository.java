package edu.udea.sigepos.repository;

import edu.udea.sigepos.model.AgreementType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AgreementTypeRepository extends JpaRepository<AgreementType, UUID> {
}

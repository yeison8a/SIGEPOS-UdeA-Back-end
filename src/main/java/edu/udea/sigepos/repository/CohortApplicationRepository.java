package edu.udea.sigepos.repository;

import edu.udea.sigepos.model.CohortApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CohortApplicationRepository extends JpaRepository<CohortApplication, UUID> {
}

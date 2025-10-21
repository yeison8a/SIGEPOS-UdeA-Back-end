package edu.udea.sigepos.repository;

import edu.udea.sigepos.model.CohortApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CohortApplicationRepository extends JpaRepository<CohortApplication, UUID> {

    @Query("SELECT c FROM CohortApplication c " +
        "JOIN FETCH c.programa p " +
            "JOIN FETCH p.unidadAcademica " +
               "WHERE c.id = :id")
    Optional<CohortApplication> findByIdWithProgramAndUnit(@Param("id")UUID id);
}

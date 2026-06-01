package edu.udea.sigepos.repository;

import edu.udea.sigepos.model.Revision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RevisionRepository extends JpaRepository<Revision, UUID> {

}

package edu.udea.sigepos.repository;

import edu.udea.sigepos.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, UUID> {
    List<Document> findByUsuarioId(UUID userId);
}

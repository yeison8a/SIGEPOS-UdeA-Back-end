package edu.udea.sigepos.repository;

import edu.udea.sigepos.model.Program;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProgramRepository extends JpaRepository<Program, UUID> {
}

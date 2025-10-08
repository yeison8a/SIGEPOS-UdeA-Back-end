package edu.udea.sigepos.service;

import edu.udea.sigepos.model.Program;
import edu.udea.sigepos.repository.ProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProgramService {

    private final ProgramRepository programRepository;

    public List<Program> findAll(){
        return programRepository.findAll();
    }

    public Program findById(UUID id){
        return programRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programa no encontrado"));
    }

    public Program save(Program program){
        return programRepository.save(program);
    }

    public Program update(UUID id, Program updatedProgram){
        Program existing = findById(id);
        existing.setCodigo(updatedProgram.getCodigo());
        existing.setNombre(updatedProgram.getNombre());
        existing.setUnidadAcademica(updatedProgram.getUnidadAcademica());
        existing.setSnies(updatedProgram.getSnies());
        existing.setRegistroCalificado(updatedProgram.getRegistroCalificado());
        existing.setFechaRegistroCalificado(updatedProgram.getFechaRegistroCalificado());
        existing.setAcuardosCreacion(updatedProgram.getAcuardosCreacion());
        existing.setTipoAcuerdo(updatedProgram.getTipoAcuerdo());
        existing.setAcreditacionAltaCalidad(updatedProgram.getAcreditacionAltaCalidad());
        existing.setFechaAcreditacion(updatedProgram.getFechaAcreditacion());
        return programRepository.save(existing);
    }

    public void delete(UUID id){
        programRepository.deleteById(id);
    }

}

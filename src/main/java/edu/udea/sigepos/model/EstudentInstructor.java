package edu.udea.sigepos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "estudiante_instructor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstudentInstructor {

    @Id
    @GeneratedValue
    private UUID id;

    private String infoEstimuloInstructor;

    private int numeroPlazas;
}

package edu.udea.sigepos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.UUID;

@Entity
@Table(name = "solicitud_cohorte")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CohortApplication {

    @Id
    @GeneratedValue
    private UUID id;

    private String numeroActa;
    private Date fechaActaAprobacion;

    @ManyToOne(optional = false)
    @JoinColumn(name = "unidad_academica_id", nullable = false)
    private AcademicUnit unidadAcademica;

    private String perfilAspirante;
    private String correoDocumentacion;
    private int diasHabilesRecepcion;
    private int puntajeMinimoCorte;
    private int cupoMinCohorte;
    private int cupoMaxCohorte;
    private int cupoEstudiantes;
    private boolean plazasDisponibles;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cohorte_id", nullable = false)
    private EstudentInstructor instructor;
    private String periodoCohorte;
}

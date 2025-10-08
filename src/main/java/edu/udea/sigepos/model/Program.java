package edu.udea.sigepos.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "programa")
public class Program {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private Long codigo;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidad_academica_id", nullable = false)
    private AcademicUnit unidadAcademica;

    private Long snies;

    private Long registroCalificado;

    @Temporal(TemporalType.DATE)
    private Date fechaRegistroCalificado;

    private String acuardosCreacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_acuerdo_id", nullable = false)
    private AgreementType tipoAcuerdo;

    private String acreditacionAltaCalidad;

    @Temporal(TemporalType.DATE)
    private Date fechaAcreditacion;

}

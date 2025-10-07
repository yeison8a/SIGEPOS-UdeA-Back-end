package edu.udea.sigepos.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "unidades_academicas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicUnit {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String nombre;
}

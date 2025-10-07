package edu.udea.sigepos.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "tipo_acuerdo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgreementType {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String nombre;
}

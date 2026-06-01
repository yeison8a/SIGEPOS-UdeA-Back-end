package edu.udea.sigepos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "revision")
public class Revision {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cohort_application_id", nullable = false)
    private CohortApplication cohortApplication;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;

    @Column(nullable = false)
    private String status;

    @Column(length = 2000)
    private String observations;

    @Column(nullable = false)
    private String priority;

    @Temporal(TemporalType.TIMESTAMP)
    private Date reviewDate;
}
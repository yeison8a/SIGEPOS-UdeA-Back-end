package edu.udea.sigepos.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateRevisionRequest {

    private UUID cohortApplicationId;

    private UUID reviewerId;

    private String priority;

    private String status;

    private String observations;
}
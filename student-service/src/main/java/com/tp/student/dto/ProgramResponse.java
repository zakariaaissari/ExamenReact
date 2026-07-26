package com.tp.student.dto;

/**
 * Mirrors the response body that program-service returns for GET /programs/{id}.
 * Feign deserializes the JSON from program-service into this object.
 * If program-service adds new fields, this class only needs updating if we need them.
 */
public class ProgramResponse {

    private Long id;
    private String name;
    private String description;
    private int durationYears;

    public ProgramResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getDurationYears() { return durationYears; }
    public void setDurationYears(int durationYears) { this.durationYears = durationYears; }
}

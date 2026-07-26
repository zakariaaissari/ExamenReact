package com.tp.program.dto;

/**
 * This is the contract that student-service depends on via its own ProgramResponse DTO.
 * Any field added here should be added there too if it needs to be consumed.
 */
public class ProgramResponse {

    private Long id;
    private String name;
    private String description;
    private int durationYears;

    public ProgramResponse() {}

    public ProgramResponse(Long id, String name, String description, int durationYears) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.durationYears = durationYears;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getDurationYears() { return durationYears; }
    public void setDurationYears(int durationYears) { this.durationYears = durationYears; }
}

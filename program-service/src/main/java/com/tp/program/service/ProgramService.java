package com.tp.program.service;

import com.tp.program.dto.ProgramRequest;
import com.tp.program.dto.ProgramResponse;

import java.util.List;

public interface ProgramService {

    List<ProgramResponse> getAllPrograms();

    ProgramResponse getProgramById(Long id);

    ProgramResponse createProgram(ProgramRequest request);

    ProgramResponse updateProgram(Long id, ProgramRequest request);

    void deleteProgram(Long id);
}

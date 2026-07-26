package com.tp.program.service;

import com.tp.program.dto.ProgramRequest;
import com.tp.program.dto.ProgramResponse;
import com.tp.program.entity.Program;
import com.tp.program.exception.ProgramNotFoundException;
import com.tp.program.repository.ProgramRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgramServiceImpl implements ProgramService {

    private final ProgramRepository programRepository;

    public ProgramServiceImpl(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    @Override
    public List<ProgramResponse> getAllPrograms() {
        return programRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProgramResponse getProgramById(Long id) {
        Program program = programRepository.findById(id)
                .orElseThrow(() -> new ProgramNotFoundException(id));
        return toResponse(program);
    }

    @Override
    public ProgramResponse createProgram(ProgramRequest request) {
        if (programRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Program name already exists: " + request.getName());
        }
        Program program = new Program(
                request.getName(),
                request.getDescription(),
                request.getDurationYears()
        );
        return toResponse(programRepository.save(program));
    }

    @Override
    public ProgramResponse updateProgram(Long id, ProgramRequest request) {
        Program program = programRepository.findById(id)
                .orElseThrow(() -> new ProgramNotFoundException(id));

        // Allow same name only if it belongs to this program
        programRepository.findByName(request.getName()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new IllegalArgumentException("Program name already exists: " + request.getName());
            }
        });

        program.setName(request.getName());
        program.setDescription(request.getDescription());
        program.setDurationYears(request.getDurationYears());

        return toResponse(programRepository.save(program));
    }

    @Override
    public void deleteProgram(Long id) {
        if (!programRepository.existsById(id)) {
            throw new ProgramNotFoundException(id);
        }
        programRepository.deleteById(id);
    }

    private ProgramResponse toResponse(Program program) {
        return new ProgramResponse(
                program.getId(),
                program.getName(),
                program.getDescription(),
                program.getDurationYears()
        );
    }
}

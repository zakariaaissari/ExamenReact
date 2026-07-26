package com.tp.student.service;

import com.tp.student.client.ProgramClient;
import com.tp.student.dto.ProgramResponse;
import com.tp.student.dto.StudentRequest;
import com.tp.student.dto.StudentResponse;
import com.tp.student.entity.Student;
import com.tp.student.exception.StudentNotFoundException;
import com.tp.student.repository.StudentRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final StudentRepository studentRepository;
    private final ProgramClient programClient;

    // Constructor injection — preferred over @Autowired for testability
    public StudentServiceImpl(StudentRepository studentRepository, ProgramClient programClient) {
        this.studentRepository = studentRepository;
        this.programClient = programClient;
    }

    @Override
    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public StudentResponse getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        return toResponse(student);
    }

    @Override
    public StudentResponse createStudent(StudentRequest request) {
        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use: " + request.getEmail());
        }
        Student student = new Student(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getProgramId()
        );
        Student saved = studentRepository.save(student);
        return toResponse(saved);
    }

    @Override
    public StudentResponse updateStudent(Long id, StudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));

        // Allow same email only if it belongs to this student
        studentRepository.findByEmail(request.getEmail()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new IllegalArgumentException("Email already in use: " + request.getEmail());
            }
        });

        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setProgramId(request.getProgramId());

        return toResponse(studentRepository.save(student));
    }

    @Override
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException(id);
        }
        studentRepository.deleteById(id);
    }


    private StudentResponse toResponse(Student student) {
        StudentResponse response = new StudentResponse();
        response.setId(student.getId());
        response.setFirstName(student.getFirstName());
        response.setLastName(student.getLastName());
        response.setEmail(student.getEmail());
        response.setProgramId(student.getProgramId());

        if (student.getProgramId() != null) {
            try {
                ProgramResponse program = programClient.getProgramById(student.getProgramId());
                response.setProgram(program);
            } catch (FeignException e) {
                log.warn("Could not fetch program {} for student {}: {}",
                        student.getProgramId(), student.getId(), e.getMessage());
            }
        }

        return response;
    }
}

package com.tp.student.repository;

import com.tp.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);

    boolean existsByEmail(String email);

    // Used to rank a student within their program (relevé feature).
    List<Student> findByProgramId(Long programId);
}

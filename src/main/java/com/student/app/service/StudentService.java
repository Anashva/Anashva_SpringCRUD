package com.student.app.service;

import com.student.app.model.Student;
import com.student.app.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer — contains business logic.
 * Sits between Controller and Repository.
 */
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // ── CREATE ────────────────────────────────────────────────
    public String createStudent(Student student) {
        int rows = studentRepository.save(student);
        return (rows > 0) ? "Student created successfully." : "Failed to create student.";
    }

    // ── READ ALL ──────────────────────────────────────────────
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // ── READ BY ID ────────────────────────────────────────────
    public Student getStudentById(Integer id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
    }

    // ── UPDATE ────────────────────────────────────────────────
    public String updateStudent(Integer id, Student student) {
        // Verify student exists first
        studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        int rows = studentRepository.update(id, student);
        return (rows > 0) ? "Student updated successfully." : "Failed to update student.";
    }

    // ── DELETE ────────────────────────────────────────────────
    public String deleteStudent(Integer id) {
        // Verify student exists first
        studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        int rows = studentRepository.deleteById(id);
        return (rows > 0) ? "Student deleted successfully." : "Failed to delete student.";
    }
}

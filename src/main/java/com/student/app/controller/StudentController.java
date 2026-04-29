package com.student.app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.student.app.model.Student;
import com.student.app.service.StudentService;

/**
 * Controller layer — exposes REST API endpoints.
 *
 * POST   /students        → Create a student
 * GET    /students        → Get all students
 * GET    /students/{id}   → Get student by ID
 * PUT    /students/{id}   → Update a student
 * DELETE /students/{id}   → Delete a student
 */
@RestController
@RequestMapping("/students")
@CrossOrigin(origins = "*")   
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // ── POST /students ────────────────────────────────────────
    @PostMapping
    public ResponseEntity<?> createStudent(@RequestBody Student student) {
        try {
            String message = studentService.createStudent(student);
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    // ── GET /students ─────────────────────────────────────────
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    // ── GET /students/{id} ────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable Integer id) {
        try {
            Student student = studentService.getStudentById(id);
            return ResponseEntity.ok(student);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ── PUT /students/{id} ────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Integer id,
                                           @RequestBody Student student) {
        try {
            String message = studentService.updateStudent(id, student);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ── DELETE /students/{id} ─────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Integer id) {
        try {
            String message = studentService.deleteStudent(id);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

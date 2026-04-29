package com.student.app.repository;

import com.student.app.model.Student;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Repository layer — handles all database operations using JdbcTemplate.
 * All SQL queries are written manually (no ORM / Hibernate).
 */
@Repository
public class StudentRepository {

    private final JdbcTemplate jdbcTemplate;

    public StudentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ── RowMapper: maps each ResultSet row to a Student object ──
    private final RowMapper<Student> studentRowMapper = new RowMapper<Student>() {
        @Override
        public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Student(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("course")
            );
        }
    };

    // ── CREATE ────────────────────────────────────────────────
    public int save(Student student) {
        String sql = "INSERT INTO students (name, email, course) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql,
                student.getName(),
                student.getEmail(),
                student.getCourse());
    }

    // ── READ ALL ──────────────────────────────────────────────
    public List<Student> findAll() {
        String sql = "SELECT * FROM students ORDER BY id ASC";
        return jdbcTemplate.query(sql, studentRowMapper);
    }

    // ── READ BY ID ────────────────────────────────────────────
    public Optional<Student> findById(Integer id) {
        String sql = "SELECT * FROM students WHERE id = ?";
        List<Student> result = jdbcTemplate.query(sql, studentRowMapper, id);
        return result.stream().findFirst();
    }

    // ── UPDATE ────────────────────────────────────────────────
    public int update(Integer id, Student student) {
        String sql = "UPDATE students SET name = ?, email = ?, course = ? WHERE id = ?";
        return jdbcTemplate.update(sql,
                student.getName(),
                student.getEmail(),
                student.getCourse(),
                id);
    }

    // ── DELETE ────────────────────────────────────────────────
    public int deleteById(Integer id) {
        String sql = "DELETE FROM students WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}

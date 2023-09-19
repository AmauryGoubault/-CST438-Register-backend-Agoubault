package com.cst438.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Student;
import com.cst438.domain.StudentDTO;
import com.cst438.domain.StudentRepository;
import com.google.common.base.Optional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@CrossOrigin
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    /*
     * add a new student
     */
    @PostMapping("")
    @Transactional
    public Student addStudent(@RequestBody Student student) {
        // Check if the student email is unique in the database
        if (studentRepository.findByEmail(student.getEmail()) == null) {
            // Set the student status and save to the database
            student.setStatus("Active");
            student.setStatusCode(0);
            studentRepository.save(student);
            return student;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student with the same email already exists.");
        }
    }

    /*
     * List all students
     */
    @GetMapping("")
    public List<Student> getAllStudents() {
        // Convertir l'itérable en liste en utilisant Java Stream API
        List<Student> studentList = StreamSupport
                .stream(studentRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        return studentList;
    }

    /*
     * Update student status
     */
    @PutMapping("/{studentId}/status")
    public ResponseEntity<Student> updateStudentStatus(@PathVariable("studentId") int studentId, @RequestBody StudentDTO studentDTO) {
        java.util.Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (!studentOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Student student = studentOpt.get();
        student.setStatus(studentDTO.status());
        studentRepository.save(student);
        return ResponseEntity.ok(student);
    }

    /*
     * Delete a student
     */
    @DeleteMapping("/{student_id}")
    @Transactional
    public void deleteStudent(@PathVariable int student_id) {
        Student student = studentRepository.findById(student_id).orElse(null);
        if (student != null) {
            // You can add more logic here as needed
            studentRepository.delete(student);
        } else {
        	throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found.");

        }
    }
}

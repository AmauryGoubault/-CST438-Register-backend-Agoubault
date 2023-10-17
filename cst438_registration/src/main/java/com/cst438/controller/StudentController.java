package com.cst438.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cst438.domain.Student;
import com.cst438.domain.StudentDTO;
import com.cst438.domain.StudentRepository;

import java.util.List;
import java.util.Optional;

@CrossOrigin (origins = "http://localhost:3000")
@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    //Listing all students
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok((List<Student>) studentRepository.findAll());
    }

    //Adding a new student
    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        Student savedStudent = studentRepository.save(student);
        return ResponseEntity.ok(savedStudent);
    }
    
//    -=-=-=-=- old without StudentDTO -=-=-=-=-
//    //Updating the student
//    @PutMapping("/{studentId}/status")
//    public ResponseEntity<Student> updateStudentStatus(@PathVariable("studentId") int studentId, @RequestBody String status) {
//        Optional<Student> studentOpt = studentRepository.findById(studentId);
//        if (!studentOpt.isPresent()) {
//            return ResponseEntity.notFound().build();
//        }
//        Student student = studentOpt.get();
//        student.setStatus(status);
//        studentRepository.save(student);
//        return ResponseEntity.ok(student);
//    }
//    
  //Updating the student status
    @PutMapping("/{studentId}")
    public ResponseEntity<Student> updateStudentStatus(@PathVariable("studentId") int studentId, @RequestBody StudentDTO studentDTO) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (!studentOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Student student = studentOpt.get();
        student.setStatus(studentDTO.status());
        student.setName(studentDTO.name());
        student.setEmail(studentDTO.email());
        studentRepository.save(student);
        return ResponseEntity.ok(student);
    }



    //Deleting a student
    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable("studentId") int studentId) {
        if (!studentRepository.existsById(studentId)) {
            return ResponseEntity.notFound().build();
        }
        studentRepository.deleteById(studentId);
        return ResponseEntity.noContent().build();
    }
}

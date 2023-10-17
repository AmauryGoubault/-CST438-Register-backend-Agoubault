package com.cst438.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cst438.domain.FinalGradeDTO;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentDTO;
import com.cst438.domain.EnrollmentRepository;

@Service
@ConditionalOnProperty(prefix = "gradebook", name = "service", havingValue = "rest")
@RestController
public class GradebookServiceREST implements GradebookService {

	private RestTemplate restTemplate = new RestTemplate();

	@Value("${gradebook.url}")
	private String gradebook_url;

	@Override
	public void enrollStudent(String student_email, String student_name, int course_id) {
	    System.out.println("Start Message " + student_email + " " + course_id); 

	    // Create an instance of EnrollmentDTO using its constructor
	    EnrollmentDTO enrollment = new EnrollmentDTO(0, student_email, student_name, course_id);

	    // Send the POST request to the Gradebook backend using the provided URL and EnrollmentDTO object
	    restTemplate.postForObject(gradebook_url, enrollment, EnrollmentDTO.class);
	}

	
	@Autowired
	EnrollmentRepository enrollmentRepository;
	/*
	 * endpoint for final course grades
	 */
	@PutMapping("/course/{course_id}")
	@Transactional
	public void updateCourseGrades(@RequestBody FinalGradeDTO[] grades, @PathVariable("course_id") int course_id) {
	    System.out.println("Grades received " + grades.length);
	    
	    for (FinalGradeDTO gradeDTO : grades) {
	        // Using the findByEmailAndCourseId method from the EnrollmentRepository to retrieve the enrollment record
	        Enrollment enrollment = enrollmentRepository.findByEmailAndCourseId(gradeDTO.studentEmail(), gradeDTO.courseId());
	        
	        if (enrollment != null) {
	            // Updating the enrollment's courseGrade using the grade from the FinalGradeDTO
	            enrollment.setCourseGrade(gradeDTO.grade());
	            enrollmentRepository.save(enrollment);
	        } else {
	            System.out.println("No enrollment record found for student: " + gradeDTO.studentEmail() + " in course: " + gradeDTO.courseId());
	        }
	    }
	}

}

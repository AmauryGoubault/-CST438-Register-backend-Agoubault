package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cst438.domain.Student;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class JunitTestStudentController {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testGetAllStudents() throws Exception {
        MockHttpServletResponse response;

        response = mvc.perform(
                MockMvcRequestBuilders
                        .get("/students")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        Student[] students = fromJsonString(response.getContentAsString(), Student[].class);
        assertNotNull(students);
    }

    @Test
    public void testAddStudent() throws Exception {
        MockHttpServletResponse response;

        Student student = new Student();
        student.setName("John");
        student.setEmail("john@example.com");
        student.setStatus("Active");
        student.setStatusCode(0);

        response = mvc.perform(
                MockMvcRequestBuilders
                        .post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(student))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        Student result = fromJsonString(response.getContentAsString(), Student.class);
        assertNotNull(result);
        assertNotNull(result.getStudent_id());
    }

    @Test
    public void testUpdateStudentStatus() throws Exception {
        MockHttpServletResponse response;

        int studentId = 2;
        String newStatus = "active";

        response = mvc.perform(
                MockMvcRequestBuilders
                        .put("/students/" + studentId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"" + newStatus + "\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        Student result = fromJsonString(response.getContentAsString(), Student.class);
        assertEquals(newStatus, result.getStatus());
    }


    @Test
    public void testDeleteStudent() throws Exception {
        MockHttpServletResponse response;

        int studentId = 1;

        response = mvc.perform(
                MockMvcRequestBuilders
                        .delete("/students/" + studentId)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(204, response.getStatus());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> T fromJsonString(String str, Class<T> valueType) {
        try {
            return new ObjectMapper().readValue(str, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

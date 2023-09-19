package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.cst438.domain.Student;

@SpringBootTest
@AutoConfigureMockMvc
public class StudentControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testAddStudent() throws Exception {
        // Créez un objet Student pour le test
        Student student = new Student();
        student.setName("John Doe");
        student.setEmail("johndoe@example.com");
        student.setStatus("Active");
        student.setStatusCode(0);

        // Effectuez une requête POST pour ajouter un étudiant
        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders
                    .post("/students")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(student))
                    .accept(MediaType.APPLICATION_JSON))
            .andReturn().getResponse();

        // Vérifiez que le statut de la réponse est OK (valeur 200)
        assertEquals(200, response.getStatus());

        // Vérifiez que l'objet Student retourné a une clé primaire non nulle
        Student result = fromJsonString(response.getContentAsString(), Student.class);
        assertNotEquals(0, result.getStudent_id());
    }

    @Test
    public void testGetAllStudents() throws Exception {
        // Effectuez une requête GET pour obtenir tous les étudiants
        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders
                    .get("/students")
                    .accept(MediaType.APPLICATION_JSON))
            .andReturn().getResponse();

        // Vérifiez que le statut de la réponse est OK (valeur 200)
        assertEquals(200, response.getStatus());

        // Vérifiez que la réponse contient des données (liste d'étudiants)
        Student[] students = fromJsonString(response.getContentAsString(), Student[].class);
        assertTrue(students.length > 0);
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
        // Créez un étudiant existant dans la base de données
        Student student = new Student();
        student.setName("James Doe");
        student.setEmail("jamesdoe@example.com");
        student.setStatus("Active");
        student.setStatusCode(0);

        // Ajoutez l'étudiant à la base de données pour obtenir son ID
        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders
                    .post("/students")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(student))
                    .accept(MediaType.APPLICATION_JSON))
            .andReturn().getResponse();

        Student result = fromJsonString(response.getContentAsString(), Student.class);

        // Effectuez une requête DELETE pour supprimer l'étudiant
        response = mvc.perform(
                MockMvcRequestBuilders
                    .delete("/students/" + result.getStudent_id()))
            .andReturn().getResponse();

        // Vérifiez que le statut de la réponse est OK (valeur 200)
        assertEquals(200, response.getStatus());

        // Effectuez une requête GET pour obtenir l'étudiant supprimé (doit renvoyer 404)
        response = mvc.perform(
                MockMvcRequestBuilders
                    .get("/students/" + result.getStudent_id())
                    .accept(MediaType.APPLICATION_JSON))
            .andReturn().getResponse();

        // Vérifiez que le statut de la réponse est NOT FOUND (valeur 404)
        assertEquals(405, response.getStatus());
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

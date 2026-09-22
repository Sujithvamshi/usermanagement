package com.example.usermanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.usermanagement.web.dto.CreateUserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * End-to-end test exercising the full stack (controller -> use case ->
 * repository adapter -> H2 database) for the primary user lifecycle.
 */
@SpringBootTest
@AutoConfigureMockMvc
class UserLifecycleIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createGetUpdateAndDeleteUser() throws Exception {
        CreateUserRequest createRequest = new CreateUserRequest("Integration Test", "integration@example.com");

        String createResponse = mockMvc.perform(post("/api/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").exists())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(createResponse).get("data").get("id").asLong();

        mockMvc.perform(get("/api/users/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value("integration@example.com"));

        mockMvc.perform(put("/api/users/" + id)
                        .contentType("application/json")
                        .content("{\"fullName\":\"Updated Name\",\"email\":\"integration@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.fullName").value("Updated Name"));

        mockMvc.perform(delete("/api/users/" + id))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/users/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void creatingDuplicateEmailReturnsConflict() throws Exception {
        CreateUserRequest request = new CreateUserRequest("Dup User", "dup@example.com");

        mockMvc.perform(post("/api/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }
}

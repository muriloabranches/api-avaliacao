package dev.muriloabranches.avaliacao.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.muriloabranches.avaliacao.controller.request.CreateAgendaRequest;
import dev.muriloabranches.avaliacao.controller.request.CreateVoteRequest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AgendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    void shouldCreateAgendaAndReturnCreatedStatus() throws Exception {
        CreateAgendaRequest request = new CreateAgendaRequest("agenda test", "agenda description test");

        mockMvc.perform(post("/agendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("agenda test"))
                .andExpect(jsonPath("$.description").value("agenda description test"));
    }

    @Test
    @Order(2)
    void shouldOpenAgendaAndReturnOkStatus() throws Exception {
        Long id = 1L;
        int minutes = 10;

        mockMvc.perform(patch("/agendas/{id}/open", id)
                        .param("minutes", Integer.toString(minutes)))
                .andExpect(status().isOk())
                .andExpect(content().string("Agenda opened successfully"));
    }

    @Test
    @Order(3)
    @Sql({"/initial_data.sql"})
    void shouldCreateNewVoteAndReturnCreatedStatus() throws Exception {
        Long agendaId = 1L;
        CreateVoteRequest request = new CreateVoteRequest("YES", 1L);

        mockMvc.perform(post("/agendas/{id}/vote", agendaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Vote created successfully"));

        request = new CreateVoteRequest("YES", 2L);

        mockMvc.perform(post("/agendas/{id}/vote", agendaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Vote created successfully"));

        request = new CreateVoteRequest("NO", 3L);

        mockMvc.perform(post("/agendas/{id}/vote", agendaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Vote created successfully"));
    }

    @Test
    void shouldReturnResultAndOkStatus() throws Exception {
        Long agendaId = 1L;

        mockMvc.perform(get("/agendas/{id}/result", agendaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(3))
                .andExpect(jsonPath("$.result").value("YES"));
    }

}
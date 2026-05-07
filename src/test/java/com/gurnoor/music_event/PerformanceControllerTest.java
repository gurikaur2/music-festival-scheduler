package com.gurnoor.music_event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gurnoor.music_event.model.Performance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PerformanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    // Helper method to build a performance JSON string
    private String buildPerformanceJson(String date, String start, String end) throws Exception {
        Performance p = Performance.builder()
                .date(LocalDate.parse(date))
                .startTime(LocalTime.parse(start))
                .endTime(LocalTime.parse(end))
                .build();
        return objectMapper.writeValueAsString(p);
    }

    // ─────────────────────────────────────────────
    // TEST 1: Create performance successfully — expects 201
    // ─────────────────────────────────────────────
    @Test
    @WithMockUser(roles = "ORGANIZER")
    void createPerformance_Returns201() throws Exception {
        String body = buildPerformanceJson("2025-08-01", "10:00", "12:00");

        mockMvc.perform(post("/api/performances/artist/1/stage/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated());
    }

    // ─────────────────────────────────────────────
    // TEST 2: Stage overlap — expects 400
    // ─────────────────────────────────────────────
    @Test
    @WithMockUser(roles = "ORGANIZER")
    void createPerformance_StageOverlap_Returns400() throws Exception {
        String body = buildPerformanceJson("2025-08-01", "10:00", "12:00");

        // First booking — should succeed
        mockMvc.perform(post("/api/performances/artist/1/stage/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated());

        // Second booking on same stage, overlapping time — should fail
        String conflictBody = buildPerformanceJson("2025-08-01", "11:00", "13:00");

        mockMvc.perform(post("/api/performances/artist/2/stage/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(conflictBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    // ─────────────────────────────────────────────
    // TEST 3: Artist double-booking — expects 400
    // ─────────────────────────────────────────────
    @Test
    @WithMockUser(roles = "ORGANIZER")
    void createPerformance_ArtistDoubleBooking_Returns400() throws Exception {
        String body = buildPerformanceJson("2025-08-01", "10:00", "12:00");

        // Book artist 1 on stage 1
        mockMvc.perform(post("/api/performances/artist/1/stage/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated());

        // Try to book same artist 1 on stage 2 at overlapping time
        String conflictBody = buildPerformanceJson("2025-08-01", "11:00", "13:00");

        mockMvc.perform(post("/api/performances/artist/1/stage/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(conflictBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    // ─────────────────────────────────────────────
    // TEST 4: Different stages, same time — expects 201 (no conflict)
    // ─────────────────────────────────────────────
    @Test
    @WithMockUser(roles = "ORGANIZER")
    void createPerformance_DifferentStages_SameTime_Returns201() throws Exception {
        String body = buildPerformanceJson("2025-08-01", "10:00", "12:00");

        // Artist 1 on stage 1
        mockMvc.perform(post("/api/performances/artist/1/stage/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated());

        // Artist 2 on stage 2 — same time, different stage — should succeed
        mockMvc.perform(post("/api/performances/artist/2/stage/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated());
    }

    // ─────────────────────────────────────────────
    // TEST 5: Delete non-existent performance — expects 404
    // ─────────────────────────────────────────────
    @Test
    @WithMockUser(roles = "ORGANIZER")
    void deletePerformance_NotFound_Returns404() throws Exception {
        mockMvc.perform(delete("/api/performances/999"))
                .andExpect(status().isNotFound());
    }

    // ─────────────────────────────────────────────
    // TEST 6: Delete existing performance — expects 204
    // ─────────────────────────────────────────────
    @Test
    @WithMockUser(roles = "ORGANIZER")
    void deletePerformance_Success_Returns204() throws Exception {
        // First create one
        String body = buildPerformanceJson("2025-08-01", "10:00", "12:00");
        mockMvc.perform(post("/api/performances/artist/1/stage/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated());

        // Then delete it — ID will be 6 because seeder adds 5 artists/stages
        // Use GET first to find the actual ID
        mockMvc.perform(delete("/api/performances/1"))
                .andExpect(status().isNoContent());
    }

    // ─────────────────────────────────────────────
    // TEST 7: Get schedule by date — expects 200
    // ─────────────────────────────────────────────
    @Test
    @WithMockUser(roles = "VIEWER")
    void getScheduleByDate_Returns200() throws Exception {
        mockMvc.perform(get("/schedule/2025-08-01"))
                .andExpect(status().isOk());
    }

    // ─────────────────────────────────────────────
    // TEST 8: Viewer cannot create performance — expects 403
    // ─────────────────────────────────────────────
    @Test
    @WithMockUser(roles = "VIEWER")
    void viewerCannotCreate_Returns403() throws Exception {
        String body = buildPerformanceJson("2025-08-01", "10:00", "12:00");

        mockMvc.perform(post("/api/performances/artist/1/stage/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isForbidden());
    }

    // ─────────────────────────────────────────────
    // TEST 9: Get all artists — expects 200
    // ─────────────────────────────────────────────
    @Test
    @WithMockUser(roles = "VIEWER")
    void getAllArtists_Returns200() throws Exception {
        mockMvc.perform(get("/api/artists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    // ─────────────────────────────────────────────
    // TEST 10: Get all stages — expects 200
    // ─────────────────────────────────────────────
    @Test
    @WithMockUser(roles = "VIEWER")
    void getAllStages_Returns200() throws Exception {
        mockMvc.perform(get("/api/stages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
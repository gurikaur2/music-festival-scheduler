package com.gurnoor.music_event;

import com.gurnoor.music_event.exception.ConflictException;
import com.gurnoor.music_event.model.Artist;
import com.gurnoor.music_event.model.Performance;
import com.gurnoor.music_event.model.Stage;
import com.gurnoor.music_event.repository.ArtistRepository;
import com.gurnoor.music_event.repository.PerformanceRepository;
import com.gurnoor.music_event.repository.StageRepository;
import com.gurnoor.music_event.service.PerformanceService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConflictDetectionTest {

    @Mock
    private PerformanceRepository performanceRepository;
    @Mock
    private ArtistRepository artistRepository;
    @Mock
    private StageRepository stageRepository;

    @InjectMocks
    private PerformanceService performanceService;

    private Artist artist;
    private Stage stage;

    @BeforeEach
    void setUp() {
        artist = Artist.builder().id(1L).name("Coldplay").genre("Rock").build();
        stage  = Stage.builder().id(1L).name("Main Stage").location("North").build();
    }

    private Performance buildPerformance(int startHour, int endHour) {
        return Performance.builder()
                .date(LocalDate.of(2025, 8, 1))
                .startTime(LocalTime.of(startHour, 0))
                .endTime(LocalTime.of(endHour, 0))
                .build();
    }

    // ─────────────────────────────────────────────
    // TEST 1: Exact same time — conflict
    // ─────────────────────────────────────────────
    @Test
    void exactSameTime_ThrowsConflict() {
        Performance existing = buildPerformance(10, 12);
        existing.setArtist(artist);
        existing.setStage(stage);

        when(artistRepository.findById(1L)).thenReturn(Optional.of(artist));
        when(stageRepository.findById(1L)).thenReturn(Optional.of(stage));
        when(performanceRepository.findOverlappingOnStage(any(), any(), any(), any()))
                .thenReturn(List.of(existing));

        Performance newPerf = buildPerformance(10, 12);
        assertThrows(ConflictException.class,
                () -> performanceService.createPerformance(1L, 1L, newPerf));
    }

    // ─────────────────────────────────────────────
    // TEST 2: Partial overlap — conflict
    // ─────────────────────────────────────────────
    @Test
    void partialOverlap_ThrowsConflict() {
        Performance existing = buildPerformance(10, 12);
        existing.setArtist(artist);
        existing.setStage(stage);

        when(artistRepository.findById(1L)).thenReturn(Optional.of(artist));
        when(stageRepository.findById(1L)).thenReturn(Optional.of(stage));
        when(performanceRepository.findOverlappingOnStage(any(), any(), any(), any()))
                .thenReturn(List.of(existing));

        // New performance starts at 11 — overlaps with 10-12
        Performance newPerf = buildPerformance(11, 13);
        assertThrows(ConflictException.class,
                () -> performanceService.createPerformance(1L, 1L, newPerf));
    }

    // ─────────────────────────────────────────────
    // TEST 3: Back-to-back — NO conflict (10-12 then 12-14)
    // ─────────────────────────────────────────────
    @Test
    void backToBack_NoConflict() {
        when(artistRepository.findById(1L)).thenReturn(Optional.of(artist));
        when(stageRepository.findById(1L)).thenReturn(Optional.of(stage));
        when(performanceRepository.findOverlappingOnStage(any(), any(), any(), any()))
                .thenReturn(Collections.emptyList()); // no overlap
        when(performanceRepository.findOverlappingForArtist(any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        Performance existing = buildPerformance(10, 12);
        existing.setArtist(artist);
        existing.setStage(stage);
        when(performanceRepository.save(any())).thenReturn(existing);

        // 12-14 starts exactly when 10-12 ends — should be fine
        Performance newPerf = buildPerformance(12, 14);
        assertDoesNotThrow(
                () -> performanceService.createPerformance(1L, 1L, newPerf));
    }

    // ─────────────────────────────────────────────
    // TEST 4: Completely before — NO conflict
    // ─────────────────────────────────────────────
    @Test
    void completelyBefore_NoConflict() {
        when(artistRepository.findById(1L)).thenReturn(Optional.of(artist));
        when(stageRepository.findById(1L)).thenReturn(Optional.of(stage));
        when(performanceRepository.findOverlappingOnStage(any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(performanceRepository.findOverlappingForArtist(any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        Performance perf = buildPerformance(10, 12);
        perf.setArtist(artist);
        perf.setStage(stage);
        when(performanceRepository.save(any())).thenReturn(perf);

        // 8-10 ends before 10-12 starts — no conflict
        Performance newPerf = buildPerformance(8, 10);
        assertDoesNotThrow(
                () -> performanceService.createPerformance(1L, 1L, newPerf));
    }

    // ─────────────────────────────────────────────
    // TEST 5: Completely after — NO conflict
    // ─────────────────────────────────────────────
    @Test
    void completelyAfter_NoConflict() {
        when(artistRepository.findById(1L)).thenReturn(Optional.of(artist));
        when(stageRepository.findById(1L)).thenReturn(Optional.of(stage));
        when(performanceRepository.findOverlappingOnStage(any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(performanceRepository.findOverlappingForArtist(any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        Performance perf = buildPerformance(14, 16);
        perf.setArtist(artist);
        perf.setStage(stage);
        when(performanceRepository.save(any())).thenReturn(perf);

        // 14-16 starts after 10-12 ends — no conflict
        Performance newPerf = buildPerformance(14, 16);
        assertDoesNotThrow(
                () -> performanceService.createPerformance(1L, 1L, newPerf));
    }
}
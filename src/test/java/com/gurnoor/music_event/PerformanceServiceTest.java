package com.gurnoor.music_event;

import com.gurnoor.music_event.exception.ConflictException;
import com.gurnoor.music_event.exception.ResourceNotFoundException;
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
public class PerformanceServiceTest {

    // These are FAKE/MOCK versions of the repositories
    // They don't touch any real database
    @Mock
    private PerformanceRepository performanceRepository;

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private StageRepository stageRepository;

    // This is the REAL service we are testing
    // Mockito will inject the mock repos into it
    @InjectMocks
    private PerformanceService performanceService;

    // Sample data we will reuse across tests
    private Artist sampleArtist;
    private Stage sampleStage;
    private Performance samplePerformance;

    // This runs BEFORE each test to set up fresh sample data
    @BeforeEach
    void setUp() {
        sampleArtist = Artist.builder()
                .id(1L)
                .name("Coldplay")
                .genre("Rock")
                .build();

        sampleStage = Stage.builder()
                .id(1L)
                .name("Main Stage")
                .location("North Lawn")
                .build();

        samplePerformance = Performance.builder()
                .id(1L)
                .date(LocalDate.of(2025, 8, 1))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(12, 0))
                .artist(sampleArtist)
                .stage(sampleStage)
                .build();
    }

    // ─────────────────────────────────────────────
    // TEST 1: Successfully create a performance
    // ─────────────────────────────────────────────
    @Test
    void createPerformance_Success() {
        // ARRANGE — tell mocks what to return
        when(artistRepository.findById(1L))
                .thenReturn(Optional.of(sampleArtist));
        when(stageRepository.findById(1L))
                .thenReturn(Optional.of(sampleStage));
        when(performanceRepository.findOverlappingOnStage(any(), any(), any(), any()))
                .thenReturn(Collections.emptyList()); // no conflicts
        when(performanceRepository.findOverlappingForArtist(any(), any(), any(), any()))
                .thenReturn(Collections.emptyList()); // no conflicts
        when(performanceRepository.save(any(Performance.class)))
                .thenReturn(samplePerformance);

        // ACT — call the actual method
        Performance result = performanceService.createPerformance(
                1L, 1L, samplePerformance);

        // ASSERT — check the result is correct
        assertNotNull(result);
        assertEquals("Coldplay", result.getArtist().getName());
        assertEquals("Main Stage", result.getStage().getName());
        assertEquals(LocalTime.of(10, 0), result.getStartTime());

        // Verify save was called exactly once
        verify(performanceRepository, times(1)).save(any(Performance.class));
    }

    // ─────────────────────────────────────────────
    // TEST 2: Stage conflict — same stage, overlapping time
    // ─────────────────────────────────────────────
    @Test
    void createPerformance_StageConflict_ThrowsException() {
        // ARRANGE
        when(artistRepository.findById(1L))
                .thenReturn(Optional.of(sampleArtist));
        when(stageRepository.findById(1L))
                .thenReturn(Optional.of(sampleStage));

        // Simulate: there IS already a performance on this stage at this time
        when(performanceRepository.findOverlappingOnStage(any(), any(), any(), any()))
                .thenReturn(List.of(samplePerformance)); // conflict found!

        // ACT + ASSERT — expect ConflictException to be thrown
        ConflictException exception = assertThrows(
                ConflictException.class,
                () -> performanceService.createPerformance(1L, 1L, samplePerformance)
        );

        // Check the error message
        assertTrue(exception.getMessage().contains("Main Stage"));

        // Verify save was NEVER called because conflict was detected
        verify(performanceRepository, never()).save(any());
    }

    // ─────────────────────────────────────────────
    // TEST 3: Artist double-booking — same artist, overlapping time
    // ─────────────────────────────────────────────
    @Test
    void createPerformance_ArtistDoubleBooking_ThrowsException() {
        // ARRANGE
        when(artistRepository.findById(1L))
                .thenReturn(Optional.of(sampleArtist));
        when(stageRepository.findById(1L))
                .thenReturn(Optional.of(sampleStage));

        // Stage has no conflict
        when(performanceRepository.findOverlappingOnStage(any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        // But artist IS already booked somewhere else
        when(performanceRepository.findOverlappingForArtist(any(), any(), any(), any()))
                .thenReturn(List.of(samplePerformance)); // artist conflict!

        // ACT + ASSERT
        ConflictException exception = assertThrows(
                ConflictException.class,
                () -> performanceService.createPerformance(1L, 1L, samplePerformance)
        );

        assertTrue(exception.getMessage().contains("Coldplay"));
        verify(performanceRepository, never()).save(any());
    }

    // ─────────────────────────────────────────────
    // TEST 4: Two different artists on different stages at same time — should SUCCEED
    // ─────────────────────────────────────────────
    @Test
    void createPerformance_DifferentStages_SameTime_NoConflict() {
        // ARRANGE
        Artist artist2 = Artist.builder()
                .id(2L).name("Dua Lipa").genre("Pop").build();
        Stage stage2 = Stage.builder()
                .id(2L).name("Acoustic Stage").location("East Garden").build();
        Performance performance2 = Performance.builder()
                .id(2L)
                .date(LocalDate.of(2025, 8, 1))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(12, 0))
                .artist(artist2)
                .stage(stage2)
                .build();

        when(artistRepository.findById(2L)).thenReturn(Optional.of(artist2));
        when(stageRepository.findById(2L)).thenReturn(Optional.of(stage2));
        when(performanceRepository.findOverlappingOnStage(any(), any(), any(), any()))
                .thenReturn(Collections.emptyList()); // different stage, no conflict
        when(performanceRepository.findOverlappingForArtist(any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(performanceRepository.save(any())).thenReturn(performance2);

        // ACT
        Performance result = performanceService.createPerformance(2L, 2L, performance2);

        // ASSERT — should work fine
        assertNotNull(result);
        assertEquals("Dua Lipa", result.getArtist().getName());
        assertEquals("Acoustic Stage", result.getStage().getName());
    }

    // ─────────────────────────────────────────────
    // TEST 5: Artist not found — throws ResourceNotFoundException
    // ─────────────────────────────────────────────
    @Test
    void createPerformance_ArtistNotFound_ThrowsException() {
        // ARRANGE — artist ID 99 does not exist
        when(artistRepository.findById(99L))
                .thenReturn(Optional.empty());

        // ACT + ASSERT
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> performanceService.createPerformance(99L, 1L, samplePerformance)
        );

        assertTrue(exception.getMessage().contains("99"));
        verify(performanceRepository, never()).save(any());
    }

    // ─────────────────────────────────────────────
    // TEST 6: Stage not found — throws ResourceNotFoundException
    // ─────────────────────────────────────────────
    @Test
    void createPerformance_StageNotFound_ThrowsException() {
        when(artistRepository.findById(1L))
                .thenReturn(Optional.of(sampleArtist));
        when(stageRepository.findById(99L))
                .thenReturn(Optional.empty()); // stage doesn't exist

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> performanceService.createPerformance(1L, 99L, samplePerformance)
        );

        assertTrue(exception.getMessage().contains("99"));
    }

    // ─────────────────────────────────────────────
    // TEST 7: Delete existing performance — success
    // ─────────────────────────────────────────────
    @Test
    void deletePerformance_Success() {
        when(performanceRepository.existsById(1L)).thenReturn(true);

        // Should not throw any exception
        assertDoesNotThrow(() -> performanceService.deletePerformance(1L));

        // Verify deleteById was called
        verify(performanceRepository, times(1)).deleteById(1L);
    }

    // ─────────────────────────────────────────────
    // TEST 8: Delete non-existent performance — throws exception
    // ─────────────────────────────────────────────
    @Test
    void deletePerformance_NotFound_ThrowsException() {
        when(performanceRepository.existsById(999L)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> performanceService.deletePerformance(999L)
        );

        assertTrue(exception.getMessage().contains("999"));
        verify(performanceRepository, never()).deleteById(any());
    }

    // ─────────────────────────────────────────────
    // TEST 9: Get all performances
    // ─────────────────────────────────────────────
    @Test
    void getAllPerformances_ReturnsList() {
        when(performanceRepository.findAll())
                .thenReturn(List.of(samplePerformance));

        List<Performance> result = performanceService.getAllPerformances();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Coldplay", result.get(0).getArtist().getName());
    }

    // ─────────────────────────────────────────────
    // TEST 10: Get performances by date
    // ─────────────────────────────────────────────
    @Test
    void getPerformancesByDate_ReturnsSortedList() {
        LocalDate date = LocalDate.of(2025, 8, 1);
        when(performanceRepository.findByDateOrderByStartTimeAsc(date))
                .thenReturn(List.of(samplePerformance));

        List<Performance> result = performanceService.getPerformancesByDate(date);

        assertEquals(1, result.size());
        assertEquals(date, result.get(0).getDate());
    }
}
package com.gurnoor.music_event.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gurnoor.music_event.exception.ConflictException;
import com.gurnoor.music_event.exception.ResourceNotFoundException;
import com.gurnoor.music_event.model.Artist;
import com.gurnoor.music_event.model.Performance;
import com.gurnoor.music_event.model.Stage;
import com.gurnoor.music_event.repository.ArtistRepository;
import com.gurnoor.music_event.repository.PerformanceRepository;
import com.gurnoor.music_event.repository.StageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PerformanceService {

    private final PerformanceRepository performanceRepository;
    private final ArtistRepository artistRepository;
    private final StageRepository stageRepository;

    public Performance createPerformance(Long artistId, Long stageId,
                                          Performance performance) {
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found: " + artistId));

        Stage stage = stageRepository.findById(stageId)
                .orElseThrow(() -> new ResourceNotFoundException("Stage not found: " + stageId));

        // Check stage overlap
        List<Performance> stageConflicts = performanceRepository.findOverlappingOnStage(
                stageId, performance.getDate(),
                performance.getStartTime(), performance.getEndTime());
        if (!stageConflicts.isEmpty()) {
            throw new ConflictException("Stage '" + stage.getName() +
                    "' already has a performance at this time.");
        }

        // Check artist double-booking
        List<Performance> artistConflicts = performanceRepository.findOverlappingForArtist(
                artistId, performance.getDate(),
                performance.getStartTime(), performance.getEndTime());
        if (!artistConflicts.isEmpty()) {
            throw new ConflictException("Artist '" + artist.getName() +
                    "' is already booked at this time.");
        }

        performance.setArtist(artist);
        performance.setStage(stage);
        return performanceRepository.save(performance);
    }

    public Performance updatePerformance(Long id, Long artistId, Long stageId,
                                          Performance updated) {
        Performance existing = performanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Performance not found: " + id));

        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found: " + artistId));

        Stage stage = stageRepository.findById(stageId)
                .orElseThrow(() -> new ResourceNotFoundException("Stage not found: " + stageId));

        // Check stage overlap excluding current
        List<Performance> stageConflicts = performanceRepository.findOverlappingOnStageExcluding(
                stageId, updated.getDate(),
                updated.getStartTime(), updated.getEndTime(), id);
        if (!stageConflicts.isEmpty()) {
            throw new ConflictException("Stage conflict detected after update.");
        }

        // Check artist overlap excluding current
        List<Performance> artistConflicts = performanceRepository.findOverlappingForArtistExcluding(
                artistId, updated.getDate(),
                updated.getStartTime(), updated.getEndTime(), id);
        if (!artistConflicts.isEmpty()) {
            throw new ConflictException("Artist double-booking detected after update.");
        }

        existing.setDate(updated.getDate());
        existing.setStartTime(updated.getStartTime());
        existing.setEndTime(updated.getEndTime());
        existing.setArtist(artist);
        existing.setStage(stage);
        return performanceRepository.save(existing);
    }

    public void deletePerformance(Long id) {
        if (!performanceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Performance not found: " + id);
        }
        performanceRepository.deleteById(id);
    }

    public List<Performance> getAllPerformances() {
        return performanceRepository.findAll();
    }

    public List<Performance> getPerformancesByDate(LocalDate date) {
        return performanceRepository.findByDateOrderByStartTimeAsc(date);
    }

    public List<Performance> getPerformancesByStage(Long stageId) {
        return performanceRepository.findByStageIdOrderByDateAscStartTimeAsc(stageId);
    }

    // Domain feature: grouped by stage for a given date
    public Map<String, List<Performance>> getScheduleByDate(LocalDate date) {
        List<Performance> performances = performanceRepository.findByDateOrderByStartTimeAsc(date);
        return performances.stream()
                .collect(Collectors.groupingBy(p -> p.getStage().getName()));
    }
}
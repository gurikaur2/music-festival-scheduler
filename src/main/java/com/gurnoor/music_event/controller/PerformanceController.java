package com.gurnoor.music_event.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gurnoor.music_event.model.Performance;
import com.gurnoor.music_event.service.PerformanceService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/performances")
@RequiredArgsConstructor
public class PerformanceController {

    private final PerformanceService performanceService;

    @GetMapping
    public ResponseEntity<List<Performance>> getAll() {
        return ResponseEntity.ok(performanceService.getAllPerformances());
    }

    @GetMapping("/day")
    public ResponseEntity<List<Performance>> getByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(performanceService.getPerformancesByDate(date));
    }

    @GetMapping("/stage/{stageId}")
    public ResponseEntity<List<Performance>> getByStage(@PathVariable Long stageId) {
        return ResponseEntity.ok(performanceService.getPerformancesByStage(stageId));
    }

    @PostMapping("/artist/{artistId}/stage/{stageId}")
    public ResponseEntity<Performance> create(
            @PathVariable Long artistId,
            @PathVariable Long stageId,
            @RequestBody Performance performance) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(performanceService.createPerformance(artistId, stageId, performance));
    }

    @PutMapping("/{id}/artist/{artistId}/stage/{stageId}")
    public ResponseEntity<Performance> update(
            @PathVariable Long id,
            @PathVariable Long artistId,
            @PathVariable Long stageId,
            @RequestBody Performance performance) {
        return ResponseEntity.ok(
                performanceService.updatePerformance(id, artistId, stageId, performance));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        performanceService.deletePerformance(id);
        return ResponseEntity.noContent().build();
    }
}

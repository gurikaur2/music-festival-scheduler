package com.gurnoor.music_event.controller;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gurnoor.music_event.model.Performance;
import com.gurnoor.music_event.service.PerformanceService;
import com.gurnoor.music_event.service.StageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final PerformanceService performanceService;
    private final StageService stageService;

    // GET /schedule/2025-08-01 → all performances grouped by stage
    @GetMapping("/{date}")
    public ResponseEntity<Map<String, List<Performance>>> getScheduleByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(performanceService.getScheduleByDate(date));
    }

    // GET /schedule/stages → each stage and its full lineup
    @GetMapping("/stages")
    public ResponseEntity<Map<String, List<Performance>>> getScheduleByStages() {
        Map<String, List<Performance>> result = new LinkedHashMap<>();
        stageService.getAllStages().forEach(stage -> {
            List<Performance> lineup =
                    performanceService.getPerformancesByStage(stage.getId());
            result.put(stage.getName(), lineup);
        });
        return ResponseEntity.ok(result);
    }
}
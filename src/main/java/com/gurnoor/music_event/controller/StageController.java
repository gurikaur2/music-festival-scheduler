package com.gurnoor.music_event.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gurnoor.music_event.model.Stage;
import com.gurnoor.music_event.service.StageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stages")
@RequiredArgsConstructor
public class StageController {

    private final StageService stageService;

    @GetMapping
    public ResponseEntity<List<Stage>> getAll() {
        return ResponseEntity.ok(stageService.getAllStages());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stage> getById(@PathVariable Long id) {
        return ResponseEntity.ok(stageService.getStageById(id));
    }

    @PostMapping
    public ResponseEntity<Stage> create(@RequestBody Stage stage) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(stageService.createStage(stage));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Stage> update(@PathVariable Long id,
                                         @RequestBody Stage stage) {
        return ResponseEntity.ok(stageService.updateStage(id, stage));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        stageService.deleteStage(id);
        return ResponseEntity.noContent().build();
    }
}
package com.gurnoor.music_event.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gurnoor.music_event.exception.ResourceNotFoundException;
import com.gurnoor.music_event.model.Stage;
import com.gurnoor.music_event.repository.StageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StageService {

    private final StageRepository stageRepository;

    public List<Stage> getAllStages() {
        return stageRepository.findAll();
    }

    public Stage getStageById(Long id) {
        return stageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stage not found with id: " + id));
    }

    public Stage createStage(Stage stage) {
        return stageRepository.save(stage);
    }

    public Stage updateStage(Long id, Stage updated) {
        Stage existing = getStageById(id);
        existing.setName(updated.getName());
        existing.setLocation(updated.getLocation());
        return stageRepository.save(existing);
    }

    public void deleteStage(Long id) {
        getStageById(id);
        stageRepository.deleteById(id);
    }
}
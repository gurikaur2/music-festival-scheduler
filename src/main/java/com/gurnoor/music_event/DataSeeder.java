package com.gurnoor.music_event;

import com.gurnoor.music_event.model.Artist;
import com.gurnoor.music_event.model.Stage;
import com.gurnoor.music_event.repository.ArtistRepository;
import com.gurnoor.music_event.repository.StageRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final ArtistRepository artistRepository;
    private final StageRepository stageRepository;

    @Override
    public void run(String... args) {
        stageRepository.save(Stage.builder()
                .name("Main Stage").location("North Lawn").build());
        stageRepository.save(Stage.builder()
                .name("Acoustic Stage").location("East Garden").build());
        stageRepository.save(Stage.builder()
                .name("Electronic Stage").location("West Dome").build());

        artistRepository.save(Artist.builder()
                .name("Coldplay").genre("Alternative Rock").build());
        artistRepository.save(Artist.builder()
                .name("Dua Lipa").genre("Pop").build());
        artistRepository.save(Artist.builder()
                .name("Martin Garrix").genre("EDM").build());
        artistRepository.save(Artist.builder()
                .name("Arijit Singh").genre("Bollywood").build());

        log.info("====== Festival Scheduler Started — Sample data loaded ======");
    }
}
package com.gurnoor.music_event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gurnoor.music_event.model.Artist;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

}


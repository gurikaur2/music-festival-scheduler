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

import com.gurnoor.music_event.model.Artist;
import com.gurnoor.music_event.service.ArtistService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/artists")
@RequiredArgsConstructor
public class ArtistController {
	 private final ArtistService artistService;

	    @GetMapping
	    public ResponseEntity<List<Artist>> getAll() {
	        return ResponseEntity.ok(artistService.getAllArtists());
	    }

	    @GetMapping("/{id}")
	    public ResponseEntity<Artist> getById(@PathVariable Long id) {
	        return ResponseEntity.ok(artistService.getArtistById(id));
	    }

	    @PostMapping
	    public ResponseEntity<Artist> create(@RequestBody Artist artist) {
	        return ResponseEntity.status(HttpStatus.CREATED)
	                .body(artistService.createArtist(artist));
	    }

	    @PutMapping("/{id}")
	    public ResponseEntity<Artist> update(@PathVariable Long id,
	                                          @RequestBody Artist artist) {
	        return ResponseEntity.ok(artistService.updateArtist(id, artist));
	    }

	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> delete(@PathVariable Long id) {
	        artistService.deleteArtist(id);
	        return ResponseEntity.noContent().build();
	    }
}
package com.gurnoor.music_event.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gurnoor.music_event.exception.ResourceNotFoundException;
import com.gurnoor.music_event.model.Artist;
import com.gurnoor.music_event.repository.ArtistRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArtistService {
	private final ArtistRepository artistRepository;
	
	public List<Artist> getAllArtists()
	{
		return artistRepository.findAll();
	}
	
	public Artist getArtistById(Long id) {
        return artistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found with id: " + id));
    }
	
	public Artist createArtist(Artist artist) {
        return artistRepository.save(artist);
    }
	
	public Artist updateArtist(Long id, Artist updated) {
        Artist existing = getArtistById(id);
        existing.setName(updated.getName());
        existing.setGenre(updated.getGenre());
        return artistRepository.save(existing);
    }
	
	 public void deleteArtist(Long id) {
	        getArtistById(id);
	        artistRepository.deleteById(id);
	    }

}

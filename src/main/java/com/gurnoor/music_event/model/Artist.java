package com.gurnoor.music_event.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Artist {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable= false)
	private String name;
	
	@Column(nullable = false)
	private String genre;
	
	@OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
	@JsonIgnore
	//@ToString.Exclude // Prevents infinite loop
	private List<Performance> performances;
}
package com.gurnoor.music_event.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

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
	private List<Performance> performances;
}

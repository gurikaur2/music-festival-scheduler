package com.gurnoor.music_event.model;

import java.time.*;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Performance {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private LocalDate date;
	
	@Column(nullable = false)
	private LocalTime startTime;
	
	@Column(nullable = false)
	private LocalTime endTime;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "artist_id", nullable = false)
	private Artist artist;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stage_id", nullable = false)
	private Stage stage;
}

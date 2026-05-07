package com.gurnoor.music_event.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Stage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false)
	private String name;
	
	@Column(nullable = false)
	private String location;
	
	@OneToMany(mappedBy = "stage", cascade = CascadeType.ALL)
	@JsonIgnore
	//@ToString.Exclude // Prevents infinite loop
	private List<Performance> performances;
}
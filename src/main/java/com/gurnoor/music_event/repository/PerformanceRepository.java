package com.gurnoor.music_event.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gurnoor.music_event.model.Performance;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {

	@Query("SELECT p FROM Performance p WHERE p.stage.id = :stageId " +
	           "AND p.date = :date " +
	           "AND p.startTime < :endTime " +
	           "AND p.endTime > :startTime")
	List<Performance> findOverlappingOnStage(
			@Param("stageId") Long stageId,
			@Param("date") LocalDate date,
			@Param("startTime") LocalTime startTime,
			@Param("endTime") LocalTime endTime);
	
	  @Query("SELECT p FROM Performance p WHERE p.artist.id = :artistId " +
	           "AND p.date = :date " +
	           "AND p.startTime < :endTime " +
	           "AND p.endTime > :startTime")
	    List<Performance> findOverlappingForArtist(
	            @Param("artistId") Long artistId,
	            @Param("date") LocalDate date,
	            @Param("startTime") LocalTime startTime,
	            @Param("endTime") LocalTime endTime);

	  List<Performance> findByDateOrderByStartTimeAsc(LocalDate date);
	  
	  List<Performance> findByStageIdOrderByDateAscStartTimeAsc(Long stageId);
	  
	  @Query("SELECT p FROM Performance p WHERE p.stage.id = :stageId " +
	           "AND p.date = :date " +
	           "AND p.startTime < :endTime " +
	           "AND p.endTime > :startTime " +
	           "AND p.id <> :excludeId")
	    List<Performance> findOverlappingOnStageExcluding(
	            @Param("stageId") Long stageId,
	            @Param("date") LocalDate date,
	            @Param("startTime") LocalTime startTime,
	            @Param("endTime") LocalTime endTime,
	            @Param("excludeId") Long excludeId);
	  
	  @Query("SELECT p FROM Performance p WHERE p.artist.id = :artistId " +
	           "AND p.date = :date " +
	           "AND p.startTime < :endTime " +
	           "AND p.endTime > :startTime " +
	           "AND p.id <> :excludeId")
	    List<Performance> findOverlappingForArtistExcluding(
	            @Param("artistId") Long artistId,
	            @Param("date") LocalDate date,
	            @Param("startTime") LocalTime startTime,
	            @Param("endTime") LocalTime endTime,
	            @Param("excludeId") Long excludeId);
}

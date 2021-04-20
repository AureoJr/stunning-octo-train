package com.github.aureo.csvanalyser.repository;

import java.util.List;

import com.github.aureo.csvanalyser.api.dto.MovieDTO;

public interface MovieRepository {

	MovieDTO save(MovieDTO movie);
	
	MovieDTO delete(Integer id);
	
	MovieDTO update(MovieDTO movie);
	
	List<MovieDTO> list();

	MovieDTO get(Integer year);
}

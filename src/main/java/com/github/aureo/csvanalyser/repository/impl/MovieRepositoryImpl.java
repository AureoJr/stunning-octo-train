package com.github.aureo.csvanalyser.repository.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import com.github.aureo.csvanalyser.api.dto.MovieDTO;
import com.github.aureo.csvanalyser.repository.MovieRepository;

@Repository
public class MovieRepositoryImpl implements MovieRepository  {

	private Map<Integer, MovieDTO> movies = new HashedMap(); 
	
	private AtomicInteger sequence = new AtomicInteger(0);
	
	@Override
	public MovieDTO save(MovieDTO movie) {
		
		movie.setId(sequence.addAndGet(1));		
		movies.put(movie.getId(), movie);
		
		return movie;
		
	}

	@Override
	public MovieDTO delete(Integer id) {
		
		if(Objects.isNull(id) || id  < 1 || Objects.isNull(movies.get(id))) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie Not Found");
		}
		
		return movies.remove(id);
	}

	@Override
	public MovieDTO update(MovieDTO movie) {
		
		if(Objects.isNull(movie) || Objects.isNull(movie.getId()) || movie.getId() < 1) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie Not Found");
		}
		
		return movies.put(movie.getId(), movie);

	}

	@Override
	public MovieDTO get(Integer id) {

		MovieDTO movie = movies.get(id);
		
		if(Objects.isNull(movie)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie Not Found");

		}
		
		return movie;
	}
	
	@Override
	public List<MovieDTO> list() {
		
		return movies
				.values()
				.stream()
				.collect(Collectors.toList());

	}

}

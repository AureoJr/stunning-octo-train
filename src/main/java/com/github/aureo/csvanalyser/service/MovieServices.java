package com.github.aureo.csvanalyser.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.github.aureo.csvanalyser.api.dto.IntervalListDTO;
import com.github.aureo.csvanalyser.api.dto.MovieDTO;
import com.github.aureo.csvanalyser.api.dto.StatusDTO;

public interface MovieServices {

	StatusDTO addNewMovie(MovieDTO movie);

	StatusDTO updateMovie(MovieDTO movie);

	List<MovieDTO> getMovies();

	StatusDTO deleteMovieById(Integer year);

	IntervalListDTO generateIntervals();

	MovieDTO getMovieById(Integer id); 
}

package com.github.aureo.csvanalyser.api;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.aureo.csvanalyser.api.dto.IntervalListDTO;
import com.github.aureo.csvanalyser.api.dto.MovieDTO;
import com.github.aureo.csvanalyser.api.dto.StatusDTO;
import com.github.aureo.csvanalyser.service.MovieServices;

@RestController
public class MovieController {

	@Autowired
	private MovieServices movieService;
	
	@PostMapping("/movie")
	public StatusDTO addNewMovie(@RequestBody MovieDTO movie) {
		
		return movieService.addNewMovie(movie);
	}
	
	@PutMapping("/movie")
	@ResponseBody
	public StatusDTO updateMovie(@RequestBody MovieDTO movie) {
		
		return movieService.updateMovie(movie);
	}
	
	@GetMapping("/movie/{id}")
	public MovieDTO getMovies(@PathVariable("id") Integer id) {
		return movieService.getMovieById(id);
	}
	
	@GetMapping( "/movies")
	public List<MovieDTO> getMovies() {
		
		return movieService.getMovies();
	}
	
	@GetMapping( "/movies/interval")
	public IntervalListDTO getIntervals() {
		
		return movieService.generateIntervals();
	}
	
	@DeleteMapping("/movie/{id}")
	public StatusDTO delete(@PathVariable("id") Integer id) {
		
		return movieService.deleteMovieById(id);
	}
}

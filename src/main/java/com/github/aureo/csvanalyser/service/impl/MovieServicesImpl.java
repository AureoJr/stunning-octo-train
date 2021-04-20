package com.github.aureo.csvanalyser.service.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.github.aureo.csvanalyser.api.dto.IntervalDTO;
import com.github.aureo.csvanalyser.api.dto.IntervalListDTO;
import com.github.aureo.csvanalyser.api.dto.MovieDTO;
import com.github.aureo.csvanalyser.api.dto.StatusDTO;
import com.github.aureo.csvanalyser.repository.MovieRepository;
import com.github.aureo.csvanalyser.service.MovieServices;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;


@Service
public class MovieServicesImpl implements MovieServices {

	@Autowired
	private MovieRepository movieRepository;
	
	@Value("classpath:movielist.csv")
	private Resource csvFile;

	@Override
	public StatusDTO addNewMovie(MovieDTO movie) {

		MovieDTO newMovie = movieRepository.save(movie);
		StatusDTO status = new StatusDTO("Movie with id " + newMovie.getId() +" was created", "200");
		
		return status;	
	}

	
	@Override	 	
	public StatusDTO updateMovie(MovieDTO movie) {
		
		MovieDTO updatedMovie = movieRepository.update(movie);
		Objects.requireNonNull(updatedMovie);
		
		return new StatusDTO("Movie with ID " + movie.getId() + " was updated", "200");
	}

	@Override
	public MovieDTO getMovieById(Integer id) {
		return movieRepository.get(id);
	}

	@Override
	public List<MovieDTO> getMovies() {
		return movieRepository.list();
	}
	
	@Override
	public StatusDTO deleteMovieById(Integer id) {
		movieRepository.delete(id);
		return new StatusDTO("Movie with ID " + id + " was deleted", "200");
	}

	@PostConstruct
	private void loadMovies()  throws Exception {

		List<MovieDTO> movies = parseFileToList(csvFile.getInputStream());
		movies.stream().forEach(movie -> this.addNewMovie(movie));
	}

	private List<MovieDTO> parseFileToList(InputStream in) throws Exception {
		List<MovieDTO> movies;
		
        try (Reader reader = new BufferedReader(new InputStreamReader(in))) {
        	
            CsvToBean<MovieDTO> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(MovieDTO.class)
                    .withSeparator(';')
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            movies = csvToBean.parse();
        } catch (Exception ex) {
            throw ex;
        }
        
        return movies;
   
	}

	@Override
	public IntervalListDTO generateIntervals() {
		final IntervalListDTO intervalList = new IntervalListDTO();
		
		final Map<String,IntervalDTO> min = new HashMap();
		final Map<String,IntervalDTO> max = new HashMap();
		List<MovieDTO> movies = movieRepository.list();
		
		movies
			.sort(Comparator.comparingInt(MovieDTO::getYear));

		movies
			.stream()
			.filter(movie -> movie.isWinner())
			.forEach(calulateRanking(intervalList, min, max));
		
		return intervalList;
	}

	private Consumer<? super MovieDTO> calulateRanking(final IntervalListDTO intervalList, final Map<String, IntervalDTO> min, final Map<String, IntervalDTO> max) {
		
		return movie -> {
			
			verifyProducers(movie.getProducers(), min, movie, intervalList);

		};
	}

	private void verifyProducers(String producers, Map<String, IntervalDTO> min, MovieDTO movie, IntervalListDTO intervalList) {
		
		Arrays.stream(producers.split(",|and"))
		    .map(String::trim)
			.forEach(producer -> {

				if(!min.containsKey(producer)) {
					min.put(producer, createInterval(movie, producer));
					return;
				}
				
				calcInterval(min, producer, movie, intervalList);
				
			});
		
	}

	private void calcInterval(Map<String, IntervalDTO> min, String producer, MovieDTO movie, IntervalListDTO intervalList) {
		IntervalDTO interval = min.get(producer);
		Integer newInterval = movie.getYear() - interval.getPreviousWin();
		
		interval.setFollowingWin(movie.getYear());
		interval.setInterval(newInterval);
		
		startIntervals(intervalList, newInterval);
		
		validateShortestInterval(intervalList, interval, newInterval);
		
		validateGretestInterval(intervalList, interval, newInterval);
		
		min.put(producer, createInterval(movie, producer));
		
	}

	private void validateGretestInterval(IntervalListDTO intervalList, IntervalDTO interval, Integer newInterval) {
		
		if(newInterval > intervalList.getMaxInterval()) {
			intervalList.getMax().clear();
		}
		
		if(newInterval >= intervalList.getMaxInterval()) {
			intervalList.getMax().add(interval);
			intervalList.setMaxInterval(newInterval);
		}
	}

	private void validateShortestInterval(IntervalListDTO intervalList, IntervalDTO interval,
			Integer newInterval) {

			if(newInterval < intervalList.getMinInterval() ) {
				intervalList.getMin().clear();
			}
			
			if(newInterval <= intervalList.getMinInterval() ) {
				intervalList.getMin().add(interval);
				intervalList.setMinInterval(newInterval);
			}
			
	}

	private void startIntervals(IntervalListDTO intervalList, Integer newInterval) {
		if(intervalList.getMinInterval() == null) {
			intervalList.setMaxInterval(newInterval);
			intervalList.setMinInterval(newInterval);
		}
	}

	private IntervalDTO createInterval(MovieDTO movie, String producer) {
		
		return new IntervalDTO(producer, 0, movie.getYear(), 0);
	}
}

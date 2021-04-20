package com.github.aureo.csvanalyser.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.github.aureo.csvanalyser.api.MovieController;
import com.github.aureo.csvanalyser.repository.MovieRepository;
import com.github.aureo.csvanalyser.service.MovieServices;

@WebMvcTest(MovieController.class)
public class MovieControllerTests {
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean 
	private MovieServices movieServices;

	@MockBean 
	private MovieRepository movieRepository;

	@Test
	public void testAddNewMovie() throws Exception {
		
		final String successDTO = "{\"year\": 1980, \"title\": \"Can't Stop the Music\", \"studios\": \"Associated Film Distribution\", \"producers\" : \"Allan Carr\", \"winner\": \"yes\"}";
		
		this.mockMvc.perform(
						post("/movie")
							.contentType(MediaType.APPLICATION_JSON.toString())
							.content(successDTO)
					)
			.andExpect(status().isOk())
		    .andDo(print());
		
	}
	
	@Test
	public void testAddNewMovieWithoutTitleOrEmptyTitle() throws Exception {
		
		final String movieWithEmptyTitle = "{\"year\": 1980, \"title\": \"\", \"studios\": \"Associated Film Distribution\", \"producers\" : \"Allan Carr\", \"winner\": \"yes\"}";
		final String movieWithoutTitle = "{\"year\": 1980, \"studios\": \"Associated Film Distribution\", \"producers\" : \"Allan Carr\", \"winner\": \"yes\"}";

		this.mockMvc
			.perform(post("/movie", movieWithoutTitle))
			.andExpect(status().is4xxClientError())
		    .andDo(print());
		
		this.mockMvc
			.perform(post("/movie", movieWithEmptyTitle))
			.andExpect(status().is4xxClientError())
		    .andDo(print());
		
	}

	
	@Test
	public void testAddNewMovieWithoutStudio() throws Exception {
		
		final String movieWithEmptyStudio = "{\"year\": 1980, \"title\": \"Can't Stop the Music\", \"studios\": \"\", \"producers\" : \"Allan Carr\", \"winner\": \"yes\"}";
		final String movieWithoutStudio = "{\"year\": 1980, \"title\": \"Can't Stop the Music\", \"studios\": \"\", \"producers\" : \"Allan Carr\", \"winner\": \"yes\"}";

		this.mockMvc
			.perform(post("/movie", movieWithEmptyStudio))
			.andExpect(status().is4xxClientError());
		
		this.mockMvc
			.perform(post("/movie", movieWithoutStudio))
			.andExpect(status().is4xxClientError());
		
	}
	
	@Test
	public void testAddNewMovieWithoutStudioOrEmptyProducer() throws Exception {
		
		final String movieWithEmptyProducer = "{\"year\": 1980, \"title\": \"Can't Stop the Music\", \"studios\": \"Associated Film Distribution\", \"producers\" : \"\", \"winner\": \"yes\"}";;
		final String movieWithoutproducer = "{\"year\": 1980, \"title\": \"Can't Stop the Music\", \"studios\": \"\", \"winner\": \"yes\"}";

		this.mockMvc
			.perform(post("/movie", movieWithEmptyProducer))
			.andExpect(status().is4xxClientError());
		
		this.mockMvc
			.perform(post("/movie", movieWithoutproducer))
			.andExpect(status().is4xxClientError());
		
	}
	
	@Test
	public void testUpdateMovie() throws Exception {
		
		final String successDTO = "{\"id\": 1, \"year\": 1980, \"title\": \"Can't Stop the Music\", \"studios\": \"Associated Film Distribution\", \"producers\" : \"Allan Carr\", \"winner\": \"yes\"}";
		
		this.mockMvc
			.perform(put("/movie")
					.content(successDTO)
					.contentType(MediaType.APPLICATION_JSON.toString()))
			.andExpect(status().isOk())
		    .andDo(print());
		
	}	
	
	@Test
	public void testGetMovieById() throws Exception {
		this.mockMvc
			.perform(get("/movie/1/"))
			.andExpect(status().isOk())
		    .andDo(print());
		
	}	
	
	@Test
	public void testGetMovies() throws Exception {
		this.mockMvc
			.perform(get("/movies/"))
			.andExpect(status().isOk())
		    .andDo(print());
		
	}	
	
	@Test
	public void testGetIntervals() throws Exception {
		this.mockMvc
			.perform(get("/movies/interval"))
			.andExpect(status().isOk())
		    .andDo(print());
		
	}	
	
	@Test
	public void testDeleteMovieById() throws Exception {
		this.mockMvc
			.perform(delete("/movie/1"))
			.andExpect(status().isOk())
		    .andDo(print());
		
	}
}

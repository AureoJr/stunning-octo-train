package com.github.aureo.csvanalyser.api.dto;

import static java.util.Objects.requireNonNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvIgnore;

import lombok.Data;

@Data
public class MovieDTO {

	public MovieDTO() {

	}

	@JsonCreator
	public MovieDTO(@JsonProperty("id") final Integer id,
			@JsonProperty("year") final Integer year,
			@JsonProperty("title") final String title,
			@JsonProperty("producers") final String producers,
			@JsonProperty("studios") final String studios,
			@JsonProperty("winner") final String winner) {

		this.id = id;
		this.year = requireNonNull(year);
		this.title = requireNonNull(title);
		this.studios = requireNonNull(studios);
		this.producers = requireNonNull(producers);
		this.winner = winner;
	}

	@CsvIgnore
	private Integer id;

	@CsvBindByName
	private Integer year;

	@CsvBindByName
	private String title;

	@CsvBindByName
	private String studios;

	@CsvBindByName
	private String producers;

	@CsvBindByName(required = false)
	private String winner = "";

	public String getWinner() {

		if (!this.winner.equals("yes")) {
			return "no";
		}

		return this.winner;
	}

	public Boolean isWinner() {
		return getWinner().equals("yes");
	}

}

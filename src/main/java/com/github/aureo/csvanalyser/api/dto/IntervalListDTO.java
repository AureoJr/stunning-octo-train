package com.github.aureo.csvanalyser.api.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class IntervalListDTO {

	@JsonIgnore
	private Integer minInterval;
	
	@JsonIgnore
	private Integer maxInterval;
	
	private List<IntervalDTO> min;
	
	private List<IntervalDTO> max;
	
	public List<IntervalDTO> getMin() {
		if(Objects.isNull(min)) {
			this.min = new ArrayList<>();
		}
		
		return this.min;
	}
	
	public List<IntervalDTO> getMax() {
		if(Objects.isNull(max)) {
			this.max = new ArrayList<>();
		}
		
		return this.max;
	}
}

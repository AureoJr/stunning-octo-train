package com.github.aureo.csvanalyser.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IntervalDTO {
	
	private String producer;
	
	private Integer interval;
	
	private Integer previousWin;
	
	private Integer followingWin;

}

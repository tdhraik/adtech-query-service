package com.weq.adtech.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatsModel{

	public Interval interval;
	public Stats stats;
	public List<GroupedData> data;
}

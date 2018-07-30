package com.weq.adtech.models;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Interval {
	private String start;
	private String end;
}

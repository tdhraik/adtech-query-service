package com.weq.adtech.models;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Stats {
	private long deliveries;
	private long clicks;
	private long installs;
}

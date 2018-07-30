package com.weq.adtech.models;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Data
@Component
public class GroupedData {

	private HashMap<String, String> fields = new HashMap<>();
	private Stats stats = new Stats();
}

package com.weq.adtech.service;

import com.weq.adtech.models.StatsModel;

public interface AdTechQueryService {

	StatsModel fetchResults(String start, String end, String[] groupBy);
}

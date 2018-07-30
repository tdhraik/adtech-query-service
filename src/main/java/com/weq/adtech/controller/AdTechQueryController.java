package com.weq.adtech.controller;

import com.weq.adtech.models.StatsModel;
import com.weq.adtech.service.AdTechQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/ads")
public class AdTechQueryController {

	@Autowired AdTechQueryService adTechQueryService;

	@RequestMapping(value="/testquery", method = RequestMethod.GET)
	public ResponseEntity<?> testEndPoint(){
		return new ResponseEntity<HttpStatus>(HttpStatus.OK);
	}

	@GetMapping("/statistics")
	public ResponseEntity<StatsModel> getStats(
			@RequestParam String start, @RequestParam String end, @RequestParam(required = false) String[] groupBy) {
		return new ResponseEntity<StatsModel>(adTechQueryService.fetchResults(start, end, groupBy), HttpStatus.OK);
	}
}

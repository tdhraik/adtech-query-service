package com.weq.adtech.service;

import com.weq.adtech.domain.Click;
import com.weq.adtech.domain.Delivery;
import com.weq.adtech.domain.Install;
import com.weq.adtech.models.GroupedData;
import com.weq.adtech.models.Interval;
import com.weq.adtech.models.Stats;
import com.weq.adtech.models.StatsModel;
import com.weq.adtech.repositories.ClickRepository;
import com.weq.adtech.repositories.DeliveryRepository;
import com.weq.adtech.repositories.InstallRepository;
import com.weq.adtech.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class AdTechQueryServiceImpl implements AdTechQueryService {

	@Autowired MongoTemplate mongoTemplate;
	@Autowired DeliveryRepository deliveryRepository;
	@Autowired ClickRepository clickRepository;
	@Autowired InstallRepository installRepository;
	@Autowired StatsModel statsModel;
	@Autowired Interval interval;
	@Autowired Stats stats;
	@Autowired GroupedData groupedData;

	@Override public StatsModel fetchResults(String start, String end, String[] groupBy) {
		if(null == start || null == end){
			return null;
		}
		// date string reaching backend replace + with space, hence had to split the date
		start = start.split(" ")[0];
		end = end.split(" ")[0];
		Date startDate = DatatypeConverter.parseDate(start).getTime();
		Date endDate = DatatypeConverter.parseDate(end).getTime();
		interval.setStart(start);
		interval.setEnd(end);
		statsModel.setInterval(interval);
		if(null != groupBy && groupBy.length > 0) {
			Aggregation aggregation = newAggregation(match(Criteria.where(Constants.TIME).gte(startDate).
							lte(endDate)), group(groupBy).count().as(Constants.COUNT));
			List<GroupedData> groupedDataList = prepareResponse(getDeliveryAggregatedResults(aggregation),
					getClickAggregatedResults(aggregation),getInstallAggregatedResults(aggregation));
			statsModel.setData(groupedDataList);
			statsModel.setStats(null);
		}else {
			stats.setDeliveries(deliveryRepository.countByTimeBetween(startDate, endDate));
			stats.setClicks(clickRepository.countByTimeBetween(startDate, endDate));
			stats.setInstalls(installRepository.countByTimeBetween(startDate, endDate));
			statsModel.setStats(stats);
			statsModel.setData(null);
		}
		return statsModel;
	}

	private List<GroupedData> prepareResponse(List<Object> delRes, List<Object> clickRes, List<Object> instRes){
		List<GroupedData> groupedDataList = new ArrayList<>();
		setDeliveryCounts(delRes, groupedDataList);
		setClickCounts(clickRes, groupedDataList);
		setInstallCounts(instRes, groupedDataList);
		return groupedDataList;
	}

	private List<Object> getDeliveryAggregatedResults(Aggregation aggregation){
		AggregationResults<Object> deliveryGroupResults = mongoTemplate.aggregate(aggregation, Delivery.class, Object.class);
		return deliveryGroupResults.getMappedResults();
	}

	private List<Object> getClickAggregatedResults(Aggregation aggregation){
		AggregationResults<Object> clickGroupResults = mongoTemplate.aggregate(aggregation, Click.class, Object.class);
		return clickGroupResults.getMappedResults();
	}

	private List<Object> getInstallAggregatedResults(Aggregation aggregation){
		AggregationResults<Object> installGroupResults = mongoTemplate.aggregate(aggregation, Install.class, Object.class);
		return installGroupResults.getMappedResults();
	}

	private void setDeliveryCounts(List<Object> delRes, List<GroupedData> groupedDataList){
		delRes.parallelStream().forEach(dR -> {
			((HashMap)dR).forEach((k,v) -> {
				groupedData.getStats().setDeliveries(Long.valueOf(((HashMap)dR).get(Constants.COUNT).toString()));
				if(!Constants.COUNT.equalsIgnoreCase(k.toString())){
					groupedData.getFields().put(k.toString(), (v.toString()));
				}
			});
			groupedDataList.add(groupedData);
			groupedData = new GroupedData();
		});
	}

	private void setClickCounts(List<Object> clickRes, List<GroupedData> groupedDataList){
		groupedDataList.parallelStream().forEach(gd -> {
			clickRes.parallelStream().forEach(cR -> {
				long clickCount = Long.valueOf(((HashMap)cR).get(Constants.COUNT).toString());
				((HashMap)cR).remove(Constants.COUNT);
				if((((HashMap)cR).entrySet()).equals(gd.getFields().entrySet())){
					gd.getStats().setClicks(clickCount);
				}
				((HashMap)cR).put(Constants.COUNT, clickCount);
			});
		});
	}

	private void setInstallCounts(List<Object> instRes, List<GroupedData> groupedDataList){
		groupedDataList.parallelStream().forEach(gd -> {
			instRes.parallelStream().forEach(iR -> {
				long installCount = Long.valueOf(((HashMap)iR).get(Constants.COUNT).toString());
				((HashMap)iR).remove(Constants.COUNT);
				if((((HashMap)iR).entrySet()).equals(gd.getFields().entrySet())){
					gd.getStats().setInstalls(installCount);
				}
				((HashMap)iR).put(Constants.COUNT, installCount);
			});
		});
	}
}

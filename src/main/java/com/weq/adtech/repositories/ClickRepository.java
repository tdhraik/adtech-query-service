package com.weq.adtech.repositories;

import com.weq.adtech.domain.Click;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface ClickRepository extends MongoRepository<Click, String> {
	long countByTimeBetween(Date start, Date end);
}

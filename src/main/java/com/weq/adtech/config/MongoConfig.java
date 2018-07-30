package com.weq.adtech.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.weq.adtech.repositories")
public class MongoConfig extends AbstractMongoConfiguration {

	@Override
	protected String getDatabaseName() {
		return "test";
	}

	@Override public MongoClient mongoClient() {
		MongoClientURI uri = new MongoClientURI(
				"mongodb+srv://tarun:tarun@cluster0-2najt.mongodb.net/admin?retryWrites=true");

		MongoClient mongoClient = new MongoClient(uri);
		return mongoClient;
	}
}

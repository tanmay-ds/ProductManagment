package com.example.productmngmt.config;

import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.github.mongobee.Mongobee;
import com.mongodb.MongoClient;

@Configuration
public class DatabaseConfig {

	@Bean
	public Mongobee mongobee(MongoClient mongoClient, MongoTemplate mongoTemplate, MongoProperties mongoProperties) {

		Mongobee mongobee = new Mongobee(mongoClient);
		mongobee.setDbName(mongoProperties.getDatabase());
		mongobee.setMongoTemplate(mongoTemplate);
		mongobee.setChangeLogsScanPackage("com.example.productmngmt.config.changelog");
		mongobee.setEnabled(true);
		return mongobee;
	}

}

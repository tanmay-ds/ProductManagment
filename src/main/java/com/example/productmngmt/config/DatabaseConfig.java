package com.example.productmngmt.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.SpringDataMongoV3Driver;

import io.changock.runner.spring.v5.ChangockSpring5;
import io.changock.runner.spring.v5.SpringInitializingBeanRunner;

@Configuration
public class DatabaseConfig {

	@Bean
	public SpringInitializingBeanRunner mongock(
	        ApplicationContext springContext,
	        MongoTemplate mongoTemplate){
		return ChangockSpring5.builder()
		        .setDriver(SpringDataMongoV3Driver.withDefaultLock(mongoTemplate))
		        .addChangeLogsScanPackage("com.example.productmngmt.config.changelog")
		        .setSpringContext(springContext)
		        .buildInitializingBeanRunner();
	  }

}

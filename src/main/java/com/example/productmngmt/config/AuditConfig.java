package com.example.productmngmt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import com.example.productmngmt.security.CustomAuditAware;

@Configuration
@EnableMongoAuditing
public class AuditConfig {

	@Bean
	public CustomAuditAware getCustomAuditAware() {
		return new CustomAuditAware();
	}
	
}
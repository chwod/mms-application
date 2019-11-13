package com.chwod.robot.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateHelper {

	@Autowired
	public RestTemplateBuilder builder;
	
	@Bean
	public RestTemplate restTemplate(){
		return this.builder.build();
	}
}

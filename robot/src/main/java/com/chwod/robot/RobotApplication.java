package com.chwod.robot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.web.filter.DelegatingFilterProxy;

@SpringBootApplication
public class RobotApplication {

	public static void main(String[] args) {
		SpringApplication.run(RobotApplication.class, args);
	}

	public FilterRegistrationBean filterProxy() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(new DelegatingFilterProxy());

		Map<String, String> initParameters = new HashMap<>();
		initParameters.put("targetBeanName", "logFilter");
		initParameters.put("targetFilterLifecycle", "true");
		registrationBean.setInitParameters(initParameters);
		List<String> urlPatterns = new ArrayList<String>();
		urlPatterns.add("/");
		registrationBean.setUrlPatterns(urlPatterns);
		return registrationBean;
	}
}

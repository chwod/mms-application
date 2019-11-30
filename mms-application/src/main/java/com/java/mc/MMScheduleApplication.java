package com.java.mc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.java.mc.db.DBSchemaControl;
import com.java.mc.schedule.ScheduleConfiguration;

@SpringBootApplication
@EnableScheduling
@EnableBatchProcessing
public class MMScheduleApplication extends WebMvcConfigurerAdapter implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(MMScheduleApplication.class);
	
	@Autowired
	private DBSchemaControl dbSchemaControl;
	
	@Autowired
	private ScheduleConfiguration scheduleConfiguration;

	public static void main(String[] args) {
		new SpringApplicationBuilder(MMScheduleApplication.class).run(args);
	}
	
	@Override
	public void run(String... args) {
		try {
			this.dbSchemaControl.init();
			this.scheduleConfiguration.init();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			System.exit(-1);
		}
	}
	
	@Bean
	public ApplicationSecurity applicationSecurity() {
		return new ApplicationSecurity();
	}
	
	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	protected static class ApplicationSecurity extends WebSecurityConfigurerAdapter {

		@Autowired
		private JdbcTemplate jdbcTemplate;
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable().authorizeRequests()
					.antMatchers("/css/**").permitAll()
					.antMatchers("/reset").permitAll()
					.antMatchers("/check").permitAll()
					.antMatchers("/cf411a8c-2be9-490d-bed7-dba785b8718e").permitAll()
					.anyRequest()
					.fullyAuthenticated().and().formLogin().loginPage("/login")
					.failureUrl("/login?error").permitAll().and().logout().permitAll();
		}

		@Override
		public void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.jdbcAuthentication().dataSource(this.jdbcTemplate.getDataSource());
		}

	}

}

package com.personalprojects.uptimefinder;

import com.personalprojects.uptimefinder.entity.Service;
import com.personalprojects.uptimefinder.entity.UptimeMonitor;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@SpringBootApplication
@EnableBatchProcessing(modular = true)
public class UptimeFinderApplication {

	@Autowired
	private DataSource dataSource;

	public static void main(String[] args) {
		SpringApplication.run(UptimeFinderApplication.class, args);
	}

	@Bean(name="entityManagerFactory")
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);
		sessionFactory.setAnnotatedClasses(Service.class, UptimeMonitor.class);
		return sessionFactory;
	}

	@Bean
	public ScheduledExecutorService threadPoolTaskScheduler() {
		ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(10);
		return scheduledExecutor;
	}

}

package com.greencross.lims;

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class BatchConfig {
	private final ResourcelessTransactionManager tx = new ResourcelessTransactionManager();
	private final JobRepository repo;
	public BatchConfig() throws Exception {
		MapJobRepositoryFactoryBean factory = new MapJobRepositoryFactoryBean();
		factory.setTransactionManager(tx);
		factory.afterPropertiesSet();
		repo = factory.getObject();
	}
	@Bean
	public JobLauncher jobLauncher() {
		SimpleJobLauncher launcher = new SimpleJobLauncher();
		launcher.setJobRepository(repo);
		return launcher;
	}
	@Bean
	public JobBuilderFactory jobBuilderFactory() {
		return new JobBuilderFactory(repo);
	}
	@Bean
	public StepBuilderFactory stepBuilderFactory() {
		return new StepBuilderFactory(repo, tx);
	}
}

package com.greencross;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.SpringBeanContainer;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableConfigurationProperties
@EnableTransactionManagement
@EnableJpaRepositories(
	entityManagerFactoryRef = "entityManagerFactoryLims",
	transactionManagerRef = "transactionManagerLims"
) @Primary
public class DataSourceConfigLims {
	@Bean
	@Qualifier("jpaProperties")
	@ConfigurationProperties(prefix="spring.jpa")
	public Properties jpaProperties() {
		return new Properties();
	}
	@Bean(name="dataSourceLims")
	@Qualifier("dataSourceLims")
	@ConfigurationProperties(prefix="spring.datasource.hikari.lims")
	public DataSource lims() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}
	@Primary
	@Bean(name = "entityManagerFactoryLims")
	public LocalContainerEntityManagerFactoryBean getFactory(@Qualifier("dataSourceLims") DataSource datasource, @Qualifier("jpaProperties") Properties jpaProperty, ConfigurableListableBeanFactory beanFactory) {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(datasource);
		em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		em.setPackagesToScan("com.greencross.lims.entity");
		em.setPersistenceUnitName("LIMS");
		em.setJpaProperties(jpaProperty);
		em.getJpaPropertyMap().put(AvailableSettings.BEAN_CONTAINER, new SpringBeanContainer(beanFactory));
		return em;
	}
	@Primary
	@Bean(name = "transactionManagerLims")
	public PlatformTransactionManager getTransactionManager(@Qualifier("entityManagerFactoryLims") EntityManagerFactory factory) {
		return new JpaTransactionManager(factory);
	}
}

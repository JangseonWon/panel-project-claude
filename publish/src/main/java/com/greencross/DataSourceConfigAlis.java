package com.greencross;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
public class DataSourceConfigAlis {
	@Bean(name="dataSourceAlis")
	@Qualifier("dataSourceAlis")
	@ConfigurationProperties(prefix="spring.datasource.hikari.alis")
	public DataSource panel() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}
	@Bean(name = "entityManagerFactoryAlis")
	public LocalContainerEntityManagerFactoryBean getFactory(@Qualifier("dataSourceAlis") DataSource datasource, @Qualifier("jpaProperties") Properties jpaProperty) {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(datasource);
		HibernateJpaVendorAdapter adaptor = new HibernateJpaVendorAdapter();
		adaptor.setGenerateDdl(true);
		adaptor.setShowSql(false);
		em.setJpaVendorAdapter(adaptor);
		em.setPackagesToScan("com.greencross.alis.api");
		em.setPersistenceUnitName("ALIS");
		em.setJpaProperties(jpaProperty);
		return em;
	}
	@Bean(name = "transactionManagerAlis")
	public PlatformTransactionManager getTransactionManager(@Qualifier("entityManagerFactoryAlis") EntityManagerFactory factory) {
		return new JpaTransactionManager(factory);
	}
}

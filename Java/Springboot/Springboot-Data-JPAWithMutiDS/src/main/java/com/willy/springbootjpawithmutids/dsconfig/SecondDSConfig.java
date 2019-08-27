package com.willy.springbootjpawithmutids.dsconfig;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactorySecond"
		, transactionManagerRef = "transactionManagerSecond"
		, basePackages = {"com.willy.springbootjpawithmutids.dao2" })
public class SecondDSConfig {
	@Autowired
	@Qualifier("secondDataSource")
	private DataSource secondDataSource;
	@Autowired
	@Qualifier("vendorProperties")
	private Map<String, Object> vendorProperties;

	@Bean(name = "entityManagerFactorySecond")
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryPrimary(EntityManagerFactoryBuilder builder) {
		return builder
				.dataSource(secondDataSource)
				.properties(vendorProperties)
				.packages("com.willy.springbootjpawithmutids.entity")
				.persistenceUnit("secondPersistenceUnit")
				.build();
	}

	@Primary
	@Bean(name = "entityManagerSecond")
	public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
		return entityManagerFactoryPrimary(builder).getObject().createEntityManager();
	}

	@Bean(name = "transactionManagerSecond")
	@Primary
	PlatformTransactionManager transactionManagerPrimary(EntityManagerFactoryBuilder builder) {
		return new JpaTransactionManager(entityManagerFactoryPrimary(builder).getObject());
	}
}

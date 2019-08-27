package com.willy.springbootjpawithmutids.dsconfig;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DataSourceConfig {
	//第一數據源
	@Primary
	@Bean(name = "firstDataSource")
	@ConfigurationProperties("spring.datasource.firstds")
	public DataSource firstDataSource() {
		return DataSourceBuilder.create().build();
	}
	
	//第二數據源
	@Bean(name = "secondDataSource")
	@ConfigurationProperties("spring.datasource.secondds")
	public DataSource secondDataSource() {
		return DataSourceBuilder.create().build();
	}
	
	//JPA設置
	@Autowired
	private JpaProperties jpaProperties;
	@Autowired
	private HibernateProperties hibernateProperties;
	@Bean(name = "vendorProperties")
	public Map<String, Object> getVendorProperties() {
	    return hibernateProperties.determineHibernateProperties(jpaProperties.getProperties(), new HibernateSettings());
	}
}

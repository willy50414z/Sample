package com.willy.spring;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.jolbox.bonecp.BoneCPDataSource;

@Configuration//表示這是一個xml配置
@ComponentScan(basePackages = "com.willy")//掃描的Package
@PropertySource(value= {"classpath:jdbc.properties"})//將此文件納入參數設定文件
public class SpringConfig {
	@Value("${jdbc.driverName}")
	private String jdbcDriverName;
	@Value("${jdbc.url}")
	private String jdbcUrl;
	@Value("${jdbc.userName}")
	private String jdbcUserName;
	@Value("${jdbc.password}")
	private String jdbcPassword;
	@Autowired
	private DataSource ds;
	@Autowired
	private SqlSessionFactory ssf;
	
	
	@Bean(destroyMethod="close")
	public DataSource getDataSourceBean(){
		BoneCPDataSource ds=new BoneCPDataSource();
		ds.setDriverClass(jdbcDriverName);
		ds.setJdbcUrl(jdbcUrl);
		ds.setUsername(jdbcUserName);
		ds.setPassword(jdbcPassword);
		//最大空閒時間
		ds.setIdleConnectionTestPeriodInMinutes(60);
		//為連線最大存活時間
		ds.setIdleMaxAgeInMinutes(30);
		//每分區最大連線數
		ds.setMaxConnectionsPerPartition(100);
		//每分區最小連線數
		ds.setMaxConnectionsPerPartition(5);
		return ds;
	}
	@Bean
	public SqlSessionFactory getSqlSessionFactory() throws Exception {
		SqlSessionFactoryBean ssfb=new SqlSessionFactoryBean();
		ssfb.setDataSource(ds);
		ssfb.setTypeAliasesPackage("com.willy.entity");
		ssfb.setMapperLocations(
	            new PathMatchingResourcePatternResolver().getResources("classpath:com/willy/sqlMapper/*.xml"));
		return ssfb.getObject();
	}
//	@Bean
//	public SqlSession getSqlSession() throws Exception {
//		return ssf.openSession();
//	}
	@Bean
	public SqlSessionTemplate getSqlSessionTemplate() {
		return new SqlSessionTemplate(ssf);
	}
//	@Bean
//	public SqlSession getSqlSession() throws Exception {
//		return ssf.openSession();
//	}
//	@Bean
//	public DataSourceTransactionManager getTransactionManager() {
//		DataSourceTransactionManager dstm=new DataSourceTransactionManager();
//		dstm.setDataSource(ds);
//		return dstm;
//	}
//	@Bean
//	@Primary
//	public SqlSessionTemplate getSqlSessionTemplate() {
//		return new SqlSessionTemplate(ssf);
//	}
//	@Bean
//	public SqlSessionTemplate getBathSqlSessionTemplate() {
//		return new SqlSessionTemplate(ssf,ExecutorType.BATCH);
//	}
}

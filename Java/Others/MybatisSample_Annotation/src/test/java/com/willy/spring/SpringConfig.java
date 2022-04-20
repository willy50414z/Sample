package com.willy.spring;

import javax.sql.DataSource;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.jolbox.bonecp.BoneCPDataSource;
import com.willy.db.IFieldMapListImp;
import com.willy.sqlMapper.IFieldMapListDAO;


@Configuration//表示這是一個xml配置
@ComponentScan(basePackages = "com.willy"
//,excludeFilters= {
//		@ComponentScan.Filter(type=FilterType.ANNOTATION
//								, value= {Service.class,Controller.class})}
)//掃描的Package
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
	@Autowired
	private SqlSession ss;
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
//	@Bean
//	public IFieldMapListImp getIFieldMapListImp() {
//		return new IFieldMapListImp();
//	}
	//移至MybatisConfig
	@Bean
	public SqlSessionFactory getSqlSessionFactory() throws Exception {
		SqlSessionFactoryBean ssfb=new SqlSessionFactoryBean();
		ssfb.setDataSource(ds);
		ssfb.setTypeAliasesPackage("com.willy.entity");
		ssfb.setMapperLocations(
	            new PathMatchingResourcePatternResolver().getResources("classpath:com/willy/sqlMapper/*.java"));
		return ssfb.getObject();
	}
	@Bean
	@Primary
	public SqlSession getSqlSession() throws Exception {
		ssf.getConfiguration().addMapper(IFieldMapListDAO.class);
		return ssf.openSession();
	}
	@Bean(name="batchSqlSession")
	public SqlSession getBatchSqlSession() throws Exception {
		return ssf.openSession(ExecutorType.BATCH);
	}
	
	@Bean
	public DataSourceTransactionManager getTransactionManager() {
		DataSourceTransactionManager dstm=new DataSourceTransactionManager();
		dstm.setDataSource(ds);
		return dstm;
	}
	
}

package com.willy.spring;

import javax.sql.DataSource;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.willy.db.IFieldMapListImp;
import com.willy.sqlMapper.IFieldMapListDAO;

//@Configuration
public class MybatisConfig {
//	@Autowired
//	private DataSource ds;
//	@Autowired
//	private SqlSessionFactory ssf;
//	@Autowired
//	private SqlSession ss;
//	
//	@Bean
//	@ConditionalOnMissingBean//沒有這個類別才創建
//	public SqlSessionFactory getSqlSessionFactory() throws Exception {
//		SqlSessionFactoryBean ssfb=new SqlSessionFactoryBean();
//		ssfb.setDataSource(ds);
//		ssfb.setTypeAliasesPackage("com.willy.entity");
//		//可由MapperScannerConfig.java取代
////		ssfb.setMapperLocations(
////	            new PathMatchingResourcePatternResolver().getResources("classpath:com/willy/sqlMapper/*.java"));
//		//設置Mybatis XML(本案例不需要)
////		ssfb.setConfigLocation(configLocation);
//		return ssfb.getObject();
//	}
//	@Bean
//	@Primary
//	public SqlSession getSqlSession() throws Exception {
//		ssf.getConfiguration().addMapper(IFieldMapListDAO.class);
//		return ssf.openSession();
//	}
//	@Bean(name="batchSqlSession")
//	public SqlSession getBatchSqlSession() throws Exception {
//		return ssf.openSession(ExecutorType.BATCH);
//	}
//	@Bean
//    public DataSourceTransactionManager transactionManager() {
//        return new DataSourceTransactionManager(ds);
//    }
//	@Bean
//	public IFieldMapListImp getIFieldMapListImp() {
//		return new IFieldMapListImp();
//	}
//	@Bean
//	public DataSourceTransactionManager getTransactionManager() {
//		DataSourceTransactionManager dstm=new DataSourceTransactionManager();
//		dstm.setDataSource(ds);
//		return dstm;
//	}
}

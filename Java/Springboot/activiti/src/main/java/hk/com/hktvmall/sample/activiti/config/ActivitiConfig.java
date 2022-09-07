package hk.com.hktvmall.sample.activiti.config;

import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import javax.sql.DataSource;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
public class ActivitiConfig {
  private final DataSource dataSource;

  private final PlatformTransactionManager platformTransactionManager;
  @Autowired
  public ActivitiConfig(HikariDataSource dataSource, PlatformTransactionManager platformTransactionManager) {
    this.dataSource = dataSource;
    this.platformTransactionManager = platformTransactionManager;
  }
  /*
   * 1. 创建配置文件，也就是提供一些配置信息，这样就可以自定义自己的创建信息了
   * 需要一些参数，1. 数据源。2. 事务管理器。
   * 这里还加入了自动扫描process包下的bpmn(流程定义文件)的设置，这样就可以省去了部署
   * */
  @Bean
  public SpringProcessEngineConfiguration springProcessEngineConfiguration() {
    SpringProcessEngineConfiguration spec = new SpringProcessEngineConfiguration();
    spec.setDataSource(dataSource);
    spec.setTransactionManager(platformTransactionManager);
    spec.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
    Resource[] resources = null;
    // 启动自动部署流程
    try {
      resources = new PathMatchingResourcePatternResolver().getResources("classpath*:process/*.bpmn");
    } catch (IOException e) {
      e.printStackTrace();
    }
    spec.setDeploymentResources(resources);
    return spec;
  }
}

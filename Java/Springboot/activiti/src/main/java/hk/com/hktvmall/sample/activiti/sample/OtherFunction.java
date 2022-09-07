package hk.com.hktvmall.sample.activiti.sample;

import java.util.Date;
import java.util.List;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OtherFunction {
  private ProcessEngine pe;
  private Logger logger = LoggerFactory.getLogger(OtherFunction.class);

  public OtherFunction() {
    pe = ProcessEngines.getDefaultProcessEngine();
  }

  /**
   * 打印出某個流程的明細
   * @param taskId
   */
  public void printHisRecord(String taskId){
    HistoryService hs = pe.getHistoryService();
    List<HistoricActivityInstance> hisList = hs.createHistoricActivityInstanceQuery().processInstanceId(taskId).list();
    hisList.forEach(his -> {
      logger.info(his.toString());
    });
  }

  /**
   * 暫停流程
   * @param processKey
   */
  public void suspendProcess(String processKey) {
    RepositoryService rs = pe.getRepositoryService();
    ProcessDefinition pd = rs.createProcessDefinitionQuery().processDefinitionKey(processKey).singleResult();
    if(!pd.isSuspended()) {
      rs.suspendProcessDefinitionByKey(processKey);
    }
  }

  /**
   * 啟動流程
   * @param processKey
   */
  public void activeProcess(String processKey) {
    RepositoryService rs = pe.getRepositoryService();
    ProcessDefinition pd = rs.createProcessDefinitionQuery().processDefinitionKey(processKey).singleResult();
    if(pd.isSuspended()) {
      rs.activateProcessDefinitionByKey(processKey);
    }
  }

}

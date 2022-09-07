package hk.com.hktvmall.sample.activiti.sample;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LeaveSample {

  private Logger logger = LoggerFactory.getLogger(LeaveSample.class);
  private ProcessEngine pe;
  private TaskService ts;
  private String taskId = "42501";

  public void test() {
    //init Activiti process
    pe = ProcessEngines.getDefaultProcessEngine();
    ts = pe.getTaskService();

//    啟動流程
    startProcess();
//    完成流程第一步(填寫表單)並送至下一步
    apply();
//    撈出申請單填寫資料
    showTaskVariable();
    //審核，並填上意見
    audit();
  }

  private void deployProcess() {
    pe.getRepositoryService().createDeployment();
  }

  private void startProcess() {
    String instanceKey = "leaveProcess";
    logger.info("開啟請假流程...");
    Map<String, Object> map = new HashMap<String, Object>();
    //在holiday.bpmn中,填寫請假單的任務辦理人為動態傳入的userId,此處模擬一個id
    map.put("userId", "10001");

    ProcessInstance instance = pe.getRuntimeService().startProcessInstanceByKey(instanceKey, map);
    logger.info("啟動流程例項成功:{}", instance);
    logger.info("流程例項ID(要記錄起來，後面才能繼續流程):{}", instance.getId());
    logger.info("請更新taskId[" + instance.getId() + "]");
    this.taskId = instance.getId();
    logger.info("流程定義ID:{}", instance.getProcessDefinitionId());
  }

  private void apply() {
    //定義參數
    String leaveDays = "10"; // 請假天數
    String leaveReason = "回老家結婚"; // 請假原因

    //建立任務
    Task task = ts.createTaskQuery().processInstanceId(taskId).singleResult();
    if (task == null) {
      logger.info("任務ID:{}查詢到任務為空！", taskId);
    }
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("days", leaveDays);
    map.put("date", new Date());
    map.put("reason", leaveReason);
    ts.complete(task.getId(), map);

    logger.info("執行【員工申請】環節，流程推動到【上級稽核】環節");
  }

  public void showTaskVariable() {
    Task task = ts.createTaskQuery().processInstanceId(taskId).singleResult();
    String days = (String) ts.getVariable(task.getId(), "days");
    Date date = (Date) ts.getVariable(task.getId(), "date");
    String reason = (String) ts.getVariable(task.getId(), "reason");
    String userId = (String) ts.getVariable(task.getId(), "userId");
    System.out.println("請假天數:  " + days);
    System.out.println("請假理由:  " + reason);
    System.out.println("請假人id:  " + userId);
    System.out.println("請假日期:  " + date.toString());
  }

  private void audit() {
    String departmentalOpinion = "早去早回";
    Task task = ts.createTaskQuery().processInstanceId(taskId).singleResult();
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("departmentalOpinion", departmentalOpinion);
    ts.complete(task.getId(), map);
    logger.info("新增審批意見,請假流程結束");
  }
}

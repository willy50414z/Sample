package hk.com.hktvmall.sample.activiti.sample;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Leave2ListenerSample {

  private Logger logger = LoggerFactory.getLogger(Leave2ListenerSample.class);
  private ProcessEngine pe;
  private TaskService taskService;
  private String taskId = "17501";
  private String busKey = "busKey" + new Date().getTime();

  public void test() {
    //init Activiti process
    pe = ProcessEngines.getDefaultProcessEngine();
    taskService = pe.getTaskService();

//    啟動流程
    logger.info("***開啟請假流程***");
    startProcess();
//    完成流程第一步(填寫表單)並送至下一步
    logger.info("***填寫請假資料***");
    apply();
//    撈出申請單填寫資料
    logger.info("***撈出請假資料待核對***");
    showTaskVariable();
    //審核，並填上意見
    logger.info("***撈出經理群的代辦資料覆核***");
    auditByManager();
    logger.info("***撈出老闆群的代辦資料覆核***");
    auditByBoss();
    logger.info("***撈出老李的代辦資料覆核***");
    auditByOldLee();
  }

  private void deployProcess() {
    pe.getRepositoryService().createDeployment();
  }

  private void startProcess() {
    String instanceKey = "leaveProcess2listener";
    Map<String, Object> map = new HashMap<String, Object>();
    //在holiday.bpmn中,填寫請假單的任務辦理人為動態傳入的userId,此處模擬一個id
    map.put("userId", "10001");

    ProcessInstance instance = pe.getRuntimeService().startProcessInstanceByKey(instanceKey, busKey, map);
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
    Task task = taskService.createTaskQuery().processInstanceId(taskId).singleResult();
    if (task == null) {
      logger.info("任務ID:{}查詢到任務為空！", taskId);
      return;
    }
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("days", leaveDays);
    map.put("date", new Date());
    map.put("reason", leaveReason);
    task.setAssignee("老王");
    taskService.complete(task.getId(), map);

    logger.info("執行【員工申請】環節，流程推動到【上級稽核】環節");
  }

  public void showTaskVariable() {
//    Task task = taskService.createTaskQuery().processInstanceId(taskId).singleResult();
    Task task = taskService.createTaskQuery().processInstanceBusinessKey(busKey).singleResult();
    String days = (String) taskService.getVariable(task.getId(), "days");
    Date date = (Date) taskService.getVariable(task.getId(), "date");
    String reason = (String) taskService.getVariable(task.getId(), "reason");
    String userId = (String) taskService.getVariable(task.getId(), "userId");
    System.out.println("請假天數:  " + days);
    System.out.println("請假理由:  " + reason);
    System.out.println("請假人id:  " + userId);
    System.out.println("請假日期:  " + date.toString());
    System.out.println("承辦人員:  " + task.getAssignee());
  }

  private void auditByManager() {
    //撈出經理的任務逐筆做
    List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("manager").list();// 经理组的任务
    for (Task task : tasks) {
      System.out.println("经理组的任务taskname:" + task.getName() + ",id:" + task.getId() + ",assignee:"+task.getAssignee());
      taskService.claim(task.getId(), "李四");//申领任务
      taskService.setVariable(task.getId(), "flag", true);//true批准，false不批准
      Object applyUser = taskService.getVariable(task.getId(), "userId");
      Object day = taskService.getVariable(task.getId(), "days");
      System.out.println(String.format("%s请假%s天", applyUser, day));
      taskService.complete(task.getId());//完成任务
    }
  }

  private void auditByBoss() {
    //撈出老闆的任務逐筆做
    List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("boss").list();// 经理组的任务
    for (Task task : tasks) {
      System.out.println("董事長组的任务taskname:" + task.getName() + ",id:" + task.getId() + ",assignee:"+task.getAssignee());
      taskService.claim(task.getId(), "陳五");//申领任务
      taskService.setVariable(task.getId(), "flag", true);//true批准，false不批准
      Object applyUser = taskService.getVariable(task.getId(), "userId");
      Object day = taskService.getVariable(task.getId(), "days");
      System.out.println(String.format("%s请假%s天", applyUser, day));
      taskService.complete(task.getId());//完成任务
    }
  }

  private void auditByOldLee() {
    //撈出老闆的任務逐筆做
    List<Task> tasks = taskService.createTaskQuery().taskAssignee("老李").list();// 老李的任务
    for (Task task : tasks) {
      System.out.println("老李的任务taskname:" + task.getName() + ",id:" + task.getId() + ",assignee:"+task.getAssignee());
      taskService.claim(task.getId(), "老李");//申领任务
      taskService.setVariable(task.getId(), "flag", false);//true批准，false不批准
      Object applyUser = taskService.getVariable(task.getId(), "userId");
      Object day = taskService.getVariable(task.getId(), "days");
      System.out.println(String.format("%s请假%s天", applyUser, day));
      taskService.complete(task.getId());//完成任务
    }
  }

  private void findTaskStatus (String taskId){
    Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
    logger.info("task name["+task+"]");
  }
}

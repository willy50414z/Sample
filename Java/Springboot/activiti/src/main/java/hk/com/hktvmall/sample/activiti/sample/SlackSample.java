package hk.com.hktvmall.sample.activiti.sample;

import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

@Slf4j
public class SlackSample {

  private String processId;
  private ProcessEngine pe;

  private String bpmnFileName = "slack2";
  private String processName = "slcak2process";
  private TaskService taskService;
  public void test() throws Exception {
    //init Activiti process
    pe = ProcessEngines.getDefaultProcessEngine();
    taskService = pe.getTaskService();

    //啟動流程
    this.startProcess();

    //執行流程
    this.executeProcess();
    this.executeProcess();
    this.executeProcess();
    this.executeProcess();
    this.executeProcess();
    this.executeProcess();
  }

  private void startProcess(){
    String instanceKey = processName;
    log.info("開啟Slack流程...");
    HashMap<String, Object> map = new HashMap<>();
    map.put("channelMute", false);
    map.put("TAS", false);
    map.put("isDnD", false);
    map.put("UserInDnD", false);
    map.put("isDnDOverride", false);
    map.put("isCMS", false);
    map.put("isCEHMessage", false);
    map.put("isTEPO", false);
    map.put("isCNNothing", false);
    map.put("pref", "Default");
    map.put("mentions", "here");
    map.put("isTM", false);
    map.put("isUS", false);
    map.put("isCMU", false);
    map.put("isUPA", false);
    map.put("globalPref", "Highlight");
    ProcessInstance instance = pe.getRuntimeService().startProcessInstanceByKey(instanceKey, map);
    this.processId = instance.getId();
    log.info("流程ID["+this.processId+"]");

//    instanceKey = "leaveProcess2";
//    log.info("開啟Slack流程...");
//    instance = pe.getRuntimeService().startProcessInstanceByKey(instanceKey);
//    this.processId = instance.getId();
//    log.info("流程ID["+this.processId+"]");
  }

  private void executeProcess(){
    Task task = taskService.createTaskQuery().processInstanceId(this.processId).singleResult();
    if (task == null) {
      log.info("任務ID:{}查詢到任務為空！", this.processId);
      return;
    } else {
      log.info("***當前流程為["+task.getName()+"], Assignee["+task.getAssignee()+"]***");
      taskService.complete(task.getId());
    }

  }
}

package hk.com.hktvmall.sample.activiti;

import hk.com.hktvmall.sample.activiti.sample.Leave2ListenerSample;
import hk.com.hktvmall.sample.activiti.sample.Leave2Sample;
import hk.com.hktvmall.sample.activiti.sample.LeaveSample;
import hk.com.hktvmall.sample.activiti.sample.OtherFunction;
import hk.com.hktvmall.sample.activiti.sample.SlackSample;
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
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ActivitiApplication implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication.run(ActivitiApplication.class, args);
  }



  @Override
  public void run(String... args) throws Exception {
//    new LeaveSample().test();
//    new Leave2Sample().test();
//    new SlackSample().test();
//    new Leave2ListenerSample().test();

//    new OtherFunction().printHisRecord("42501");
//    new OtherFunction().suspendProcess("leaveProcess2");
//    new OtherFunction().activeProcess("leaveProcess2");

    System.exit(0);
  }
}

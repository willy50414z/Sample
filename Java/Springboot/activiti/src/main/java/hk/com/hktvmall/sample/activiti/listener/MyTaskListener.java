package hk.com.hktvmall.sample.activiti.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyTaskListener implements TaskListener {
  private Logger logger = LoggerFactory.getLogger(MyTaskListener.class);
  @Override
  public void notify(DelegateTask delegateTask) {
    logger.info("Listened the notification from ["+delegateTask.getName()+"] Event["+delegateTask.getEventName()+"]");
  }
}

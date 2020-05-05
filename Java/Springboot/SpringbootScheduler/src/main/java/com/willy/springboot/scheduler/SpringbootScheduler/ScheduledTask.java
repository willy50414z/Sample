package com.willy.springboot.scheduler.SpringbootScheduler;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Integer count0 = 1;
    private Integer count1 = 1;
    private Integer count2 = 1;

    //5000毫秒執行一次
    @Scheduled(fixedRate = 5000)
    public void execEveryMs() throws InterruptedException {
        System.out.println(String.format("execEveryMs 第%s次执行，当前时间为：%s", count0++, dateFormat.format(new Date())));
    }

    //執行完成後5000毫秒再執行一次
    @Scheduled(fixedDelay = 5000)
    public void execAfterLastExecFinishMs() throws InterruptedException {
        System.out.println(String.format("execAfterLastExecFinishMs 第%s次执行，当前时间为：%s", count1++, dateFormat.format(new Date())));
    }

    //cron表達式
    // 分鐘 小時 日 月 星期
    
	    //每隔 10 分鐘執行一次
	    // */10 * * * *
    
	    //從早上 9 點到下午 6 點，凡遇到整點就執行
	    // 00 09-18 * * * 
    
    	//每月 1 日、15 日、29 日晚上 9 點 30 分各執行一次
    	//30 21 1,15,29 * *
    @Scheduled(cron = "*/1 * * * * 2")//每星期二每分鐘執行一次
    public void cronJob() throws InterruptedException {
        System.out.println(String.format("cronJob 第%s次执行，当前时间为：%s", count2++, dateFormat.format(new Date())));
    }

}

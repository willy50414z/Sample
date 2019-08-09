package com.willy.clouldcustomerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableHystrixDashboard
public class ClouldHystrixDashboard 
{
    public static void main( String[] args )
    {
        SpringApplication.run(ClouldHystrixDashboard.class, args);
    }
}

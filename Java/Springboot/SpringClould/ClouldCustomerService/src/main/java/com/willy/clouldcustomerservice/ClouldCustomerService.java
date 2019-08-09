package com.willy.clouldcustomerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableEurekaClient
public class ClouldCustomerService 
{
    public static void main( String[] args )
    {
        SpringApplication.run(ClouldCustomerService.class, args);
    }
}

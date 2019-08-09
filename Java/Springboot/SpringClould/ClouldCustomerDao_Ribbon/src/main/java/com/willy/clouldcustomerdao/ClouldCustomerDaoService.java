package com.willy.clouldcustomerdao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableEurekaClient
public class ClouldCustomerDaoService 
{
    public static void main( String[] args )
    {
        SpringApplication.run(ClouldCustomerDaoService.class, args);
    }
}

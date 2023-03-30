package com.willy.springboot;

import com.willy.springboot.dto.ConfigData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConfigClientApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(ConfigClientApplication.class, args);
    }

    @Autowired private ConfigData configData;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(configData.toString());
    }
}

package com.willy.spring;

import org.h2.tools.DeleteDbFiles;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootSecuritySampleApplication {

	public static void main(String[] args) {
		DeleteDbFiles.execute("D:/", "SpringSecurity", true);
		SpringApplication.run(SpringBootSecuritySampleApplication.class, args);
	}

}

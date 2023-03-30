package com.willy.sample.zookeeperSample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ZkApplication implements CommandLineRunner
{

	public static void main(String[] args) {
		SpringApplication.run(ZkApplication.class, args);
	}


	@Autowired
	private ZkDao zk;

	@Override
	public void run(String... args) throws Exception
	{
		zk.create("/customer", "Willy");
	}
}

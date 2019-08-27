package com.willy.springbootprofile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import com.willy.springbootprofile.Config.ConfigBean;
@Component
public class AppRunner  implements CommandLineRunner{
	@Autowired
	private ConfigBean configBean;
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(configBean.toString());
	}
}

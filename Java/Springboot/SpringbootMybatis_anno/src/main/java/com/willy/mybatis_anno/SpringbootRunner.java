package com.willy.mybatis_anno;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.willy.mybatis_anno.mapper.CityMapper;
@Component
public class SpringbootRunner implements CommandLineRunner{
	@Autowired
	private CityMapper cityMapper;

	  @Override
	  public void run(String... args) {
	    System.out.println(this.cityMapper.findByState("CA"));
	  }
}

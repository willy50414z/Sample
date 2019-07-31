package com.willy.springbootjpa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.willy.springbootjpa.dao.PersonRepository;
import com.willy.springbootjpa.entity.Person;
@Component
public class SpringbootRunner implements CommandLineRunner{
//	@Autowired
//	DemoServiceImpl service;
	@Autowired
	PersonRepository personRepository; //1
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		List<Person> pList = personRepository.findAll();
		for(Person p : pList){
			System.out.println(p.getName());
		}
	}

}

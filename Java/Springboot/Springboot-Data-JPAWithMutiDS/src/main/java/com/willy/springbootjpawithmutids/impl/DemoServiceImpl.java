package com.willy.springbootjpawithmutids.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.willy.springbootjpawithmutids.dao1.PersonRepository1;
import com.willy.springbootjpawithmutids.entity.Person;
import com.willy.springbootjpawithmutids.service.DemoService;
@Service
public class DemoServiceImpl implements DemoService {
	@Autowired
	PersonRepository1 personRepository; //1
	
	@Transactional(rollbackFor={IllegalArgumentException.class}) //2
	public Person savePersonWithRollBack(Person person){
		Person p =personRepository.save(person);

		if(person.getName().equals("汪云飞")){
			throw new IllegalArgumentException("汪云飞已存在，数据将回滚"); //3
		}
		return p;
	}

	@Transactional(noRollbackFor={IllegalArgumentException.class}) //4
	public Person savePersonWithoutRollBack(Person person){
		Person p =personRepository.save(person);
		
		if(person.getName().equals("汪云飞")){
			throw new IllegalArgumentException("汪云飞虽已存在，数据将不会回滚");
		}
		return p;
	}
}

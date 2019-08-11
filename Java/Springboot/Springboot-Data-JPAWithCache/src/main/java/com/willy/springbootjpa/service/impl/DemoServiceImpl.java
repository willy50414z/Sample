package com.willy.springbootjpa.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.willy.springbootjpa.dao.PersonRepository;
import com.willy.springbootjpa.entity.Person;
import com.willy.springbootjpa.service.DemoService;
@Service
public class DemoServiceImpl implements DemoService {
	@Autowired
	PersonRepository personRepository; //1

	@Override
	@Cacheable(value="Person",key="#person.id")//若Cache中沒有名為Person此ID的值，將它塞入Cache
	public Person findById(Person person) {
		// TODO Auto-generated method stub
		return personRepository.findById(person.getId()).orElse(new Person());
	}

	@Override
	//save會自動更新Cache
	public void save(Person person) {
		// TODO Auto-generated method stub
		personRepository.save(person);
	}

	@Override
//	@CacheEvict(value="Person")//刪除該ID在Cache保留的值
	public void delete(Person person) {
		// TODO Auto-generated method stub
		personRepository.delete(person);
	}

	@Override
	public List<Person> findAll() {
		// TODO Auto-generated method stub
		return personRepository.findAll();
	}
	
}

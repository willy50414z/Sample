package com.willy.springbootjpa.service;

import java.util.List;

import com.willy.springbootjpa.entity.Person;

public interface DemoService {
	public Person findById(Person person);
	public void save(Person person);
	public void delete(Person person);
	public List<Person> findAll();
}

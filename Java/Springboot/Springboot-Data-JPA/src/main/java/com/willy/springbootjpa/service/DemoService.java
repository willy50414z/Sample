package com.willy.springbootjpa.service;

import com.willy.springbootjpa.entity.Person;

public interface DemoService {
	public Person savePersonWithRollBack(Person person);
	public Person savePersonWithoutRollBack(Person person);

}

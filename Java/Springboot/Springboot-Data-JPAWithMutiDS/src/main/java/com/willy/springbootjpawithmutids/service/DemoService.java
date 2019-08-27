package com.willy.springbootjpawithmutids.service;

import com.willy.springbootjpawithmutids.entity.Person;

public interface DemoService {
	public Person savePersonWithRollBack(Person person);
	public Person savePersonWithoutRollBack(Person person);

}

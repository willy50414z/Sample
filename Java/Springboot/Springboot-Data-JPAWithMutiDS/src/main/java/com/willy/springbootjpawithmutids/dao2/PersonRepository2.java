package com.willy.springbootjpawithmutids.dao2;

import org.springframework.data.jpa.repository.JpaRepository;

import com.willy.springbootjpawithmutids.entity.Person;

public interface PersonRepository2 extends JpaRepository<Person, Long> {
	

}

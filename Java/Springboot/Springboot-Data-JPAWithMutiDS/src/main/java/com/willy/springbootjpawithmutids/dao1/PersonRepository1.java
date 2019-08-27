package com.willy.springbootjpawithmutids.dao1;

import org.springframework.data.jpa.repository.JpaRepository;

import com.willy.springbootjpawithmutids.entity.Person;

public interface PersonRepository1 extends JpaRepository<Person, Long> {
	

}

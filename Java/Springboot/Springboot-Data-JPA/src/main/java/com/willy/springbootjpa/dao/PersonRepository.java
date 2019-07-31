package com.willy.springbootjpa.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.willy.springbootjpa.entity.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
	

}

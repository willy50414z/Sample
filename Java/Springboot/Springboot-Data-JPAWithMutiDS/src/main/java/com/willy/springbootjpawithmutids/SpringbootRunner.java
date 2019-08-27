package com.willy.springbootjpawithmutids;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.willy.springbootjpawithmutids.dao1.PersonRepository1;
import com.willy.springbootjpawithmutids.dao2.PersonRepository2;
import com.willy.springbootjpawithmutids.entity.Person;
@Component
public class SpringbootRunner implements CommandLineRunner{
	@Autowired
	PersonRepository1 personRepository1;
	@Autowired
	PersonRepository2 personRepository2;
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
//		Person ds1Person1 = new Person(1,"Willy",30,"台北1號");
//		Person ds1Person2 = new Person(2,"Hank",40,"台北2號");
//		Person ds2Person1 = new Person(1,"Judy",50,"台北3號");
//		Person ds2Person2 = new Person(2,"Yami",60,"台北4號");
		System.out.println(" --- DataSources1 資料 --- ");
		List<Person> pList = personRepository1.findAll();
		for(Person p : pList){
			System.out.println(p.getName());
		}
		System.out.println(" --- DataSources2 資料 --- ");
		pList = personRepository2.findAll();
		for(Person p : pList){
			System.out.println(p.getName());
		}
	}

}

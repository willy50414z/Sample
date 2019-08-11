package com.willy.springbootjpa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Component;

import com.willy.springbootjpa.entity.Person;
import com.willy.springbootjpa.service.impl.DemoServiceImpl;
@Component
@EnableCaching//啟用Cache
public class SpringbootRunner implements CommandLineRunner{
//	@Autowired
//	DemoServiceImpl service;
	@Autowired
	DemoServiceImpl daoService; //1
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("---先列出資料庫中所有人員名單---");
		List<Person> pList = daoService.findAll();
		for(Person p : pList){
			System.out.println(p.toString());
		}
		Person p = new Person();
		
		System.out.println("---撈出ID為1的資料，並加入Cache---");
		p.setId(1L);
		p = daoService.findById(p);
		
		System.out.println("---刪除ID為1的資料---");
		daoService.delete(p);
		
		System.out.println("---撈出ID為2的資料，並加入Cache---");
		p.setId(2L);
		p = daoService.findById(p);
		
		System.out.println("---更新ID為2的資料---");
		p.setAge(1000);
		daoService.save(p);
		
		System.out.println("---驗證---");
		System.out.println("---印出ID為1的資料(即使已被刪除，仍會從cache中取出，因delete方法沒有執行CacheEvict)---");
		p.setId(1L);
		System.out.println(daoService.findById(p));
		
		System.out.println("---印出ID為2的資料(save會自動更新Cache，因此會印出新資料)---");
		p.setId(2L);
		System.out.println(daoService.findById(p));
		
		System.out.println("---印出所有資料(不從cache中取出)---");
		pList = daoService.findAll();
		for(Person p1 : pList){
			System.out.println(p1.toString());
		}
	}

}

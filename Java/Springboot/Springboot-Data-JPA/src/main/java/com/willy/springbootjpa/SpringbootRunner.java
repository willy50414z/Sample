package com.willy.springbootjpa;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.willy.springbootjpa.dao.PersonRepository;
import com.willy.springbootjpa.dao.MultiPKTableRepository;
import com.willy.springbootjpa.entity.MultiPKPo;
import com.willy.springbootjpa.entity.MultiPKPoPK;
import com.willy.springbootjpa.entity.Person;

@Component
public class SpringbootRunner implements CommandLineRunner {
	@Autowired
	PersonRepository personRepository; // 1
	@Autowired
	MultiPKTableRepository ur;

	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		List<Person> pList = personRepository.findAll();
		for (Person p : pList) {
			System.out.println(p.getName());
		}

		//複合PK
		MultiPKPo uf1 = new MultiPKPo();
		// 必須手動設定主鍵
		uf1.setUserFundPK(new MultiPKPoPK(1, "2017-07-01"));
		uf1.setRate(new BigDecimal("0.041"));
		uf1.setPrincipal(new BigDecimal("10000"));
		// 截斷，只保留兩位小數
		uf1.setInterest(uf1.getRate().multiply(uf1.getPrincipal()).divide(new BigDecimal("365"), 2, RoundingMode.DOWN));

		MultiPKPo uf2 = new MultiPKPo();
		// 必須手動設定主鍵
		uf2.setUserFundPK(new MultiPKPoPK(2, "2017-07-01"));
		uf2.setRate(new BigDecimal("0.041"));
		uf2.setPrincipal(new BigDecimal("20000"));
		// 截斷，只保留兩位小數
		uf2.setInterest(uf2.getRate().multiply(uf2.getPrincipal()).divide(new BigDecimal("365"), 2, RoundingMode.DOWN));

		ur.save(uf1);
		ur.save(uf2);

		ur.findAll().forEach(System.out::println);
	}

}

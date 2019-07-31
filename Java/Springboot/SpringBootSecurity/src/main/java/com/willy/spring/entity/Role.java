package com.willy.spring.entity;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;

@Data
public class Role implements GrantedAuthority{
	private Long id;
    private String name;
	@Override
	public String getAuthority() {
		// TODO Auto-generated method stub
		return name;
	}
}

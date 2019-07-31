package com.willy.spring.entity;

import java.io.Serializable;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;
@Data
public class User  implements UserDetails , Serializable{
	private Long id;
    private String username;
    private String password;
    private List<Role> authorities;
	@Override
    public List<Role> getAuthorities() {
        return authorities;
    }
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return username;
	}
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
    
}

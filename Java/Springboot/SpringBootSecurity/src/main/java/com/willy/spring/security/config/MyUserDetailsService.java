package com.willy.spring.security.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.willy.spring.entity.Role;
import com.willy.spring.entity.User;
import com.willy.spring.mapper.RoleMapper;
import com.willy.spring.mapper.UserMapper;
//以使用者名稱取得所屬角色
//實現Security提供的UserDetailsService接口，定義登入時以UserName取得該User所有角色的方法
@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserMapper userDao;
    @Autowired
    private RoleMapper roleDao;
public static void main(String[] args) {
	System.out.println(DigestUtils.md5DigestAsHex("willy50414z".toString().getBytes()));


}
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        //查数据库
        User user = userDao.loadUserByUsername( userName );
        if (null != user) {
            List<Role> roles = roleDao.getRolesByUserId(user.getId());
            user.setAuthorities(roles);
        }
        return user;
    }
}

package com.willy.spring.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;

//驗證密碼並註冊登入頁、驗證頁面黑白名單、錯誤頁面
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
    private MyUserDetailsService userService;

    /**
     * 設定驗證方式
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        //驗證客戶資訊
        auth.userDetailsService( userService ).passwordEncoder( new PasswordEncoder() {
            //對密碼進行加密
            @Override
            public String encode(CharSequence charSequence) {
                return DigestUtils.md5DigestAsHex(charSequence.toString().getBytes());
            }
            //對密碼進行判斷匹配
            @Override
            public boolean matches(CharSequence charSequence, String s) {
                String encode = DigestUtils.md5DigestAsHex(charSequence.toString().getBytes());
                boolean res = s.equals( encode );
                return res;
            }
        } );

    }

    /**
     * 設定請求資源攔截邏輯
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
        		//這些request不需攔截
                .antMatchers("/","index","/login","/login-error","/401","/css/**","/js/**").permitAll()
                //設定所有request皆需攔截
                .anyRequest().authenticated()
                .and()
                //設定登入頁及失敗頁
                .formLogin().loginPage( "/login" ).failureUrl( "/login-error" )
                .and()
                //error頁面
                .exceptionHandling().accessDeniedPage( "/401" );
        //登出頁
        http.logout().logoutSuccessUrl( "/" );
    }
}

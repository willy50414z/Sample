package com.willy.spring.security.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import com.willy.spring.entity.RolePermission;
import com.willy.spring.mapper.PermissionMapper;

//回傳角色有權使用的資源路徑
//實現Security提供的FilterInvocationSecurityMetadataSource接口
@Component
public class MyResourcesRoleMapService implements FilterInvocationSecurityMetadataSource {

	 @Autowired
	    private PermissionMapper permissionDao;

	    /**
	     * 每一个资源所需要的角色 Collection<ConfigAttribute>决策器会用到
	     */
	    private static HashMap<String, Collection<ConfigAttribute>> map =null;


	    /**
	     * 回傳請求資源的授權使用者
	     */
	    @Override
	    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
	        HttpServletRequest request = ((FilterInvocation) o).getHttpRequest();
	        for (Iterator<String> it = map.keySet().iterator() ; it.hasNext();) {
	            String url = it.next();
	            if (new AntPathRequestMatcher( url ).matches( request )) {
	                return map.get( url );
	            }
	        }

	        return null;
	    }

	    /**
	     * 載入所有<資源,授權使用者>Map
	     */
	    @Override
	    public Collection<ConfigAttribute> getAllConfigAttributes() {
	    	map = new HashMap<>(16);
	        //撈出權限、資源對應
	        List<RolePermission> rolePermissons = permissionDao.getRolePermissions();
	        Collection<ConfigAttribute> caList = new ArrayList<ConfigAttribute>();
	        //某个资源 可以被哪些角色访问
	        for (RolePermission rolePermisson : rolePermissons) {
	            String url = rolePermisson.getUrl();
	            String roleName = rolePermisson.getRoleName();
	            ConfigAttribute role = new SecurityConfig(roleName);
	            caList.add(role);
	            //將各<資源(url),有權角色(SecurityConfig)>放入map
	            if(map.containsKey(url)){
	                map.get(url).add(role);
	            }else{
	                List<ConfigAttribute> list =  new ArrayList<>();
	                list.add( role );
	                map.put( url , list );
	            }
	        }
	        return null;
	    }
	    @Override
	    public boolean supports(Class<?> aClass) {
	        return true;
	    }
}
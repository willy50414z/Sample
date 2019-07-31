package com.willy.spring.security.config;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

//判斷該角色是否有權訪問
@Component
public class MyAccessDecisionManager implements AccessDecisionManager {
	/**
	 * 通過傳遞的參數來決定用戶是否有訪問對應受保護物件的許可權
	 *
	 * @param authentication
	 *            包含了當前的使用者資訊，包括擁有的許可權。這裡的許可權來源就是前面登錄時UserDetailsService中設置的authorities。
	 * @param object
	 *            就是FilterInvocation物件，可以得到request等web資源
	 * @param configAttributes
	 *            資源與有權限角色對應，由MyResourcesRoleMappingService.getAttributes()取得
	 */
	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException, InsufficientAuthenticationException {
		if (null == configAttributes || 0 >= configAttributes.size()) {
			return;
		} else {
			String needRole;
			// 遍覽User的角色與各資源的有權角色，驗證該資源是否可被訪問
			for (Iterator<ConfigAttribute> iter = configAttributes.iterator(); iter.hasNext();) {
				needRole = iter.next().getAttribute();

				for (GrantedAuthority ga : authentication.getAuthorities()) {
					if (needRole.trim().equals(ga.getAuthority().trim())) {
						return;
					}
				}
			}

			throw new AccessDeniedException("當前訪問沒有許可權");
		}

	}

	/**
	 * 表示此AccessDecisionManager是否能夠處理傳遞的ConfigAttribute呈現的授權請求
	 */
	@Override
	public boolean supports(ConfigAttribute configAttribute) {
		return true;
	}

	/**
	 * 表示當前AccessDecisionManager實現是否能夠為指定的安全物件（方法調用或Web請求）提供存取控制決策
	 */
	@Override
	public boolean supports(Class<?> aClass) {
		return true;
	}

}

package com.mierro.authority.shiro;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;

/**
 * <p>Title:CustomUrlPermissonResovler </p>
 * <p>Description:自定义shiro权限解析器 </p>
 * <p>Company:Wteam </p> 
 *  @author Wteam 李焕滨 86571705@qq.com
 *  @date 2016年4月12日 下午9:27:40
 */
public class CustomUrlPermissonResovler implements PermissionResolver {

	@Override
	public Permission resolvePermission(String arg0) {
		
		if(arg0.startsWith("/")) {
			return new CustomUrlPermission(arg0);
		}
		
		return new WildcardPermission(arg0);
	}
}

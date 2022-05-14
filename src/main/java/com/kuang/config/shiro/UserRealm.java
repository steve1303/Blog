package com.kuang.config.shiro;

import com.kuang.pojo.User;
import com.kuang.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    UserService userService;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //System.out.println("执行了授权");

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        //info.addStringPermission("user:add");

        //拿到当前用户登陆对象
        Subject subject = SecurityUtils.getSubject();
        //拿到User对象
        User currentUser = (User) subject.getPrincipal();
        //设置当前用户对象
        info.addStringPermission(currentUser.getUserPerms());

        return info;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //System.out.println("执行了认证");

        //用户名，密码，数据库中获取
        UsernamePasswordToken userToken = (UsernamePasswordToken) authenticationToken;
        //获取用户名
        User user = userService.queryUserByName(userToken.getUsername());

        // 空指针
        String username = user.getUserName();
        String password = user.getUserPassword();

        System.out.println("[realm]"+username);
        System.out.println("[realm]"+password);

        if (user == null) {
            //说明查无此人
            return null;
        }

        //密码认证,shiro做
        return new SimpleAuthenticationInfo(user, password, "");//放入User对象

    }

}


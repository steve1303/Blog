package com.kuang.config.shiro;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    /*
    shiro主要有三大功能模块：
    1. Subject：主体，一般指用户。
    2. SecurityManager：安全管理器，管理所有Subject，可以配合内部安全组件。(类似于SpringMVC中的DispatcherServlet)
    3. Realms：用于进行权限信息的验证，一般需要自己实现。
    */

    //ShiroFilterBean  3
    //Filter工厂，设置对应的过滤条件和跳转条件
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("getDefaultWebSecurityManager") @Lazy DefaultWebSecurityManager defaultWebSecurityManager) {

        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        //设置安全管理器
        bean.setSecurityManager(defaultWebSecurityManager);

        //添加shiro的内置过滤器
        /*
            anon:无需认证就能访问 匿名访问
            authc:必须认证才能访问
            user:必须拥有记住我功能才能访问
            perms:拥有某个资源的权限才能访问
            role:拥有某个角色权限才能访问
         */
        // 拦截
        Map<String, String> filterMap = new LinkedHashMap<>();


        //授权 如果未授权 跳转至未授权页面
        // user:admin 管理员 可以进入 标签 评论 crud页面
        filterMap.put("/user/*", "perms[user:admin]");
        filterMap.put("/tag/*", "perms[user:admin]");
        filterMap.put("/comment/*", "perms[user:admin]");

        filterMap.put("/user/userMsg/3","authc");
        //设置登陆的请求
        bean.setLoginUrl("/toLogin");
        // 登出 注销 ok
        filterMap.put("/user/logout", "logout");

        //设置未授权的请求
        bean.setUnauthorizedUrl("auth");

        bean.setFilterChainDefinitionMap(filterMap);

        return bean;
    }

    // DefaultWebSecurityManager 2
    // 安全管理器
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        //关联UserRealm
        securityManager.setRealm(userRealm);

        return securityManager;
    }


    //创建realm对象 需要自定义 1
    @Bean
    public UserRealm userRealm() {
        return new UserRealm();
    }

    //整合shiroDialect:用来整合shiro thymeleaf
    @Bean
    public ShiroDialect getShiroDialect() {
        return new ShiroDialect();
    }





    //开启对shiro注解的支持
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    //开启aop注解支持
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }


}

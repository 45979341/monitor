package com.zhuzhou.impl.shiro;

import com.zhuzhou.spi.sys.SysUserService;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionStorageEvaluator;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.Arrays;
import java.util.Map;

/**
 * @author xiechonghu
 */
@Configuration
public class ShiroConfig {

    /**
     * 注册shiro的Filter，拦截请求
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean(SecurityManager securityManager) throws Exception{
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter((Filter) shiroFilter(securityManager).getObject());
        filterRegistration.addInitParameter("targetFilterLifecycle", "true");
        filterRegistration.setAsyncSupported(true);
        filterRegistration.setEnabled(true);
        filterRegistration.setDispatcherTypes(DispatcherType.REQUEST);
        return filterRegistration;
    }

    /**
     * 初始化Authenticator
     */
    @Bean
    public Authenticator authenticator(SysUserService sysUserService) {
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        //设置两个Realm，一个用于用户登录验证和访问权限获取；一个用于jwt token的认证
        authenticator.setRealms(Arrays.asList(jwtShiroRealm(sysUserService), dbShiroRealm(sysUserService)));
        //设置多个realm认证策略，一个成功即跳过其它的
        authenticator.setAuthenticationStrategy(new FirstSuccessfulStrategy());
        return authenticator;
    }

    @Bean
    public SessionsSecurityManager securityManager(SysUserService sysUserService) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealms(Arrays.asList(jwtShiroRealm(sysUserService), dbShiroRealm(sysUserService)));
        return securityManager;
    }

    /**
     * 禁用session, 不保存用户登录状态。保证每次请求都重新认证。
     * 需要注意的是，如果用户代码里调用Subject.getSession()还是可以用session，如果要完全禁用，要配合下面的noSessionCreation的Filter来实现
     */
    @Bean
    protected SessionStorageEvaluator sessionStorageEvaluator(){
        DefaultWebSessionStorageEvaluator sessionStorageEvaluator = new DefaultWebSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        return sessionStorageEvaluator;
    }

    /**
     * 用于用户名密码登录时认证的realm
     */
    @Bean("dbRealm")
    public Realm dbShiroRealm(SysUserService sysUserService) {
        UserShiroRealm userShiroRealm = new UserShiroRealm(sysUserService);
        return userShiroRealm;
    }

    /**
     * 用于JWT token认证的realm
     */
    @Bean("jwtRealm")
    public Realm jwtShiroRealm(SysUserService sysUserService) {
        JwtShiroRealm jwtShiroRealm = new JwtShiroRealm(sysUserService);
        return jwtShiroRealm;
    }

    /**
     * 设置过滤器，将自定义的Filter加入
     */
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager);
        Map<String, Filter> filterMap = factoryBean.getFilters();
        filterMap.put("authcToken", createAuthFilter());
//        filterMap.put("anyRole", createRolesFilter());
        factoryBean.setFilters(filterMap);
        factoryBean.setFilterChainDefinitionMap(shiroFilterChainDefinition().getFilterChainMap());
        return factoryBean;
    }

    /**
     * 添加注解支持
     */
    @Bean
//    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib，防止重复代理和可能引起代理出错的问题
        // https://zhuanlan.zhihu.com/p/29161098
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    protected ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        //login不做认证，noSessionCreation的作用是用户在操作session时会抛异常
        chainDefinition.addPathDefinition("/sys/user/login", "noSessionCreation,anon");
        //对外接口，无需登录
        chainDefinition.addPathDefinition("/api/**/*", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/error/**/*", "noSessionCreation,anon");
        //导出接口，无需登录
        chainDefinition.addPathDefinition("/statistic/analysis/onerailexport", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/statistic/analysis/railexport", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/idx/analysis/export", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/lkj/export", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/lkj/test", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/lkj/data/test", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/phase/temp/list", "noSessionCreation,anon");

        //做用户认证，permissive参数的作用是当token无效时也允许请求访问，不会返回鉴权未通过的错误
        chainDefinition.addPathDefinition("/sys/user/logout", "noSessionCreation,authcToken[permissive]");
//        chainDefinition.addPathDefinition("/image/**", "anon");
        //只允许admin或manager角色的用户访问
//        chainDefinition.addPathDefinition("/sys/*", "noSessionCreation,authcToken,anyRole[admin]");
//        chainDefinition.addPathDefinition("/article/list", "noSessionCreation,authcToken");
//        chainDefinition.addPathDefinition("/article/*", "noSessionCreation,authcToken[permissive]");
        // 默认进行用户鉴权 测试时不需要,authcToken
         chainDefinition.addPathDefinition("/**", "noSessionCreation,authcToken");
        return chainDefinition;
    }

    //注意不要加@Bean注解，不然spring会自动注册成filter
    protected JwtFilter createAuthFilter(){
        return new JwtFilter();
    }

    //注意不要加@Bean注解，不然spring会自动注册成filter
//    protected AnyRolesAuthorizationFilter createRolesFilter(){
//        return new AnyRolesAuthorizationFilter();
//    }
}

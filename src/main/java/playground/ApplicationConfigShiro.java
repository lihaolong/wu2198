package playground;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import playground.security.WorkhourseRealm;

@Configuration
public class ApplicationConfigShiro {


    @Bean
    public ShiroFilterFactoryBean shiroFilter() {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager());
        return factoryBean;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager bean = new DefaultWebSecurityManager();
        bean.setRealm(workhourseRealm());
        bean.setSessionManager(sessionManager());
        return bean;
    }

    @Bean
    @DependsOn({"lifecycleBeanPostProcessor", "userService"})
    public WorkhourseRealm workhourseRealm() {
        return new WorkhourseRealm();
    }

    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(sessionDao());
        sessionManager.setGlobalSessionTimeout(43200000); // 12 hours
        return sessionManager;
    }

    @Bean
    public SessionDAO sessionDao() {
        return new MemorySessionDAO();
    }

    @Bean
    public HashedCredentialsMatcher credentialsMatcher() {
        HashedCredentialsMatcher bean = new HashedCredentialsMatcher();
        bean.setHashAlgorithmName("MD5");
        bean.setStoredCredentialsHexEncoded(true);
        bean.setHashIterations(1);
        return bean;
    }

    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public static DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager());
        return advisor;
    }
}

package com.security.acldoc;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.security.acldoc.security.MyPermissionEvaluator;
import com.security.acldoc.util.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.Md4PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

@Configuration
@Slf4j
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class AclSecurityConfig extends WebSecurityConfigurerAdapter {

/*    @Value("${spring.datasource.url}")
    private String jdbcUrl;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.driver-class-name}")
    private String driverClass;*/

@Autowired
DataSource dataSource;
    @Qualifier("customUserDetailsService")
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    MyPermissionEvaluator myPermissionEvaluator;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.jdbcAuthentication().dataSource(dataSource).usersByUsernameQuery("select username,password,true from" +
//                " tb_user where username=?").passwordEncoder(new Md4PasswordEncoder());
//        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
        auth.userDetailsService(userDetailsService).passwordEncoder(new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return MD5Utils.md5((String) charSequence);
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return s.equals(MD5Utils.md5((String)charSequence));
            }
        });
    }

   /* @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        log.info("CreateExpressionHandler() got called");

        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        try {
            expressionHandler.setPermissionEvaluator(new AclPermissionEvaluator(aclService()));
        } catch (PropertyVetoException e) {
            throw new RuntimeException("Error creating expressionHandler", e);
        }
        return expressionHandler;
    }*/

    @Bean
    public MutableAclService aclService() throws PropertyVetoException{
        final JdbcMutableAclService mutableAclService = new JdbcMutableAclService(dataSource, lookupStrategy(), aclCache());
        mutableAclService.setClassIdentityQuery("SELECT LAST_INSERT_ID()");
        mutableAclService.setSidIdentityQuery("SELECT LAST_INSERT_ID()");
 /*       mutableAclService.setInsertSidSql(insert into acl_sid(id, principal, sid) values (seq_acl_sid.nextval, ?, ?));
        mutableAclService.setInsertClassSql(insert into acl_class(id, class) values (seq_acl_class.nextval, ?));
        mutableAclService.setInsertObjectIdentitySql(insert into acl_object_identity(id, object_id_class, object_id_identity, owner_sid, entries_inheriting) values(seq_acl_object_identity.nextval, ?, ?, ?, ?));
        mutableAclService.setInsertEntrySql(insert into acl_entry(id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) values (seq_acl_entry.nextval, ?, ?, ?, ?, ?, ?, ?));*/
        return mutableAclService;
    }

  /*  public DataSource dataSource() throws PropertyVetoException {
//        final ComboPooledDataSource dataSource = new ComboPooledDataSource(true);
//        cpds.setJdbcUrl(jdbcUrl);
//        cpds.setUser(username);
//        cpds.setPassword(password);
//        cpds.setDriverClass(driverClass);
        final ComboPooledDataSource  dataSource = new ComboPooledDataSource();
        dataSource.setUser("root");
        dataSource.setPassword("123");
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/base?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&allowMultiQueries=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
        dataSource.setDriverClass("com.mysql.jdbc.Driver");
        dataSource.setInitialPoolSize(2);
        dataSource.setAcquireIncrement(5);
        dataSource.setMinPoolSize(2);
        dataSource.setMaxPoolSize(20);
        dataSource.setMaxStatements(50);
        dataSource.setMaxIdleTime(30);
        dataSource.setIdleConnectionTestPeriod(15);
        dataSource.setAcquireRetryAttempts(3);
        return dataSource;
    }*/

    @Bean
    public BasicLookupStrategy lookupStrategy() throws PropertyVetoException {
        return new BasicLookupStrategy(dataSource, aclCache(), aclAuthorizationStrategy(), grantingStrategy());
    }

    @Bean
    public AclCache aclCache() {
        return new EhCacheBasedAclCache(ehCacheManagerFactoryBean().getObject().getCache("aclCache"), grantingStrategy(), aclAuthorizationStrategy());
    }

    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();

        cacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
        cacheManagerFactoryBean.setCacheManagerName("aclCacheManager");
        cacheManagerFactoryBean.setShared(true);

        return cacheManagerFactoryBean;
    }

    @Bean
    public AclAuthorizationStrategy aclAuthorizationStrategy() {
        return new AclAuthorizationStrategyImpl(
                new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Bean
    public PermissionGrantingStrategy grantingStrategy(){
        return new DefaultPermissionGrantingStrategy(auditLogger());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
//        super.configure(web);
        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setPermissionEvaluator(new MyPermissionEvaluator());
        web.expressionHandler(handler);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/all/**").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/db/**").hasAnyRole("ADMIN", "DBA")
                .antMatchers("/**").authenticated()
//                .anyRequest().authenticated()
                .anyRequest().access("@mySecurity.check(authentication,request)")
                .and()//.httpBasic().and()
                .formLogin()
//                .loginPage();
                .defaultSuccessUrl("/login_success")
                .failureUrl("/all/login_failure");


    }

    @Bean
    public AuditLogger auditLogger(){
        return new ConsoleAuditLogger();
    }

}
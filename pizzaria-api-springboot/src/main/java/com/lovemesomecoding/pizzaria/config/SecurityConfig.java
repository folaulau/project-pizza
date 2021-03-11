package com.lovemesomecoding.pizzaria.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.RegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.NullSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.lovemesomecoding.pizzaria.security.AuthenticationFilter;
import com.lovemesomecoding.pizzaria.security.CustomAcccessDeniedHandler;
import com.lovemesomecoding.pizzaria.security.CustomAuthenticationProvider;
import com.lovemesomecoding.pizzaria.security.CustomLoginFilter;
import com.lovemesomecoding.pizzaria.security.CustomLogoutHandler;
import com.lovemesomecoding.pizzaria.security.CustomLogoutSuccessHandler;
import com.lovemesomecoding.pizzaria.utils.PathUtils;

/***
 * 
 * @author folaukaveinga
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    private CustomAcccessDeniedHandler   customAcccessDeniedHandler;

    @Autowired
    private CustomLogoutHandler          customLogoutHandler;

    @Autowired
    private CustomLogoutSuccessHandler   customLogoutSuccessHandler;

    @Bean
    public CustomLoginFilter customLoginFilter() throws Exception {
        return new CustomLoginFilter(PathUtils.LOGIN_URL, authenticationManagerBean());
    }

    @Bean
    public AuthenticationFilter authorizationFilter() {
        return new AuthenticationFilter();
    }

    @Bean
    public RegistrationBean jwtAuthFilterRegister(AuthenticationFilter customAuthenticationFilter) {
        FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<>(customAuthenticationFilter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.cors().and().csrf().disable()
                .authorizeRequests()
                
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                    .permitAll()
                .antMatchers(PathUtils.PING_URLS)
                    .permitAll()
                .antMatchers(PathUtils.PUBLIC_URLS)
                    .permitAll()
                .antMatchers(PathUtils.SWAGGER_DOC_URLS)
                    .permitAll()
                .antMatchers(PathUtils.LOGIN_URL)
                    .permitAll()

                .anyRequest()
                    .permitAll();

        http.logout()
            .logoutRequestMatcher(new AntPathRequestMatcher(PathUtils.LOGOUT_URL))
            .addLogoutHandler(customLogoutHandler)
            .logoutSuccessHandler(customLogoutSuccessHandler);

        http.addFilterBefore(securityContextPersistenceFilter(), UsernamePasswordAuthenticationFilter.class);
        
        // login filter
        http.addFilterBefore(customLoginFilter(), UsernamePasswordAuthenticationFilter.class);

        // request authorization filter
        http.addFilterBefore(authorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // handler access denied calls
        http.exceptionHandling().accessDeniedHandler(customAcccessDeniedHandler);
        
        // @formatter:on
    }

    @Override
    public void configure(WebSecurity web) {
        // @formatter:off
        web.ignoring()
        .antMatchers(PathUtils.PING_URLS)
        .antMatchers(PathUtils.PUBLIC_URLS)
        .antMatchers(PathUtils.LOGIN_URL)
        .antMatchers(PathUtils.SWAGGER_DOC_URLS)
        .antMatchers("/resources/**")
        .antMatchers("/actuator/**");
        // @formatter:on
    }

    @Bean
    public MethodInvokingFactoryBean methodInvokingFactoryBean() {
        MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
        methodInvokingFactoryBean.setTargetClass(SecurityContextHolder.class);
        methodInvokingFactoryBean.setTargetMethod("setStrategyName");
        methodInvokingFactoryBean.setArguments(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
        return methodInvokingFactoryBean;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.authenticationProvider(customAuthenticationProvider);
    }

    @Bean
    public SecurityContextPersistenceFilter securityContextPersistenceFilter() {
        return new SecurityContextPersistenceFilter(new NullSecurityContextRepository());
    }

}

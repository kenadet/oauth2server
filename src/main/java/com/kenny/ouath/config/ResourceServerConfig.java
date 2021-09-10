package com.kenny.ouath.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.session.SessionManagementFilter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private DefaultTokenServices tokenServices;

    private final String RESOURCE_ID = "oauth_client_resource_Id";

    private String[] whiteList = {"/login", "/signup", "/swagger-ui.html"};

    @Autowired
    CorsFilter corsFilter;

    @Override
    public void configure(HttpSecurity http) throws Exception {
            http.addFilterBefore(corsFilter, SessionManagementFilter.class)
                .csrf()
                .disable()
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/login", "/signup", "/swagger-ui/**")
                .permitAll()
                .anyRequest().authenticated()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources
                .resourceId(RESOURCE_ID)
                .tokenServices(tokenServices)
                .tokenStore(tokenStore);
    }
}



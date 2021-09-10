package com.kenny.ouath.config;

import com.kenny.ouath.service.UserDetailServiceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    private TokenStore tokenStore;
    private UserDetailServiceLoader userDetailServiceLoader;
    private DefaultTokenServices tokenServices;
    private AuthenticationManager authenticationManager;
    private DataSource dataSource;
    private PasswordEncoder passwordEncoder;
    private JwtAccessTokenConverter accessTokenConverter;

    private final String RESOURCE_ID= "oauth_client_resource_Id";

    @Autowired
    public AuthServerConfig(UserDetailServiceLoader userDetailServiceLoader,
                            DataSource dataSource, PasswordEncoder passwordEncoder,
                            AuthenticationManager authenticationManager,
                            TokenStore tokenStore, JwtAccessTokenConverter accessTokenConverter,
                            DefaultTokenServices tokenServices){

        this.userDetailServiceLoader =  userDetailServiceLoader;
        this.dataSource = dataSource;
        this.authenticationManager = authenticationManager;
        this.tokenStore = tokenStore;
        this.accessTokenConverter = accessTokenConverter;
        this.tokenServices = tokenServices;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(dataSource);

        if(!jdbcClientDetailsService.listClientDetails().isEmpty() ) {
            jdbcClientDetailsService.removeClientDetails("Oauth2Client");
        }

        if(jdbcClientDetailsService.listClientDetails().isEmpty() ) {
            clients.jdbc(dataSource)
                    .withClient("Oauth2Client")
                    .secret(passwordEncoder.encode("123456"))
                    .authorizedGrantTypes("password", "refresh_token")
                    .scopes("read", "write")
                    .resourceIds(RESOURCE_ID)
                    .and().build();
        }
        clients.jdbc(dataSource); //.loadClientByClientId("Oauth2Client");
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients();
        security.checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(authenticationManager)
                .tokenServices(tokenServices)
                .tokenStore(tokenStore)
                .accessTokenConverter(accessTokenConverter)
                .userDetailsService(userDetailServiceLoader)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
    }
}


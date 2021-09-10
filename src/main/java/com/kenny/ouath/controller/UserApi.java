package com.kenny.ouath.controller;

import com.kenny.ouath.model.JwtToken;
import com.kenny.ouath.model.LoginUser;
import com.kenny.ouath.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

@RestController
public class UserApi {

    @Value("${server.port}")
    private String port;

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;

    @Autowired
    public UserApi(UserService userService,
                   PasswordEncoder passwordEncoder,
                   RestTemplate restTemplate
    ){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.restTemplate = restTemplate;

    }

    @PostMapping(path = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> signUp(@RequestBody @Valid LoginUser loginUser) {

        if(!userService.getUserByEmail(loginUser.getEmail()).isPresent()){

        try {
            LoginUser signUser = LoginUser.builder()
                    .uuid(UUID.randomUUID())
                    .username(loginUser.getUsername())
                    .email(loginUser.getEmail().toLowerCase())
                    .password(passwordEncoder.encode(loginUser.getPassword().replaceAll("&", "*1")))
                    .roles(new HashSet<>(Arrays.asList("USER")))
                    .build();

            userService.save(signUser);

            return ResponseEntity.ok("Account created successfully");

        } catch (Exception ex) {

            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Account creation failed");
        }
        }else{
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Account already exist");
        }

    }

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody LoginUser loginUser) throws AccountNotFoundException, HttpRequestMethodNotSupportedException, UnsupportedEncodingException {

        if (loginUser != null && loginUser.getPassword() != null && loginUser.getEmail() != null) {

            try {
                userService.getUserByEmail(loginUser.getEmail()).orElseThrow(AccountNotFoundException::new);
            }catch (AccountNotFoundException  ex){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Login failed");
            }

            return getAuthToken(loginUser, "User");

        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Login failed");
        }
    }

    private ResponseEntity<JwtToken>  getAuthToken(LoginUser loginUser, String role) throws UnsupportedEncodingException {

        String clientId = "Oauth2Client";
        String clientSecret = "123456";
        String grantType = "password";
        String username = loginUser.getEmail();
        String password = loginUser.getPassword().replaceAll("&", "*1");

        HttpHeaders headers = setHttpHeaders(clientId, clientSecret);

        String oauthTokenUrl = String.format("http://%s:%s/oauth/token?" + "username=%s&password=%s&grant_type=%s", "localhost", port,
                username, password, grantType);


        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<JwtToken> response = null;

        try {

            response = restTemplate.postForEntity(oauthTokenUrl, entity, JwtToken.class);

            JwtToken jwtToken = response.getBody();

            jwtToken.setStatus("Successful");


            return new ResponseEntity<JwtToken>(jwtToken, response.getStatusCode());

        } catch (HttpClientErrorException ex) {

            JwtToken jwtToken = new JwtToken();
            jwtToken.setStatus(ex.getResponseBodyAsString());

            return new ResponseEntity<JwtToken>(jwtToken, HttpStatus.EXPECTATION_FAILED);
        }
    }

    private HttpHeaders setHttpHeaders(String clientId, String clientSecret) {

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        headers.setBasicAuth(clientId, clientSecret);

        return headers;
    }
}

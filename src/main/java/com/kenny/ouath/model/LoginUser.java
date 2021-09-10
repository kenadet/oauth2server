package com.kenny.ouath.model;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name="login_users")
public class LoginUser {

    @Id
    private UUID uuid;
    private String username;
    private String password;
    private String email;

    @ElementCollection(fetch= EAGER)
    @JoinTable(name = "user_roles", joinColumns = {@JoinColumn(name = "uuid")})
    @Column(name = "role")
    private Set<String> roles;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    private LoginUser(){};

    public static LoginUserBuilder builder(){
        return new LoginUserBuilder();
    }

    public static class LoginUserBuilder {

        private UUID uuid;
        private String username;
        private String password;
        private String email;
        private Set<String> roles;

        public LoginUserBuilder uuid(UUID uuid){

            this.uuid = uuid;
            return this;
        }

        public LoginUserBuilder username(String username){

            this.username= username;
            return this;
        }

        public LoginUserBuilder password(String password){

            this.password= password;
            return this;
        }

        public LoginUserBuilder email(String email){

            this.email= email;
            return this;
        }

        public LoginUserBuilder roles(Set<String> roles){

            this.roles = roles;
            return this;
        }

        public LoginUser build(){
            LoginUser loginUser = new LoginUser();

            loginUser.setUuid(this.uuid);
            loginUser.setUsername(this.username);
            loginUser.setPassword(this.password);
            loginUser.setEmail(this.email);
            loginUser.setRoles(this.roles);

            return loginUser;
        }
    }

}
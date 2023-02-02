package ma.cam.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ma.cam.dto.UserResult;

public class AuthToken {

    private String token;
    @JsonIgnore
    private UserResult user;

    public AuthToken(){

    }

    public AuthToken(String token, UserResult user){
        this.token = token;
        this.user = user;
    }

    public AuthToken(String token){
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserResult getUser() {
        return this.user;
    }

    public void setUser(UserResult user) {
        this.user = user;
    }
}

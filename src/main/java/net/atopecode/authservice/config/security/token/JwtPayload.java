package net.atopecode.authservice.config.security.token;

import java.util.ArrayList;
import java.util.List;

public class JwtPayload {

    private String sub; //Subject.
    private Long iat; //IssueAt Date (token date creation).
    private Long exp; //Expiration (token date expiration).
    private String userName;
    private List<String> authorities;

    public JwtPayload() {
        this.authorities = new ArrayList<>();
    }

    public JwtPayload(String sub, Long iat, Long exp, String userName, List<String> authorities) {
        this.sub = sub;
        this.iat = iat;
        this.exp = exp;
        this.userName = userName;
        this.authorities = (authorities != null) ? authorities : new ArrayList<>();
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public Long getIat() {
        return iat;
    }

    public void setIat(Long iat) {
        this.iat = iat;
    }

    public Long getExp() {
        return exp;
    }

    public void setExp(Long exp) {
        this.exp = exp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return "JwtPayload{" +
                "sub='" + sub + '\'' +
                ", iat=" + iat +
                ", exp=" + exp +
                ", userName='" + userName + '\'' +
                ", authorities=" + authorities +
                '}';
    }
}
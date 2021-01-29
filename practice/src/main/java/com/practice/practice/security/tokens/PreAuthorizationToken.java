package com.practice.practice.security.tokens;

import com.practice.practice.domain.dto.LoginFormDTO;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * 인증전
 */
public class PreAuthorizationToken extends UsernamePasswordAuthenticationToken {

    private PreAuthorizationToken(String username, String password) {
        super(username, password);
    }

    /**
     * DTO값 세팅 생성자
     */
    public PreAuthorizationToken(LoginFormDTO loginFormDTO) {
        this(loginFormDTO.getUsername(), loginFormDTO.getPassword());
    }

    //username, password 불러오는(getPrincipal) -> 편의 메서드
    public String getUsername() {
        return (String)super.getPrincipal();
    }

    public String getPassword() {
        return (String)super.getCredentials();
    }
}

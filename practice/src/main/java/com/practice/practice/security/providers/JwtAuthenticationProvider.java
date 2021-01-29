package com.practice.practice.security.providers;

import com.practice.practice.security.MemberContext;
import com.practice.practice.security.jwt.JwtDecoder;
import com.practice.practice.security.tokens.JwtPreProcessingToken;
import com.practice.practice.security.tokens.PostAuthorizationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtDecoder jwtDecoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getPrincipal();
        MemberContext context = jwtDecoder.decodeJwt(token);
        return PostAuthorizationToken.setPostAuthorizationTokenFromMemberContext(context);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtPreProcessingToken.class.isAssignableFrom(authentication); //Jwt 인증전 토큰을 서포트 해야됨
    }
}

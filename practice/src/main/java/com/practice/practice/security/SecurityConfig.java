package com.practice.practice.security;

import com.practice.practice.security.filters.FormLoginFilter;
import com.practice.practice.security.filters.JwtAuthenticationFilter;
import com.practice.practice.security.handlers.FormLoginAuthenticationFailureHandler;
import com.practice.practice.security.handlers.FormLoginAuthenticationSuccessHandler;
import com.practice.practice.security.handlers.JwtAuthenticationFailureHandler;
import com.practice.practice.security.providers.FormLoginAuthenticationProvider;
import com.practice.practice.security.providers.JwtAuthenticationProvider;
import com.practice.practice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //시큐리티 메서드@PreAuthorize등을 사용할수있음
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler;
    private final HeaderTokenExtractor headerTokenExtractor;

    private final FormLoginAuthenticationSuccessHandler formLoginAuthenticationSuccessHandler;
    private final FormLoginAuthenticationFailureHandler formLoginAuthenticationFailureHandler;
    private final FormLoginAuthenticationProvider formLoginAuthenticationProvider;

    protected FormLoginFilter formLoginFilter() throws Exception {
        FormLoginFilter formLoginFilter = new FormLoginFilter("/api/members/login", formLoginAuthenticationSuccessHandler, formLoginAuthenticationFailureHandler);
        formLoginFilter.setAuthenticationManager(super.authenticationManagerBean());
        return formLoginFilter;
    }

    protected JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        FilterSkipMatcher matcher = new FilterSkipMatcher(Arrays.asList("/api/members/login"), "/api/**");//허용 url
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(matcher, jwtAuthenticationFailureHandler, headerTokenExtractor);
        jwtAuthenticationFilter.setAuthenticationManager(super.authenticationManagerBean());
        return jwtAuthenticationFilter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(formLoginAuthenticationProvider)
                .authenticationProvider(jwtAuthenticationProvider);
    }

    //시큐리티 설정
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()    // security에서 기본으로 생성하는 login페이지 사용 안 함
                .csrf().disable()    // csrf 사용 안 함 == REST API 사용하기 때문에
                .headers().frameOptions().disable()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)// JWT인증사용하므로 세션 사용안함
                .and()
                .addFilterBefore(formLoginFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }



}

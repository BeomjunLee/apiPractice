package com.practice.practice.configs;

import com.practice.practice.domain.Member;
import com.practice.practice.domain.MemberRole;
import com.practice.practice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class AppConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        //여러가지 암호화 방법들을 알아서 매칭
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
    
    //어플리케이션 시작시 test 계정 생성
    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {
            @Autowired
            MemberService memberService;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                Member member = Member.builder()
                        .username("admin")
                        .password("1234")
                        .role(MemberRole.ADMIN)
                        .build();
                Member member2 = Member.builder()
                        .username("leebeomjun")
                        .password("1234")
                        .role(MemberRole.USER)
                        .build();
                memberService.save(member);
                memberService.save(member2);
            }
        };
    }
}

package com.practice.practice.configs;

import com.practice.practice.domain.Member;
import com.practice.practice.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("test")
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

//    @Autowired
//    MemberRepository memberRepository;

    @Test
    public void findByUsername() throws Exception{
        //given
        String username = "leebeomjun";
        String password = "1234";
        Member member = Member.builder()
                .username(username)
                .password(password)
                .postcode("test")
                .street("test")
                .detail("test")
                .build();
        memberService.save(member);

        //when


        //then
    }

    @Test
    public void findByUsername_Fail() throws Exception{
        //given
        //when
        String username = "dqwdwdqdwqdwqd";

        //then
        Assertions.assertThrows(UsernameNotFoundException.class,
                () ->{
                });
    }
}
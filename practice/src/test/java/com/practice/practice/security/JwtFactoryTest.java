package com.practice.practice.security;

import com.practice.practice.domain.Member;
import com.practice.practice.domain.MemberRole;
import com.practice.practice.security.jwt.JwtFactory;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JwtFactoryTest {

    private static final Logger log = LoggerFactory.getLogger(JwtFactory.class);

    private MemberContext context;

    @Autowired
    private JwtFactory jwtFactory;

    public void setUp() {
        Member member = Member.builder()
                .username("lbj")
                .password("1234")
                .role(MemberRole.USER)
                .build();
        log.error("userid : {}, password : {}, role : {}", member.getUsername(), member.getPassword(), member.getRole());
        this.context = MemberContext.setMemberContextFromMember(member);
    }
    
    @Test
    public void JwtTokenTest() throws Exception{
        //given
        setUp();
        //log.error(jwtFactory.generateToken(context));
        //when
        
        //then
    }
}
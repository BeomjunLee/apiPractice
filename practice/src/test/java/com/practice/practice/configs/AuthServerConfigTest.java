package com.practice.practice.configs;


import com.practice.practice.domain.Member;
import com.practice.practice.domain.MemberRole;
import com.practice.practice.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ActiveProfiles("test")
class AuthServerConfigTest{

    @Autowired
    MemberService memberService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void getAuthToken() throws Exception{
        //given
        String username = "lbj";
        String password = "1234";
        Member member = Member.builder()
                .username(username)
                .password(password)
                .role(MemberRole.USER)
                .build();
        memberService.save(member);

        String clientId = "myApp";
        String clientSecret = "pass";

        this.mockMvc.perform(post("/oauth/token")
                    .with(httpBasic(clientId, clientSecret)) //클라이언트 id, 시크릿으로 basicAuth라는 헤더를 만듬
                    .param("username", username)
                    .param("password", password)
                    .param("grant_type", "password"))   //password라는 인증타입 사용하겠다
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists());
        //when

        //then
    }

}
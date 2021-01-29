package com.practice.practice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.practice.TestRestDocsConfig;
import com.practice.practice.domain.Member;
import com.practice.practice.domain.dto.MemberDTO;
import com.practice.practice.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(TestRestDocsConfig.class) //test restDocs config를 import
@ActiveProfiles("test")
class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ModelMapper modelMapper;

//    @Test
//    public void createMember() throws Exception {
//        Member createMember = new Member("leebeomjun", "1234","200", "동탄대로", "202");
//        MemberDTO member = new MemberDTO(createMember);
//
//        mockMvc.perform(post("/members")
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaTypes.HAL_JSON)
//                .content(objectMapper.writeValueAsString(member)))
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andDo(document("create-member",
//                        links(  //링크 문서화
//                                linkWithRel("self").description("자기자신 uri"),
//                                linkWithRel("member-info").description("가입된 회원 정보 보기 uri"),
//                                linkWithRel("update-member").description("가입된 회원 정보 수정 uri")
//                        ),
//                        requestHeaders( //요청 헤더 문서화
//                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
//                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
//                        ),
//                        requestFields(  //요청 필드 문서화
//                                fieldWithPath("id").description("회원 구분 id값"),
//                                fieldWithPath("name").description("회원 이름"),
//                                fieldWithPath("address").description("주소 객체"),
//                                fieldWithPath("address.postcode").description("우편번호"),
//                                fieldWithPath("address.street").description("도로명 주소"),
//                                fieldWithPath("address.detail").description("상세주소")
//                        ),
//                        responseHeaders( //응답 헤더 문서화
//                                headerWithName(HttpHeaders.LOCATION).description("location header"),
//                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
//                        ),
//                        responseFields( //응답 필드 문서화, relaxed를 앞에붙여서 모든 필드의 값을 안적어줘도 된다
//                                fieldWithPath("data").description("가입된 회원 구분 id값"),
//                                fieldWithPath("_links.self.href").description("자기 자신 uri"),
//                                fieldWithPath("_links.member-info.href").description("가입된 회원 정보 uri"),
//                                fieldWithPath("_links.update-member.href").description("가입된 회원 수정 uri")
//
//                        )
//                ));
//
//    }
//
    @Test
    public void selectMembers() throws Exception{
        //given
        IntStream.rangeClosed(1, 30).forEach(this::generateMember); //1부터 30까지 generateMember실행

        //when
        this.mockMvc.perform(get("/members")
                    .param("page", "1") //0이 첫번째 페이지, 1이 두번째 페이지
                    .param("size", "10")
                )
        //then
                .andDo(print()) //api 정보 보이게
                .andExpect(status().isOk());

    }
    
    //Member 객체 생성
    private void generateMember(int i) {
        Member member = Member.builder()
                .username("member" + i)
                .password("1234")
                .postcode("100" + i)
                .street("도로명 주소" + i)
                .detail("세부주소" + i)
                .build();
        this.memberRepository.save(member);
    }

    //수정 성공 테스트
    @Test
    public void updateMember() throws Exception{
        //given
        MemberDTO memberDTO = new MemberDTO(1L, "updated Member", "1234", "updated", "updated", "updated");
        //when
        //then
        this.mockMvc.perform(put("/members/{id}", memberDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(this.objectMapper.writeValueAsString(memberDTO))
        )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("name").value("updated Member"));  //name필드가 변경되어야하고
    }

    //수정 실패 테스트(입력값이 잘못됨)
    @Test
    public void updateMember400_wrong() throws Exception{
        //given
        Member member = memberRepository.findById(1L).orElse(null);
        MemberDTO memberDTO = modelMapper.map(member, MemberDTO.class);
        String updateName = "updated Member";
        memberDTO.setUsername("111111111111111111111111");
        //when
        //then
        this.mockMvc.perform(put("/members/{id}", member.getId())
                .content(MediaType.APPLICATION_JSON_VALUE)
                .content(this.objectMapper.writeValueAsString(memberDTO))
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    //수정 실패 테스트(입력값이 비어있는 경우)
    @Test
    public void updateMember400_empty() throws Exception{
        //given
        Member member = memberRepository.findById(1L).orElse(null);
        MemberDTO memberDTO = modelMapper.map(member, MemberDTO.class);
        String updateName = "updated Member";
        //when
        //then
        this.mockMvc.perform(put("/members/{id}", member.getId())
                .content(MediaType.APPLICATION_JSON_VALUE)
                .content(this.objectMapper.writeValueAsString(memberDTO))
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    //수정 실패 테스트(존재하지 않는 회원 수정 실패)
    @Test
    public void updateMember404() throws Exception{
        //given
        Member member = memberRepository.findById(1L).orElse(null);
        MemberDTO memberDTO = modelMapper.map(member, MemberDTO.class);
        String updateName = "updated Member";
        //when
        //then
        this.mockMvc.perform(put("/members/31232132")
                .content(MediaType.APPLICATION_JSON_VALUE)
                .content(this.objectMapper.writeValueAsString(memberDTO))
        )
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
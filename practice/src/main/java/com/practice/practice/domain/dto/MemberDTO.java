package com.practice.practice.domain.dto;

import com.practice.practice.domain.Address;
import com.practice.practice.domain.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)  //기본 생성자 protected
public class MemberDTO {
    private Long id;
    @NotBlank(message = "이름에 빈칸이 올수 없습니다")
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String postcode;
    @NotBlank
    private String street;
    @NotBlank
    private String detail;

    public MemberDTO(Member member){
        id = member.getId();
        username = member.getUsername();
        password = member.getPassword();
        postcode = member.getPostcode();
        street = member.getStreet();
        detail = member.getDetail();
    }
}

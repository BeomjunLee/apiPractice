package com.practice.practice.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)  //기본 생성자 protected
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;
    private String password;

    private String postcode;
    private String street;
    private String detail;

    @Enumerated(value = EnumType.STRING)
    private MemberRole role;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    public void changeMember(String username, String password, String postcode, String street, String detail) {
        this.username = username;
        this.password = password;
        this.postcode = postcode;
        this.street = street;
        this.detail = detail;
    }
    
    //시큐리티 비밀번호 암호화
    public void encodingPassword(String password) {
        this.password = password;
    }

}

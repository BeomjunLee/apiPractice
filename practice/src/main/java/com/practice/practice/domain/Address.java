package com.practice.practice.domain;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)  //기본 생성자 protected
public class Address {

    private String postcode;
    private String street;
    private String detail;

    public Address(String postcode, String street, String detail) {
        this.postcode = postcode;
        this.street = street;
        this.detail = detail;
    }
}

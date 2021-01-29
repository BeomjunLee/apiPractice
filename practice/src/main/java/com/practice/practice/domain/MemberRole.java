package com.practice.practice.domain;

import lombok.Getter;

import javax.persistence.Embeddable;
import java.util.Arrays;
import java.util.NoSuchElementException;

@Getter
public enum MemberRole {
    USER("ROLE_USER"), ADMIN("ROLE_ADMIN");

    private String roleName;

    MemberRole(String roleName) {
        this.roleName = roleName;
    }

    //enum객체 String비교로직
    public boolean isCorrectName(String name) {
        return name.equalsIgnoreCase(this.roleName);
    }
    
    //name을 MemberRole에서 찾아서 리턴
    public static MemberRole getRoleByName(String name) {
        return Arrays.stream(MemberRole.values()).filter(r -> r.isCorrectName(name)).findFirst()
                .orElseThrow(() -> new NoSuchElementException("검색된 권한이 없습니다"));
    }
}

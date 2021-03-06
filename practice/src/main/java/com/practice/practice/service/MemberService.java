package com.practice.practice.service;

import com.practice.practice.domain.Member;
import com.practice.practice.domain.MemberRole;
import com.practice.practice.repository.MemberRepository;
import com.practice.practice.security.MemberContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true) //조회최적화
@RequiredArgsConstructor    //스프링 주입
public class MemberService{

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Member member = memberRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException(username + "에 해당되는 유저를 찾을수 없습니다"));
//
//        return getMemberContext(member);//MemberContext = User 에다가 아이디, 비밀번호, ROLE을 넣어줌
//    }
//
//    private MemberContext getMemberContext(Member member) {
//        return MemberContext.setMemberContextFromMember(member);
//    }

    //로그인체크
    public Member loginCheck(String username, String password) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + "에 해당되는 유저를 찾을수 없습니다"));
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치 하지않습니다");
        } else return member;
    }

    //회원가입
    @Transactional //조회가 아니므로 Transactional
    public Member save(Member member) {
        //비밀번호 encoding
        member.encodingPassword(passwordEncoder.encode(member.getPassword()));
        return memberRepository.save(member);
    }

    //중복 회원 검증

    public void validateDuplicateMember(String name, Errors errors) {
        Long findMembers = memberRepository.countByUsername(name);
        if (findMembers > 0) {
            errors.rejectValue("name", "아이디 중복", "아이디가 중복되었습니다");
        }
        //두 유저가 동시에 가입할 경우를 대비해서 DB 에도 유니크 제약조건을 걸어줘야함
    }

    @Transactional
    public Member update(Member member) {
        return memberRepository.save(member);
    }
    //회원정보

    public Optional<Member> findOne(Long id) {
        return memberRepository.findById(id);  //값이 없을때 null
    }
    //회원 전체 조회

    public Page<Member> findAll(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }
}

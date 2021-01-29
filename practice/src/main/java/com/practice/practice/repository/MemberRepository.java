package com.practice.practice.repository;

import com.practice.practice.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {


    Long countByUsername(String username);
    
    //스프링데이터 jpa 페이징
    Page<Member> findAll(Pageable pageable);

    Optional<Member> findByUsername(String username);

}

package com.practice.practice.controller;
import com.practice.practice.domain.Member;
import com.practice.practice.domain.MemberRole;
import com.practice.practice.domain.dto.LoginDTO;
import com.practice.practice.domain.dto.LoginFormDTO;
import com.practice.practice.domain.dto.MemberDTO;
import com.practice.practice.domain.dto.Response;
import com.practice.practice.resource.MemberResource;
import com.practice.practice.resource.ResponseResource;
import com.practice.practice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/api/members", produces = MediaTypes.HAL_JSON_VALUE)
public class MemberController {

    private final MemberService memberService;

    private final ModelMapper modelMapper;
    
//    //로그인
//    @PostMapping("/login")
//    public ResponseEntity login(LoginDTO loginDTO, Errors errors){
//
//        return ResponseEntity.ok().body(loginDTO);
//    }


    //전체 회원 검색
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity findMembers(Pageable pageable, PagedResourcesAssembler<MemberDTO> assembler){
            Page<Member> findMembers = memberService.findAll(pageable);
            //Page<Member>를 Page<MemberDTO>로
            Page<MemberDTO> members = findMembers.map(member -> new MemberDTO(member));

            var memberResource = assembler.toModel(members, e -> new MemberResource(e));
            return ResponseEntity.ok(memberResource);
    }
    
    //특정 회원검색
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity findMember(@PathVariable("id") Long id) {
        Optional<Member> optionalMember = memberService.findOne(id);

        //아무것도 못찾았을 경우에는 not found(404)에러
        if (optionalMember.isEmpty()) {
            Response response = Response.builder()
                    .status("fail")
                    .message("일치하는 회원이 없습니다")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        //Entity를 DTO로 변환
        Member findMember = optionalMember.get();
        MemberDTO member = new MemberDTO(findMember.getId(), findMember.getUsername(),
                findMember.getPassword(), findMember.getPostcode(), findMember.getStreet(), findMember.getDetail());

        MemberResource memberResource = new MemberResource(member);
        memberResource.add(linkTo(MemberController.class).withRel("all-users"));

        return ResponseEntity.ok(memberResource);
    }

    //회원가입
    @PostMapping
    public ResponseEntity saveMember(@Valid MemberDTO memberDTO, Errors errors) {
        Member member = Member.builder()
                .username(memberDTO.getUsername())
                .password(memberDTO.getPassword())
                .postcode(memberDTO.getPostcode())
                .street(memberDTO.getStreet())
                .detail(memberDTO.getDetail())
                .role(MemberRole.USER)
                .build();
        
        memberService.validateDuplicateMember(member.getUsername(), errors);    //중복 회원 검증

        if (errors.hasErrors()) { //중복회원이나 Valid오류가 있다면 badRequest
            return ResponseEntity.badRequest().body(errors);
        }

        Member savedMember = memberService.save(member);

        URI createUri = linkTo(MemberController.class).toUri();
        //memberDTO = modelMapper.map(member, MemberDTO.class);

        Response response = Response.builder()
                .status("success")
                .message("회원가입 성공")
                .build();

        ResponseResource resource = new ResponseResource(response);
        resource.add(linkTo(MemberController.class).slash(savedMember.getId()).withRel("member-info"));
        resource.add(linkTo(MemberController.class).slash(savedMember.getId()).withRel("update-member"));
        return ResponseEntity.created(createUri).body(resource);
    }

    //회원 수정
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity updateMember(@PathVariable Long id, @Valid MemberDTO memberDTO, Errors errors) {
        Optional<Member> optionalMember = memberService.findOne(id);

        //없는 회원
        if (optionalMember.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        //Valid에 의해 잘못된값 기입시
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }
        Member member = optionalMember.get();

        member.changeMember(memberDTO.getUsername(), memberDTO.getPassword(), memberDTO.getPostcode(), memberDTO.getStreet(), memberDTO.getDetail());

        Member savedMember = memberService.update(member);//저장하면 더티체킹돼서 수정됨
        MemberDTO savedMemberDTO = modelMapper.map(savedMember, MemberDTO.class);

        MemberResource memberResource = new MemberResource(savedMemberDTO);

        return ResponseEntity.ok(memberResource);
    }

}

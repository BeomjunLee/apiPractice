package com.practice.practice.resource;

import com.practice.practice.controller.MemberController;
import com.practice.practice.domain.dto.MemberDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class MemberResource extends EntityModel<MemberDTO> {

    public MemberResource(MemberDTO memberDTO, Link... links){
        super(memberDTO, links);
        add(linkTo(MemberController.class).slash(memberDTO.getId()).withSelfRel());
    }
}

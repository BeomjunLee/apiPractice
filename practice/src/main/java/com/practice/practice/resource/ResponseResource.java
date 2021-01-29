package com.practice.practice.resource;

import com.practice.practice.controller.MemberController;
import com.practice.practice.domain.dto.Response;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class ResponseResource extends EntityModel<Response> {
    public ResponseResource(Response response, Link... links){
        super(response, links);
        add(linkTo(MemberController.class).withSelfRel());
    }
}

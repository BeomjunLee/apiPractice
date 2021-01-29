package com.practice.practice.resource;

import org.springframework.hateoas.EntityModel;

public class Resource<T> extends EntityModel {
    private T data;

    public Resource(T data) {
        this.data = data;
    }

    public T getData(){
        return data;
    }
}

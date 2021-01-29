package com.practice.practice.domain.item;

import lombok.Getter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@DiscriminatorValue("book") //book으로 dtype값에 저장
public class Book extends Item {

    private String author;
    private String isbn;
}

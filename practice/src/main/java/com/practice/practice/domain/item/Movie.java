package com.practice.practice.domain.item;

import lombok.Getter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@DiscriminatorValue("movie") //dtype에 movie로 저장
public class Movie extends Item {
    private String director;
    private String actor;
}

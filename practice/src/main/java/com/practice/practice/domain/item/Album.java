package com.practice.practice.domain.item;

import lombok.Getter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Entity
@DiscriminatorValue("album") //dtype에 album으로 저장
public class Album extends Item {
    private String artist;
    private String etc;
}

package com.practice.practice.domain;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "delivery") //지연로딩설정
    private Order order;

    @Enumerated(EnumType.STRING) //Enum타입 String으로 (오리지날은 0, 1, ...)
    private DeliveryStatus deliveryStatus;

    @Embedded
    private Address address;

    public void setOrder(Order order) {
        this.order = order;
    }
}

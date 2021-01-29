package com.practice.practice.domain;

import com.practice.practice.domain.item.Item;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "orderItem_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  //OneToMany, ManyToMany 빼고 다 지연로딩해야됨
    @JoinColumn(name = "order_id")  //외래키 설정
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int orderPrice;

    private int orderCount;

    public void setOrder(Order order) {
        this.order = order;
    }
}

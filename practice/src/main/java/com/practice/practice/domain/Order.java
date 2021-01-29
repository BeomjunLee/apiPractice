package com.practice.practice.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "orders") //order은 생성불가한 테이블 이름이여서 테이블이름 orders로 바꿈
public class Order extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //OneToMany, ManyToMany 빼고 다 지연로딩해야됨
    @JoinColumn(name = "member_id") //외래키 설정
    private Member member;  //주문 회원

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) //지연로딩, cascade.ALL설정(주문 취소하면 맵핑된 Delivery도 삭제)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @Enumerated(EnumType.STRING) //Enum 저장타입 String으로 설정
    private OrderStatus orderStatus;

    //==연관 관계 메서드==(Order주문 생성후 회원정보, 주문상품정보, 배송정보를 입력하려고) (여기서 set했으면 반대 Entity에도 set)
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }
}

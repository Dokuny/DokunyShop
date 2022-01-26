package dokuny.shop.entity;

import dokuny.shop.entity.constant.OrderStatus;
import dokuny.shop.utils.BaseEntity;
import dokuny.shop.utils.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Table(name = "orders")
@Entity
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderDate; // 주문 날짜

    @Builder
    public Order(Long id, Member member, List<OrderItem> orderItems, OrderStatus orderStatus, LocalDateTime orderDate) {
        this.id = id;
        this.member = member;
        this.orderItems = orderItems;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);

        orderItem.putOrder(this);

    }

    public static Order createOrder(Member member,List<OrderItem> orderItemList) {
        Order order = Order.builder()
                .member(member)
                .orderItems(new ArrayList<>())
                .orderStatus(OrderStatus.ORDER)
                .orderDate(LocalDateTime.now())
                .build();
        for (OrderItem orderItem : orderItemList) {
            order.addOrderItem(orderItem);

        }

        return order;
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCEL;

        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }

    }




}

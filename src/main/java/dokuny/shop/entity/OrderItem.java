package dokuny.shop.entity;

import dokuny.shop.utils.BaseEntity;
import dokuny.shop.utils.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    private int orderPrice;

    private int count;

    public void putOrder(Order order) {
        this.order = order;
    }

    @Builder
    public OrderItem(Long id, Order order, Item item, int orderPrice, int count) {
        this.id = id;
        this.order = order;
        this.item = item;
        this.orderPrice = orderPrice;
        this.count = count;
    }

    public static OrderItem createOrderItem(Item item, int count) {
        OrderItem orderItem = OrderItem.builder()
                .item(item)
                .count(count)
                .orderPrice(item.getPrice())
                .build();

        item.removeStock(count);
        return orderItem;
    }

    public int getTotalPrice() {
        return orderPrice*count;
    }

    public void cancel() {
        this.getItem().addStock(count);
    }
}

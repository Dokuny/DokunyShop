package dokuny.shop.dto;

import dokuny.shop.entity.OrderItem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderItemDto {

    private String itemNm;

    private int count;

    private int orderPrice;

    private String imgUrl;

    public OrderItemDto(OrderItem orderItem, String imgUrl) {
        this.itemNm = orderItem.getItem().getName();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
    }
}

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
public class CartItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    @Column
    private int count;

    @Builder
    public CartItem(Long id, Cart cart, Item item, int count) {
        this.id = id;
        this.cart = cart;
        this.item = item;
        this.count = count;
    }

    public static CartItem createCartItem(Cart cart,Item item,int count) {
        CartItem cartItem = CartItem.builder()
                .cart(cart)
                .item(item)
                .count(count)
                .build();

        return cartItem;
    }

    public void addCount(int count) {
        this.count+= count;
    }

    public void updateCount(int count) {
        this.count = count;
    }
}

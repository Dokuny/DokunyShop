package dokuny.shop.entity;

import dokuny.shop.utils.BaseEntity;
import dokuny.shop.utils.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="cart_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<CartItem> cartItemList = new ArrayList<>();

    @Builder
    public Cart(Long id, Member member, List<CartItem> cartItemList) {
        this.id = id;
        this.member = member;
        this.cartItemList = cartItemList;
    }

    public static Cart createCart(Member member) {
        Cart cart = Cart.builder()
                .member(member)
                .build();

        return cart;
    }


}

package dokuny.shop.entity;

import dokuny.shop.dto.ItemFormDto;
import dokuny.shop.entity.constant.Category;
import dokuny.shop.entity.constant.ItemStatus;
import dokuny.shop.exception.OutOfStockException;
import dokuny.shop.utils.BaseEntity;
import dokuny.shop.utils.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="item_id")
    private Long id;

    @Column(nullable = false,length = 50)
    private String name;

    @Column(nullable = false)
    private int price;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private int stockQuantity;

    @Lob
    @Column(nullable = false)
    private String itemDetail;

    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus;

    @Builder
    public Item(Long id, String name, int price,Category category ,int stockQuantity, String itemDetail, ItemStatus itemStatus) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.stockQuantity = stockQuantity;
        this.itemDetail = itemDetail;
        this.itemStatus = itemStatus;
    }

    public void updateItem(ItemFormDto itemFormDto) {
        this.name = itemFormDto.getName();
        this.price = itemFormDto.getPrice();
        this.category = itemFormDto.getCategory();
        this.stockQuantity = itemFormDto.getStockQuantity();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemStatus = itemFormDto.getItemStatus();
    }

    public void removeStock(int stockQuantity) {
        int restStock = this.stockQuantity - stockQuantity;
        if (restStock < 0) {
            throw new OutOfStockException("상품의 재고가 부족 합니다. 현재 재고 수량 : " + this.stockQuantity);
        }
        this.stockQuantity = restStock;
    }

    public void addStock(int stockQuantity) {
        this.stockQuantity += stockQuantity;
    }
}

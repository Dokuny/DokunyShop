package dokuny.shop.dto;

import dokuny.shop.entity.Item;
import dokuny.shop.entity.constant.Category;
import dokuny.shop.entity.constant.ItemStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ItemFormDto {

    private Long id;

    @NotBlank(message = "상품명을 입력해주세요.")
    private String name;

    @NotNull(message = "가격을 입력해주세요.")
    private Integer price;

    @NotBlank(message = "상세 설명을 입력해주세요.")
    private String itemDetail;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stockQuantity;

    private Category category;

    private ItemStatus itemStatus;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    private List<Long> itemImgIds = new ArrayList<>();

    @Builder
    public ItemFormDto(Long id, String name, Integer price, Category category, String itemDetail, Integer stockQuantity, ItemStatus itemStatus) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.itemDetail = itemDetail;
        this.stockQuantity = stockQuantity;
        this.itemStatus = itemStatus;
    }

    public Item toEntity() {
        return Item.builder()
                .id(id)
                .name(name)
                .price(price)
                .category(category)
                .itemDetail(itemDetail)
                .stockQuantity(stockQuantity)
                .itemStatus(itemStatus)
                .build();
    }

    public static ItemFormDto of(Item item) {
        return ItemFormDto.builder()
                .id(item.getId())
                .name(item.getName())
                .price(item.getPrice())
                .category(item.getCategory())
                .itemDetail(item.getItemDetail())
                .stockQuantity(item.getStockQuantity())
                .itemStatus(item.getItemStatus())
                .build();
    }
}

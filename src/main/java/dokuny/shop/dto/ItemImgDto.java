package dokuny.shop.dto;

import dokuny.shop.entity.ItemImg;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ItemImgDto {

    private Long id;

    private String imgName;

    private String origImgName;

    private String imgUrl;

    private String repImgYn;

    @Builder
    public ItemImgDto(Long id, String imgName, String origImgName, String imgUrl, String repImgYn) {
        this.id = id;
        this.imgName = imgName;
        this.origImgName = origImgName;
        this.imgUrl = imgUrl;
        this.repImgYn = repImgYn;
    }

    public static ItemImgDto of(ItemImg itemImg) {
        return ItemImgDto.builder()
                .id(itemImg.getId())
                .imgName(itemImg.getImgName())
                .origImgName(itemImg.getOriImgName())
                .imgUrl(itemImg.getImgUrl())
                .repImgYn(itemImg.getRepimgYn())
                .build();
    }
}

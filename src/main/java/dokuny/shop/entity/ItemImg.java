package dokuny.shop.entity;

import dokuny.shop.utils.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@NoArgsConstructor
@Getter
@Table(name="item_img")
@Entity
public class ItemImg extends BaseEntity {

    @Id
    @Column(name = "item_img_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String imgName; // 이미지 파일명

    private String oriImgName; // 원본 파일명

    private String imgUrl;  // 이미지 조회 경로

    private String repimgYn; // 대표 이미지 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    public void updateItemImg(String imgName, String oriImgName, String imgUrl) {
        this.imgName = imgName;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl; 
    }

    public void selectRepImg(String yn) {
        this.repimgYn = yn;
    }
    @Builder
    public ItemImg(Long id, String imgName, String oriImgName, String imgUrl, String repimgYn, Item item) {
        this.id = id;
        this.imgName = imgName;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
        this.repimgYn = repimgYn;
        this.item = item;
    }
}


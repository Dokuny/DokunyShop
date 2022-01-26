package dokuny.shop.dto;

import dokuny.shop.entity.constant.ItemStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemSearchDto {

    private String searchDateType;

    private ItemStatus searchItemStatus;

    private String searchBy;

    private String searchQuery = "";

}

package dokuny.shop.repository;

import dokuny.shop.dto.ItemSearchDto;
import dokuny.shop.dto.MainItemDto;
import dokuny.shop.entity.Item;
import dokuny.shop.entity.constant.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable, Category category);



}

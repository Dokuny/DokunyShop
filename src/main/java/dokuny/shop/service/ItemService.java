package dokuny.shop.service;

import dokuny.shop.dto.ItemFormDto;
import dokuny.shop.dto.ItemImgDto;
import dokuny.shop.dto.ItemSearchDto;
import dokuny.shop.dto.MainItemDto;
import dokuny.shop.entity.Item;
import dokuny.shop.entity.ItemImg;
import dokuny.shop.entity.constant.Category;
import dokuny.shop.repository.ItemImgRepository;
import dokuny.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

        Item item = itemFormDto.toEntity();
        itemRepository.save(item);

        for (int i = 0; i < itemImgFileList.size(); i++) {

            ItemImg itemImg = ItemImg.builder()
                    .item(item)
                    .build();


            if (i == 0) {
                itemImg.selectRepImg("Y");
            } else {
                itemImg.selectRepImg("N");
            }
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }
        return item.getId();
    }

    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId) {
        List<ItemImg> itemImgList = itemImgRepository.findByItem_IdOrderByIdAsc(itemId);
        if (itemImgList.isEmpty()) {
            return null;
        }


        List<ItemImgDto> itemImgDtoList = new ArrayList<>();

        log.info("itemImgList is null? : {}", itemImgList);

        for (ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);

        ItemFormDto itemFormDto = ItemFormDto.of(item);

        itemFormDto.setItemImgDtoList(itemImgDtoList);


        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {
        Item item = itemRepository.findById(itemFormDto.getId()).orElseThrow(EntityNotFoundException::new);

        item.updateItem(itemFormDto);

        List<Long> itemImgIds = itemFormDto.getItemImgIds();

        for (int i = 0; i < itemImgFileList.size(); i++) {
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        }

        return item.getId();
    }

    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<MainItemDto> getItemPage(ItemSearchDto itemSearchDto, Pageable pageable, Category category) {
        return itemRepository.getMainItemPage(itemSearchDto,pageable,category);
    }


}

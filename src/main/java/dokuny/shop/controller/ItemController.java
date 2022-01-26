package dokuny.shop.controller;

import dokuny.shop.dto.ItemFormDto;
import dokuny.shop.dto.ItemSearchDto;
import dokuny.shop.dto.MainItemDto;
import dokuny.shop.entity.Item;
import dokuny.shop.entity.constant.Category;
import dokuny.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.swing.text.html.Option;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class ItemController {

    private final ItemService itemService;


    @GetMapping("/admin/item/new")
    public String itemNewForm(Model model) {
        model.addAttribute("dto", new ItemFormDto());
        return "item/itemForm";
    }

    @PostMapping("/admin/item/new")
    public String itemNew(@Valid ItemFormDto dto,
                          BindingResult br, Model model,
                          @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {

        if (br.hasErrors()) {
            return "item/itemForm";
        }

        if (itemImgFileList.get(0).isEmpty() && dto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }

        try {
            itemService.saveItem(dto, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/";
    }

    @GetMapping("/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model) {
        try {
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);

            model.addAttribute("dto", itemFormDto);

        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다");
            model.addAttribute("dto", new ItemFormDto());

            return "item/itemForm";
        }
        return "item/itemForm";
    }

    @PostMapping("/admin/item/{itemId}")
    public String itemUpdate(@ModelAttribute("dto") @Valid ItemFormDto itemFormDto,BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList,Model model) {
        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage","첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }

        try {
            itemService.updateItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage","상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }
        return "redirect:/";
    }

    @GetMapping(value = {"/admin/items", "/admin/items/{page}"})
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);

        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);
        return "item/itemMng";
    }

    @GetMapping("/item/{itemId}")
    public String itemDetail(Model model,@PathVariable("itemId") Long itemId) {
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);

        if (itemFormDto == null) {
            return "main";
        }
        model.addAttribute("item",itemFormDto);

        return "item/itemDetail";
    }

    @GetMapping(value={"/item/category/{categoryNm}","/item/category/{categoryNm}/{page}"})
    private String womenPage(ItemSearchDto itemSearchDto,@PathVariable("categoryNm") String cartegoryNm,@PathVariable("page") Optional<Integer> page, Model model) {
        Category category = Category.parseCategory(cartegoryNm);
        System.out.println("페이지는 : " + page);
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        Page<MainItemDto> items = itemService.getItemPage(itemSearchDto, pageable, category);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto",itemSearchDto);
        model.addAttribute("category",category.name().toLowerCase());
        model.addAttribute("maxPage",5);
        return "item/categorizedItems";
    }


}

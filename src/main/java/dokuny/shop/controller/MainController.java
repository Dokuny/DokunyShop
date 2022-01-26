package dokuny.shop.controller;

import dokuny.shop.dto.ItemSearchDto;
import dokuny.shop.dto.MainItemDto;
import dokuny.shop.entity.constant.Category;
import dokuny.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class MainController {

    private final ItemService itemService;

    @GetMapping("/")
    public String main(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        Page<MainItemDto> men = itemService.getItemPage(itemSearchDto, pageable, Category.MEN);
        Page<MainItemDto> women = itemService.getItemPage(itemSearchDto, pageable, Category.WOMEN);
        Page<MainItemDto> kids = itemService.getItemPage(itemSearchDto, pageable, Category.KIDS);
        Page<MainItemDto> acc = itemService.getItemPage(itemSearchDto, pageable, Category.ACCESSORIES);

        model.addAttribute("men", men);
        model.addAttribute("women", women);
        model.addAttribute("kids", kids);
        model.addAttribute("acc", acc);

        return "main";
    }



}

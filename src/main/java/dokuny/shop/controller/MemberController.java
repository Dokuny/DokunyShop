package dokuny.shop.controller;

import dokuny.shop.dto.MemberFormDto;
import dokuny.shop.service.MemberService;
import dokuny.shop.utils.JoinFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;
    private final JoinFormValidator joinFormValidator;

    @GetMapping("/auth/join")
    public String joinForm(@ModelAttribute("dto") MemberFormDto dto) {
        return "auth/join";
    }

    @PostMapping("/auth/join")
    public String join(Model model, @ModelAttribute("dto") @Validated MemberFormDto dto, BindingResult br) {
        System.out.println(dto.getPw());
        System.out.println(dto.getPwCheck());

        if (br.hasErrors()) {
            model.addAttribute("dto",dto);
            System.out.println("1번 에러");
            return "auth/join";
        }

        joinFormValidator.validate(dto,br);

        if (br.hasErrors()) {
            model.addAttribute("dto",dto);
            System.out.println("2번 에러");
            return "auth/join";
        }

        Long memberId = memberService.joinMember(dto);

        return "redirect:/";
    }
}

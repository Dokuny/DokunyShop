package dokuny.shop.controller;

import dokuny.shop.dto.MemberFormDto;
import dokuny.shop.service.MemberService;
import dokuny.shop.utils.JoinFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@RequiredArgsConstructor
@RequestMapping("auth")
@Controller
public class MemberController {

    private final MemberService memberService;
    private final JoinFormValidator joinFormValidator;

    @GetMapping("/join")
    public String joinForm(@ModelAttribute("dto") MemberFormDto dto) {
        return "auth/join";
    }

    @PostMapping("/join")
    public String join(Model model, @ModelAttribute("dto") @Validated MemberFormDto dto, BindingResult br) {
        if (br.hasErrors()) {
            model.addAttribute("dto", dto);
            return "auth/join";
        }

        joinFormValidator.validate(dto, br);

        if (br.hasErrors()) {
            model.addAttribute("dto", dto);
            return "auth/join";
        }

        Long memberId = memberService.joinMember(dto);

        return "redirect:/auth/login";
    }

    @GetMapping("/login")
    public String loginForm() {

        return "auth/login";
    }



}

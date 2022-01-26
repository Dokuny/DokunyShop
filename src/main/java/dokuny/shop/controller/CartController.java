package dokuny.shop.controller;


import dokuny.shop.dto.CartDetailDto;
import dokuny.shop.dto.CartOrderDto;
import dokuny.shop.dto.OrderCartDto;
import dokuny.shop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/cart")
@Controller
public class CartController {
    private final CartService cartService;

    @ResponseBody
    @PostMapping("/add")
    public ResponseEntity addItem(@RequestBody @Valid OrderCartDto cartDto, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();

            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }
        String email = principal.getName();
        System.out.println(email);
        Long cartItemId;

        try {
            cartItemId = cartService.addCart(cartDto, email);

        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long>(cartItemId,HttpStatus.OK);
    }

    @GetMapping
    public String cartList(Principal principal, Model model) {
        List<CartDetailDto> cartDetailDtoList = cartService.getCartList(principal.getName());

        model.addAttribute("cartItems", cartDetailDtoList);
        return "cart/cartList";
    }

    @ResponseBody
    @PatchMapping("/{cartItemId}")
    public ResponseEntity updateCartItem(@PathVariable("cartItemId") Long cartItemId, int count, Principal principal) {
        if (count <= 0) {
            return new ResponseEntity<String>("최소 1개 이상 담아주세요",HttpStatus.BAD_REQUEST);
        }

        cartService.updateCartItemCount(cartItemId,count);
        return new ResponseEntity<Long>(cartItemId,HttpStatus.OK);
    }

    @ResponseBody
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity deleteCartItem(@PathVariable("cartItemId") Long cartItemId, Principal principal) {
        cartService.deleteCartItem(cartItemId);
        return new ResponseEntity<Long>(cartItemId,HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/orders")
    public ResponseEntity orderCartItem(@RequestBody CartOrderDto cartOrderDto, Principal principal) {
        List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();

        if (cartOrderDtoList == null || cartOrderDtoList.size() == 0) {
            return new ResponseEntity<String>("주문할 상품을 선택해주세요",HttpStatus.BAD_REQUEST);
        }

        Long orderId = cartService.orderCartItem(cartOrderDtoList, principal.getName());
        return new ResponseEntity<Long>(orderId,HttpStatus.OK);
    }
}

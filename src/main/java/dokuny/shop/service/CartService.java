package dokuny.shop.service;


import dokuny.shop.dto.CartDetailDto;
import dokuny.shop.dto.CartOrderDto;
import dokuny.shop.dto.OrderCartDto;
import dokuny.shop.dto.OrderDto;
import dokuny.shop.entity.Cart;
import dokuny.shop.entity.CartItem;
import dokuny.shop.entity.Item;
import dokuny.shop.entity.Member;
import dokuny.shop.exception.AlreadyExistInCartException;
import dokuny.shop.repository.CartItemRepository;
import dokuny.shop.repository.CartRepository;
import dokuny.shop.repository.ItemRepository;
import dokuny.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Transactional
@Service
public class CartService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderService orderService;

    public Long addCart(OrderCartDto orderCartDto, String email) {

        Member member = memberRepository.findMemberByEmail(email);

        Cart cart = cartRepository.findByMember_Id(member.getId());

        if (cart == null) {
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        if (cartItemRepository.existsByItem_IdAndCart_Id(orderCartDto.getItemId(), cart.getId())) {
            throw new AlreadyExistInCartException("이미 카트에 담겨져 있는 상품 입니다.");
        }

        Item item = itemRepository.findById(orderCartDto.getItemId()).orElseThrow(EntityNotFoundException::new);


        CartItem cartItem = CartItem.createCartItem(cart, item, orderCartDto.getCount());
        cartItemRepository.save(cartItem);

        return cartItem.getId();
    }

    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String email) {
        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();

        Member member = memberRepository.findMemberByEmail(email);
        Cart cart = cartRepository.findByMember_Id(member.getId());
        if (cart == null) {
            return cartDetailDtoList;
        }

        cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId());

        return cartDetailDtoList;
    }

    public void updateCartItemCount(Long cartItemId, int count) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);

        cartItem.updateCount(count);
    }

    public void deleteCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);
    }

    public Long orderCartItem(List<CartOrderDto> cartOrderDtoList, String email) {
        List<OrderDto> orderDtoList = new ArrayList<>();
        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId()).orElseThrow(EntityNotFoundException::new);

            OrderDto orderDto = new OrderDto();
            orderDto.setItemId(cartItem.getItem().getId());
            orderDto.setCount(cartItem.getCount());
            orderDtoList.add(orderDto);
        }

        Long orderId = orderService.orders(orderDtoList, email);

        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId()).orElseThrow(EntityNotFoundException::new);
            cartItemRepository.delete(cartItem);
        }

        return orderId;
    }
}

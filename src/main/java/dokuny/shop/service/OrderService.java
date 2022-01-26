package dokuny.shop.service;

import dokuny.shop.dto.OrderCartDto;
import dokuny.shop.dto.OrderDto;
import dokuny.shop.dto.OrderHistDto;
import dokuny.shop.dto.OrderItemDto;
import dokuny.shop.entity.*;
import dokuny.shop.repository.ItemImgRepository;
import dokuny.shop.repository.ItemRepository;
import dokuny.shop.repository.MemberRepository;
import dokuny.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class OrderService {

    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;


    public Long order(OrderDto orderDto, String email) {
        Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);

        Member member = memberRepository.findMemberByEmail(email);

        List<OrderItem> orderItemList = new ArrayList<>();

        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());

        orderItemList.add(orderItem);

        Order order = Order.createOrder(member, orderItemList);

        orderRepository.save(order);

        return order.getId();
    }

    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable) {
        List<Order> orders = orderRepository.findOrders(email, pageable);
        Long totalCount = orderRepository.countOrder(email);
        List<OrderHistDto> orderHistDtos = new ArrayList<>();

        for (Order order : orders) {
            OrderHistDto orderHistDto = new OrderHistDto(order);

            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                ItemImg itemImg = itemImgRepository.findByItem_IdAndRepimgYn(orderItem.getItem().getId(), "Y");
                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHistDto.addOrderItemDto(orderItemDto);
            }
            orderHistDtos.add(orderHistDto);
        }
        return new PageImpl<OrderHistDto>(orderHistDtos,pageable,totalCount);
    }

    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);

        order.cancelOrder();
    }


    public Long orders(List<OrderDto> orderDtoList, String email) {
        Member member = memberRepository.findMemberByEmail(email);

        List<OrderItem> orderItemList = new ArrayList<>();

        for (OrderDto orderDto : orderDtoList) {
            Item item = itemRepository.findById(orderDto.getItemId())
                    .orElseThrow(EntityNotFoundException::new);

            OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
            orderItemList.add(orderItem);
        }

        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }
}

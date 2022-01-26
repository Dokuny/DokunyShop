package dokuny.shop.repository;

import dokuny.shop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long> {

    Cart findByMember_Id(Long memberId);
}

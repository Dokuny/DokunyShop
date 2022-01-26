package dokuny.shop.repository;

import dokuny.shop.dto.CartDetailDto;
import dokuny.shop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByCart_Id(Long cartId);

    Boolean existsByItem_IdAndCart_Id(Long itemId, Long cartId);

    @Query("select new dokuny.shop.dto.CartDetailDto(ci.id,i.name,i.price,ci.count,im.imgUrl) " +
            "from CartItem ci,ItemImg im " +
            "join ci.item i " +
            "where ci.cart.id = :cartId " +
            "and im.item.id = ci.item.id " +
            "and im.repimgYn = 'Y' " +
            "order by ci.regTime desc"
    )
    List<CartDetailDto> findCartDetailDtoList(@Param("cartId") Long cartId);
}

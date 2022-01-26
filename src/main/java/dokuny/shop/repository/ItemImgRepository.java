package dokuny.shop.repository;

import dokuny.shop.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg,Long> {

    List<ItemImg> findByItem_IdOrderByIdAsc(Long itemId);

    ItemImg findByItem_IdAndRepimgYn(Long itemId,String repimgYn);
}

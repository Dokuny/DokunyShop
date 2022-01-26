package dokuny.shop.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import dokuny.shop.entity.Item;
import dokuny.shop.entity.QMember;
import dokuny.shop.entity.constant.ItemStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;


@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("상품 저장 테스트")
    public void createItemTest() {
        Item item = Item.builder()
                .name("테스트 상품")
                .price(10000)
                .itemDetail("테스트 상품 설명")
                .itemStatus(ItemStatus.SELL)
                .stockQuantity(100)
                .build();

        Item savedItem = itemRepository.save(item);

        Assertions.assertThat(savedItem).isEqualTo(item);
    }

}
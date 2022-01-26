package dokuny.shop.repository;

import dokuny.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item,Long>, QuerydslPredicateExecutor<Item>,ItemRepositoryCustom {

    List<Item> findByName(String name);


}

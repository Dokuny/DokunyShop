package dokuny.shop.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dokuny.shop.dto.ItemSearchDto;
import dokuny.shop.dto.MainItemDto;
import dokuny.shop.dto.QMainItemDto;
import dokuny.shop.entity.Item;
import dokuny.shop.entity.QItem;
import dokuny.shop.entity.QItemImg;
import dokuny.shop.entity.constant.Category;
import dokuny.shop.entity.constant.ItemStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public ItemRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchStatusEq(ItemStatus searchItemStatus) {
        return searchItemStatus == null ? null : QItem.item.itemStatus.eq(searchItemStatus);
    }

    private BooleanExpression regDtsAfter(String searchDateType) {
        LocalDateTime dateTime = LocalDateTime.now();

        if (StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return null;
        } else if (StringUtils.equals("1d", searchDateType)) {
            dateTime = dateTime.minusDays(1);
        } else if (StringUtils.equals("1w", searchDateType)) {
            dateTime = dateTime.minusWeeks(1);
        } else if (StringUtils.equals("1m", searchDateType)) {
            dateTime = dateTime.minusMonths(1);
        } else if (StringUtils.equals("6m", searchDateType)) {
            dateTime = dateTime.minusMonths(6);
        }

        return QItem.item.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if (StringUtils.equals("name", searchBy)) {
            return QItem.item.name.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("createdBy", searchBy)) {
            return QItem.item.createdBy.like("%" + searchQuery + "%");
        }

        return null;
    }


    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {

        QueryResults<Item> results = queryFactory.selectFrom(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchStatusEq(itemSearchDto.getSearchItemStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                // deprecated 되어서 fetch()로 처리하는게 좋지만 count 때문에 일단 넣어둠
                .fetchResults();

        List<Item> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression itemNmLike(String searchQuery) {
        return StringUtils.isEmpty(searchQuery) ? null : QItem.item.name.like("%" + searchQuery + "%");
    }


    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable, Category category) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        QueryResults<MainItemDto> results = queryFactory.select(
                        new QMainItemDto(
                                item.id,
                                item.name,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price
                        )
                )
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(item.category.eq(category))
                .where(item.itemStatus.eq(ItemStatus.SELL))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MainItemDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content,pageable,total);
    }

}

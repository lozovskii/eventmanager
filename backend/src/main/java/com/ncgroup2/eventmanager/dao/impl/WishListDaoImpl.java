package com.ncgroup2.eventmanager.dao.impl;

import com.ncgroup2.eventmanager.dao.DAO;
import com.ncgroup2.eventmanager.entity.Entity;
import com.ncgroup2.eventmanager.entity.Item;
import com.ncgroup2.eventmanager.entity.WishList;
import com.ncgroup2.eventmanager.mapper.WishListMapExtractor;
import com.ncgroup2.eventmanager.dto.ItemWishListDto;
import com.ncgroup2.eventmanager.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.*;

@Repository
@Transactional
public class WishListDaoImpl extends JdbcDaoSupport implements DAO {

    @Autowired
    private DataSource dataSource;

    @Autowired
    ItemDaoImpl itemDao;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }


    /**
     * @return collection of wish lists where every wish list has id = event_id.
     */
    @Override
    public Collection<WishList> getAll() {

        String sql =
                "SELECT   ew.id AS event_wishlist_id," +
                        " ew.event_id AS event_id," +
                        " iw.id AS item_wishlist_id," +
                        " iw.booker_customer_id," +
                        " iw.priority," +
                        " i.* " +
                        "FROM \"Item\" i " +
                        "INNER JOIN \"Item_WishList\" iw ON (iw.item_id = i.id)\t " +
                        "INNER JOIN \"Event_WishList\" ew ON (ew.item_wishlist_id = iw.id) ;";

        Map<String, List<ItemWishListDto>> wishListMap =
                this.getJdbcTemplate().query(sql, new WishListMapExtractor());

        return wishListMap.isEmpty() ?
                null : Mapper.mapWishListToCollection(wishListMap);
    }

    /**
     * @param wishlist_id wish list id in table "Event_WishList"
     * @return wish list
     */
    @Override
    public WishList getById(Object wishlist_id) {

        return Objects.requireNonNull(
                getEntityByField("ew.id", String.valueOf(wishlist_id)),
                "Wish list not found");
    }

    @Override
    public WishList getEntityByField(String fieldName, Object fieldValue) {

        return  getEntitiesByField(fieldName, fieldValue) != null ?
                getEntitiesByField(fieldName, fieldValue).iterator().next() :
                null;
    }

    @Override
    public Collection<WishList> getEntitiesByField(String fieldName, Object fieldValue) {

        String sql =
                "SELECT   ew.id AS event_wishlist_id," +
                        " ew.event_id AS event_id," +
                        " iw.id AS item_wishlist_id," +
                        " iw.booker_customer_id," +
                        " iw.priority," +
                        " i.* " +
                        "FROM \"Item\" i " +
                        "INNER JOIN \"Item_WishList\" iw ON (iw.item_id = i.id)\t " +
                        "INNER JOIN \"Event_WishList\" ew ON (ew.item_wishlist_id = iw.id) " +
                        "WHERE " + fieldName + " = CAST ('" + fieldValue + "' AS UUID)";

        Map<String, List<ItemWishListDto>> wishListMap =
                this.getJdbcTemplate().query(sql, new WishListMapExtractor(fieldName));

        return wishListMap.isEmpty() ?
                null : Mapper.mapWishListToCollection(wishListMap);
    }

    @Override
    public void update(Entity entity) {

        WishList wishList = (WishList) entity;

        String updateItem_WishListSql =
                "UPDATE \"Item_WishList\" " +
                        "SET booker_customer_id=?, priority=? " +
                        "WHERE id=?; ";

        List<ItemWishListDto> items = wishList.getItems();

        this.getJdbcTemplate().batchUpdate(
                updateItem_WishListSql, items, items.size(),
                (ps, itemDto) -> {

                    ps.setString(1, itemDto.getBooker_customer_id());
                    ps.setInt(2, itemDto.getPriority());
                    ps.setString(3, itemDto.getItem_wishlist_id());
                });
    }

    /**
     * Allows possibility to update booker id or set new priority
     *
     * @param item_wishlist_id id of table "Item_Wishlist"
     * @param fieldName        booker_id or priority_id
     * @param fieldValue       uuid
     */
    @Override
    public void updateField(Object item_wishlist_id, String fieldName, Object fieldValue) {

        String updateItem_WishListSql =
                "UPDATE \"Item_WishList\" " +
                        "SET " + fieldName + " = " + fieldValue +
                        " WHERE id = CAST ('" + item_wishlist_id + "' AS UUID);";

        this.getJdbcTemplate().update(updateItem_WishListSql);

    }

    /**
     * Removes wish list
     *
     * @param id id of table "Event_WishList"
     */
    @Override
    public void delete(Object id) {

        String deleteSql =
                "DELETE " +
                        "FROM \"Event_WishList\" ew " +
                        "WHERE ew.id = CAST ('" + id + "' AS UUID);";

        this.getJdbcTemplate().update(deleteSql);

    }

    /**
     * Removes items in the wish list
     *
     * @param trash
     */
    public void deleteItems(List<ItemWishListDto> trash) {

        if (trash != null && (!trash.isEmpty())) {

            String itemDeleteSql =
                    "DELETE FROM \"Event_WishList\" " +
                            "WHERE id = ?::UUID";

            this.getJdbcTemplate().batchUpdate(
                    itemDeleteSql, trash, trash.size(),
                    (ps, itemDto) -> ps.setString(1, itemDto.getEvent_wishlist_id()));
        }
    }

    /**
     * Creates new entry in table "Event_WishList".
     * Expected that object "ItemWishListDto" has all required ties
     *
     * @param entity Object "WishList"
     */
    @Override
    public void create(Entity entity) {

        WishList wishList = (WishList) entity;

        addItems(wishList.getItems());

        List<ItemWishListDto> items = wishList.getItems();

        String itemWishListInsertSql =
                "INSERT INTO \"Item_WishList\"" +
                        "(id, item_id, booker_customer_id, priority)" +
                        "VALUES (?::UUID, ?::UUID, ?::UUID, ?::SMALLINT);";

        this.getJdbcTemplate().batchUpdate(
                itemWishListInsertSql,
                items, items.size(),
                (ps, item) -> {
                    Item item1 = item.getItem();
                    ps.setString(1, item.getItem_wishlist_id());
                    ps.setString(2, item1.getId());
                    ps.setString(3, item.getBooker_customer_id());
                    ps.setInt(4, item.getPriority());

                });

        String eventWishListInsertSql =
                "INSERT INTO \"Event_WishList\"" +
                        "(event_id, item_wishlist_id)" +
                        "VALUES (?::UUID, ?::UUID);";

        this.getJdbcTemplate().batchUpdate(
                eventWishListInsertSql,
                items, items.size(),
                (ps, item) -> {
                    ps.setString(1, wishList.getId());
                    ps.setString(2, item.getItem_wishlist_id());
                });
    }

    public void addItems(List<ItemWishListDto> itemWishListDtos) {

        List<Item> items = (List<Item>) Mapper.mapDtoItemToItemCollection(itemWishListDtos);

        itemDao.createItems(items);
    }
}

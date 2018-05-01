package com.ncgroup2.eventmanager.dao.impl.postgres;

import com.ncgroup2.eventmanager.dao.DAO;
import com.ncgroup2.eventmanager.entity.Entity;
import com.ncgroup2.eventmanager.entity.WishList;
import com.ncgroup2.eventmanager.mapper.WishListMapExtractor;
import com.ncgroup2.eventmanager.dto.ItemWishListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Transactional
public class WishListDaoImpl extends JdbcDaoSupport implements DAO {

    @Autowired
    private DataSource dataSource;

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
                "SELECT   ew.id as event_wishlist_id," +
                        " ew.event_id AS event_id," +
                        " iw.id AS item_wishlist_id," +
                        " iw.booker_customer_id," +
                        " iw.priority," +
                        " i.* " +
                        "FROM \"Item\" i " +
                        "INNER JOIN \"Item_WishList\" iw ON (iw.item_id = i.id)\t " +
                        "INNER JOIN \"Event_WishList\" ew ON (ew.item_wishlist_id = iw.id) ";

        Map<String, List<ItemWishListDto>> wishListMap =
                this.getJdbcTemplate().query(sql, new WishListMapExtractor());

        Collection<WishList> wishListCollection = new ArrayList<>();

        // convert via foreach

        for (String key : wishListMap.keySet()) {

            WishList wishList = new WishList();

            wishList.setId(key);

            wishList.setItems(wishListMap.get(key));

            wishListCollection.add(wishList);
        }

        return wishListCollection;

    }

    /**
     * @param wishlist_id wish list id in table "Event_WishList"
     * @return wish list
     */
    @Override
    public WishList getById(Object wishlist_id) {

        return getEntityByField("ew.id", String.valueOf(wishlist_id));
    }

    @Override
    public WishList getEntityByField(String fieldName, Object fieldValue) {

        Collection<WishList> wishLists = getEntitiesByField(fieldName, fieldValue);

        if (!wishLists.isEmpty()) {

            return getEntitiesByField(fieldName, fieldValue).iterator().next();

        } else {

            return null;
        }
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

        //convert via stream

        Collection<WishList> wishListCollection = wishListMap.entrySet()
                .stream()
                .map( temp -> {
                    WishList wishList = new WishList();
                    wishList.setId(temp.getKey());
                    wishList.setItems(temp.getValue());
                    return wishList;
                })
                .collect(
                        Collectors.toList());

        return wishListCollection;
    }

    @Override
    public void update(Entity entity) {

        WishList wishList = (WishList)entity;

        String updateItemSql =
                        "UPDATE \"Item\" " +
                        "SET name=?, description=?, image=?, link=? " +
                        "WHERE id = ?; ";

        String updateItem_WishListSql =
                        "UPDATE \"Item_WishList\" " +
                        "SET booker_customer_id=?, priority=? " +
                        "WHERE id=?; ";

        String updateEvent_WishListSql =
                        "UPDATE \"Event_WishList\" " +
                        "SET item_wishlist_id=? " +
                        "WHERE id = ?; ";

        String sql = updateItemSql + updateItem_WishListSql + updateEvent_WishListSql;

//        for (ItemWishListDto item : wishList.getItems()) {
//
//            Object[] params = new Object[]{
//                    item.getBooker_customer_id(),
//                    item.getPriority(),
//                    item.getItem_wishlist_id(),
//
//                    item.getItem_wishlist_id(),
//                    item.getEvent_wishlist_id()
//            };
//
//            this.getJdbcTemplate().update(sql, params);
//
//        }


        List<ItemWishListDto> items = wishList.getItems();

        this.getJdbcTemplate().batchUpdate(
                sql,
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, items.get(i).getItem().getName());
                        ps.setString(2, items.get(i).getItem().getDescription());
                        ps.setString(3, items.get(i).getItem().getImage());
                        ps.setString(4, items.get(i).getItem().getLink());
                        ps.setString(5, items.get(i).getItem().getId());

                        ps.setString(6, items.get(i).getBooker_customer_id());
                        ps.setString(7, items.get(i).getPriority());
                        ps.setString(8, items.get(i).getItem_wishlist_id());

                        ps.setString(9, items.get(i).getItem_wishlist_id());
                        ps.setString(10,items.get(i).getEvent_wishlist_id());
                    }

                    public int getBatchSize() {
                        return items.size();
                    }
                } );
    }

    /**
     * Allows possibility to update booker id or set new priority
     * @param item_wishlist_id
     * @param fieldName
     * @param fieldValue
     */
    @Override
    public void updateField(Object item_wishlist_id, String fieldName, Object fieldValue) {

        String updateItem_WishListSql =
                        "UPDATE \"Item_WishList\" " +
                        "SET "+ fieldName + " = "+ fieldValue +
                        " WHERE id = CAST ('"+ item_wishlist_id + "' AS UUID);";

        this.getJdbcTemplate().update(updateItem_WishListSql);

    }

    /**
     * Deletes wish list
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
     * Creates new entry in table "Event_WishList".
     * Expected that object "ItemWishListDto" has all required ties
     * @param entity Object "WishList"
     */
    @Override
    public void create(Entity entity) {

        WishList wishList = (WishList)entity;

        String insertSql =
                "INSERT INTO \"Event_WishList\"" +
                "(event_id, item_wishlist_id)" +
                "VALUES (CAST (? AS uuid), CAST (? AS uuid));";

        List<ItemWishListDto> items = wishList.getItems();

        this.getJdbcTemplate().batchUpdate(
                insertSql,
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {

                        ps.setString(1, wishList.getId());
                        ps.setString(2, items.get(i).getItem_wishlist_id());
                    }

                    public int getBatchSize() {
                        return items.size();
                    }
                } );
    }
}

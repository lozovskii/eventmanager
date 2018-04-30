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


    @Override
    public Collection<WishList> getAll() {

        String sql =
                "SELECT ew.event_id AS event_id," +
                        " iw.id AS item_wishlist_id," +
                        " iw.booker_customer_id," +
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

    @Override
    public WishList getById(Object event_id) {

        return getEntityByField("event_id", String.valueOf(event_id));
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
                "SELECT  ew.id as event_wishlist_id," +
                        "ew.event_id AS event_id," +
                        " iw.id AS item_wishlist_id," +
                        " iw.booker_customer_id," +
                        " iw.priority," +
                        " i.* " +
                        "FROM \"Item\" i " +
                        "INNER JOIN \"Item_WishList\" iw ON (iw.item_id = i.id)\t " +
                        "INNER JOIN \"Event_WishList\" ew ON (ew.item_wishlist_id = iw.id) " +
                        "WHERE " + fieldName + " = CAST ('" + fieldValue + "' AS UUID)";

        Map<String, List<ItemWishListDto>> wishListMap =
                this.getJdbcTemplate().query(sql, new WishListMapExtractor());

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

        String updateItem_WishListSql =
                        "UPDATE \"Item_WishList\" " +
                        "SET booker_customer_id=?, priority=? " +
                        "WHERE id=?; ";

        String updateEvent_WishListSql =
                        "UPDATE \"Event_WishList\" " +
                        "SET item_wishlist_id=? " +
                        "WHERE id = ?; ";

        String updateItemSql =
                        "UPDATE \"Item\" " +
                        "SET name=?, description=?, image=?, link=? " +
                        "WHERE id = ?; ";

        String sql = updateItem_WishListSql + updateEvent_WishListSql;

        for (ItemWishListDto item : wishList.getItems()) {

            Object[] params = new Object[]{
                    item.getBooker_customer_id(),
                    item.getPriority(),
                    item.getItem_wishlist_id(),

                    item.getItem_wishlist_id(),
                    item.getEvent_wishlist_id()
            };

            this.getJdbcTemplate().update(sql, params);

        }


        List<ItemWishListDto> items = wishList.getItems();

        this.getJdbcTemplate().batchUpdate(
                updateItemSql,
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, items.get(i).getItem().getName());
                        ps.setString(2, items.get(i).getItem().getDescription());
                        ps.setString(3, items.get(i).getItem().getImage());
                        ps.setString(4, items.get(i).getItem().getLink());
                        ps.setString(5, items.get(i).getItem().getId());
                    }

                    public int getBatchSize() {
                        return items.size();
                    }
                } );
    }

    @Override
    public void updateField(Object id, String fieldName, Object fieldValue) {

    }

    @Override
    public void delete(Object id) {

    }

    @Override
    public void create(Entity entity) {

    }
}

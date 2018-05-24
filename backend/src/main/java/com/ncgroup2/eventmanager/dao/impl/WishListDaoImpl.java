package com.ncgroup2.eventmanager.dao.impl;

import com.ncgroup2.eventmanager.dao.WishListDao;
import com.ncgroup2.eventmanager.entity.Item;
import com.ncgroup2.eventmanager.entity.WishList;
import com.ncgroup2.eventmanager.mapper.WishListMapExtractor;
import com.ncgroup2.eventmanager.objects.WishListItem;
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
public class WishListDaoImpl extends JdbcDaoSupport implements WishListDao {

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
                "SELECT   i.*,\n" +
                        " ew.id as event_wishlist_id, \n" +
                        " ew.event_id AS event_id,\n" +
                        " iw.id AS item_wishlist_id,\n" +
                        " iw.booker_customer_login,\n" +
                        " iw.priority,\n" +
                        " array_agg(DISTINCT row(itag.id, t.*)) FILTER (WHERE itag.id IS NOT NULL) as tags,\n" +
                        " array_agg(DISTINCT r.*) FILTER (WHERE r.id IS NOT NULL) as rating\n" +
                        " FROM \"Item\" i\n" +
                        " LEFT JOIN \"Item_WishList\" iw ON (iw.item_id = i.id)\t\n" +
                        " LEFT JOIN \"Event_WishList\" ew ON (ew.item_wishlist_id = iw.id)\n" +
                        " LEFT JOIN \"Item_Tag\" itag ON (itag.item_id = i.id)\t\n" +
                        " LEFT JOIN \"Tag\" t ON (itag.tag_id = t.id)\n" +
                        " LEFT JOIN \"Rating_Item\" r ON (r.item_id = i.id)\n" +
                        " GROUP BY i.id,ew.id, ew.event_id,iw.id;";

        return this.getJdbcTemplate().query(sql, new WishListMapExtractor());
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
        Collection<WishList> wishLists = getEntitiesByField(fieldName, fieldValue);

        return wishLists != null ?
                wishLists.iterator().next() : null;
    }

    @Override
    public Collection<WishList> getEntitiesByField(String fieldName, Object fieldValue) {
        String castSql;

        if (fieldValue.toString().matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")) {
            castSql = " = '" + fieldValue + "' :: UUID";
        } else {
            castSql = " = '" + fieldValue + "' :: CHARACTER VARYING ";
        }

        String sql =
                "SELECT   i.*,\n" +
                        " ew.id as event_wishlist_id, \n" +
                        " ew.event_id AS event_id,\n" +
                        " iw.id AS item_wishlist_id,\n" +
                        " iw.booker_customer_login,\n" +
                        " iw.priority,\n" +
                        " array_agg(DISTINCT row(itag.id, t.*)) FILTER (WHERE itag.id IS NOT NULL) as tags,\n" +
                        " array_agg(DISTINCT r.*) FILTER (WHERE r.id IS NOT NULL) as rating\n" +
                        " FROM \"Item\" i\n" +
                        " FULL JOIN \"Item_WishList\" iw ON (iw.item_id = i.id)\t\n" +
                        " FULL JOIN \"Event_WishList\" ew ON (ew.item_wishlist_id = iw.id)\n" +
                        " FULL JOIN \"Item_Tag\" itag ON (itag.item_id = i.id)\t\n" +
                        " FULL JOIN \"Tag\" t ON (itag.tag_id = t.id)\n" +
                        " FULL JOIN \"Rating_Item\" r ON (r.item_id = i.id)\n" +
                        " WHERE " + fieldName + castSql +
                        " GROUP BY i.id,ew.id, ew.event_id,iw.id;";

        return this.getJdbcTemplate().query(sql, new WishListMapExtractor(fieldName));
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
                        " WHERE id = '" + item_wishlist_id + "' ::UUID;";

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
                        "WHERE ew.id = '" + id + "' ::UUID;";

        this.getJdbcTemplate().update(deleteSql);

    }

    /**
     * Removes items in the wish list
     *
     * @param trash
     */
    public void deleteItems(List<WishListItem> trash) {
        if (trash != null && (!trash.isEmpty())) {

            String itemDeleteSql =
                    "DELETE FROM \"Event_WishList\" " +
                            "WHERE id = ?::UUID";

            this.getJdbcTemplate().batchUpdate(
                    itemDeleteSql, trash, trash.size(),
                    (ps, itemDto) -> ps.setString(1, itemDto.getEvent_wishlist_id()));
        }
    }

    @Override
    public void update(WishList wishList) {
        List<WishListItem> items = wishList.getItems();

        String updateItem_WishListSql =
                "UPDATE \"Item_WishList\" " +
                        "SET booker_customer_login=?, priority=? " +
                        "WHERE id=?::UUID; ";

        this.getJdbcTemplate().batchUpdate(
                updateItem_WishListSql, items, items.size(),
                (ps, itemDto) -> {

                    ps.setString(1, itemDto.getBooker_customer_login());
                    ps.setInt(2, itemDto.getPriority());
                    ps.setString(3, itemDto.getItem_wishlist_id());
                });
    }


    /**
     * Creates new entry in table "Event_WishList".
     * Expected that object "ItemWishListDto" has all required ties
     *
     * @param wishList Object "WishList"
     */
    @Override
    public void create(WishList wishList) {
        addItems(wishList);
        update(wishList);
    }


    public void addItems(WishList wishList) {
        List<WishListItem> items = wishList.getItems();

        String itemWishListInsertSql =
                "INSERT INTO \"Item_WishList\"" +
                        "(id, item_id, priority)" +
                        "VALUES (?::UUID, ?::UUID, ?::SMALLINT)" +
                        "ON CONFLICT (id) DO NOTHING;";

        this.getJdbcTemplate().batchUpdate(
                itemWishListInsertSql,
                items, items.size(),
                (ps, item) -> {
                    Item item1 = item.getItem();
                    ps.setString(1, item.getItem_wishlist_id());
                    ps.setString(2, item1.getId());
                    ps.setInt(3, item.getPriority());

                });

        String eventWishListInsertSql =
                "INSERT INTO \"Event_WishList\"" +
                        "(id, event_id, item_wishlist_id)" +
                        "VALUES (?::UUID,?::UUID, ?::UUID)" +
                        "ON CONFLICT (id) DO NOTHING;";

        this.getJdbcTemplate().batchUpdate(
                eventWishListInsertSql,
                items, items.size(),
                (ps, item) -> {
                    ps.setString(1, item.getEvent_wishlist_id());
                    ps.setString(2, wishList.getId());
                    ps.setString(3, item.getItem_wishlist_id());
                });
    }
}

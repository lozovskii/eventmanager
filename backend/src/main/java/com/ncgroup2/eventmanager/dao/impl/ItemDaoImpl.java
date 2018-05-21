package com.ncgroup2.eventmanager.dao.impl;

import com.ncgroup2.eventmanager.dao.ItemDao;
import com.ncgroup2.eventmanager.objects.ExtendedTag;
import com.ncgroup2.eventmanager.entity.Item;
import com.ncgroup2.eventmanager.entity.Tag;
import com.ncgroup2.eventmanager.mapper.ItemMapExtractor;
import com.ncgroup2.eventmanager.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Date;
import java.util.*;

@Repository
@Transactional
public class ItemDaoImpl extends JdbcDaoSupport implements ItemDao {

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }


    @Override
    public Collection<Item> getAll() {
        String sql =
                "SELECT   i.*,\n" +
                        " array_agg(DISTINCT itag.id) as itag_ids,\n" +
                        " array_agg(DISTINCT t.*) as tags,\n" +
                        " array_agg(DISTINCT r.*) as rating\n" +
                        " FROM \"Item\" i\n" +
                        " LEFT JOIN \"Item_Tag\" itag ON (itag.item_id = i.id)\t\n" +
                        " LEFT JOIN \"Tag\" t ON (itag.tag_id = t.id)\n" +
                        " LEFT JOIN \"Rating_Item\" r ON (r.item_id = i.id)\n" +
                        " GROUP BY i.id;";

        return this.getJdbcTemplate().query(sql, new ItemMapExtractor());
    }

    @Override
    public Item getById(Object id) {
        return Objects.requireNonNull(
                getEntitiesByField("i.id", id).iterator().next(),
                "Item not found");
    }

    @Override
    public Item getEntityByField(String fieldName, Object fieldValue) {
        return Objects.requireNonNull(
                getEntitiesByField(fieldName, fieldValue).iterator().next(),
                "Item not found");
    }

    @Override
    public Collection<Item> getEntitiesByField(String fieldName, Object fieldValue) {
        String sql =
                "SELECT   i.*,\n" +
                        " array_agg(DISTINCT itag.id) as itag_ids,\n" +
                        " array_agg(DISTINCT t.*) as tags,\n" +
                        " array_agg(DISTINCT r.*) as rating\n" +
                        " FROM \"Item\" i\n" +
                        " LEFT JOIN \"Item_Tag\" itag ON (itag.item_id = i.id)\t\n" +
                        " LEFT JOIN \"Tag\" t ON (itag.tag_id = t.id)\n" +
                        " LEFT JOIN \"Rating_Item\" r ON (r.item_id = i.id)\n" +
                        " WHERE " + fieldName + " = '" + fieldValue + "'" +
                        " GROUP BY i.id;";

        return this.getJdbcTemplate().query(sql, new ItemMapExtractor());
    }

    @Override
    public void update(Item item) {
        String itemUpdateSql =
                "UPDATE \"Item\"" +
                        "SET creator_customer_login = ?, name = ?, description = ?, image = ?, link = ?, due_date = ?" +
                        "WHERE id = ?::UUID";

        List<ExtendedTag> tags = item.getTags();
        if (tags != null && (!tags.isEmpty())) {
            addTags(tags, item.getId());
        }

        this.getJdbcTemplate().update(itemUpdateSql, item.getParams());
    }

    @Override
    public void updateField(Object id, String fieldName, Object fieldValue) {
        String itemUpdateSql =
                "UPDATE \"Item\"" +
                        "SET " + fieldName + " = '" + fieldValue +
                        "' WHERE id = '" + id + "'::UUID;";

        this.getJdbcTemplate().update(itemUpdateSql);
    }

    @Override
    public void delete(Object itemId) {
        String itemDeleteSql =
                "DELETE FROM \"Item\" " +
                        "WHERE id = " + itemId + "::UUID;";

        this.getJdbcTemplate().update(itemDeleteSql);

    }

    @Override
    public void deleteItems(Collection<Item> trash) {
        if (trash != null && (!trash.isEmpty())) {

            String itemDeleteSql =
                    "DELETE FROM \"Item\" " +
                            "WHERE id = ?::UUID;";

            this.getJdbcTemplate().batchUpdate(
                    itemDeleteSql, trash, trash.size(),
                    (ps, item) -> {
                        ps.setString(1, item.getId());
                    });
        }

    }

    public void deleteTags(Collection<ExtendedTag> trash) {
        if (trash != null && (!trash.isEmpty())) {

            String tagDeleteSql =
                    "DELETE FROM \"Item_Tag\" " +
                            "WHERE id = ?::UUID; ";

            this.getJdbcTemplate().batchUpdate(
                    tagDeleteSql, trash, trash.size(),
                    (ps, tagDto) -> {
                        ps.setString(1, tagDto.getItemTagId());
                    });

            String decreaseTagCount =
                    "UPDATE \"Tag\" " +
                            "SET count = \"Tag\".count-1 " +
                            "WHERE id = ?::UUID; ";

            this.getJdbcTemplate().batchUpdate(
                    decreaseTagCount, trash, trash.size(),
                    (ps, tagDto) -> {
                        ps.setString(1, tagDto.getTag().getId());
                    });
        }
    }

    @Override
    public void create(Item item) {
        String itemInsertSql =
                "INSERT INTO \"Item\"" +
                        "(creator_customer_login, name, description, image, link, due_date, id)" +
                        "VALUES (?,?,?,?,?,?,?::UUID)";

        this.getJdbcTemplate().update(itemInsertSql, item.getParams());

        List<ExtendedTag> tags = item.getTags();
        if (tags != null && (!tags.isEmpty())) {
            addTags(tags, item.getId());
        }
    }

    public void createItems(Collection<Item> items) {
        if (items != null && (!items.isEmpty())) {

            String itemsInsertSql =
                    "INSERT INTO \"Item\"" +
                            "(creator_customer_login,name, description, image, link, due_date, id)" +
                            "VALUES (?,?,?,?,?,?,?::UUID) ";

            this.getJdbcTemplate().batchUpdate(
                    itemsInsertSql, items, items.size(),
                    (ps, item) -> {
                        ps.setString(1, item.getCreator_customer_login());
                        ps.setString(2, item.getName());
                        ps.setString(3, item.getDescription());
                        ps.setString(4, item.getImage());
                        ps.setString(5, item.getLink());
                        ps.setDate(6, Date.valueOf(item.getDueDate()));
                        ps.setString(7, item.getId());
                    });

            for (Item item : items) {
                List<ExtendedTag> tags = item.getTags();
                if (tags != null && (!tags.isEmpty())) {
                    addTags(tags, item.getId());
                }
            }
        }
    }

    public void addTags(Collection<ExtendedTag> tags, Object item_id) {
        if (tags != null && (!tags.isEmpty())) {

            String tagsInsertSql =
                    "INSERT INTO \"Tag\"" +
                            " (id, name) " +
                            "VALUES (?::UUID, ?)" +
                            "ON CONFLICT (name) DO UPDATE SET count = \"Tag\".count + 1 " +
                            "RETURNING id;";

            this.getJdbcTemplate().batchUpdate(
                    tagsInsertSql, tags, tags.size(),
                    (ps, tag) -> {
                        Tag tag1 = tag.getTag();
                        ps.setString(1, tag1.getId());
                        ps.setString(2, tag1.getName());
                    });

            String itemTagInsertSql =
                    "INSERT INTO \"Item_Tag\"" +
                            "(tag_id, item_id) " +
                            "VALUES ((SELECT id FROM \"Tag\" WHERE name = ?)::uuid, '" + item_id + "'::uuid);";


            this.getJdbcTemplate().batchUpdate(
                    itemTagInsertSql, tags, tags.size(),
                    (ps, tag) -> ps.setString(1, tag.getTag().getName()));
        }
    }

    public void updateRating(Object id, Object customerLogin) {
        String ratingUp =
                "INSERT INTO public.\"Rating_Item\" " +
                        "(customer_login, item_id)\n" +
                        "VALUES (?, ?::UUID);";

        String ratingDown =
                "DELETE FROM \"Rating_Item\" " +
                        "WHERE id = ?::UUID;";

        if (customerLogin == null) {
            Object[] params = {id};
            this.getJdbcTemplate().update(ratingDown, params);
        } else {
            Object[] params = {customerLogin, id};
            this.getJdbcTemplate().update(ratingUp, params);
        }
    }
}

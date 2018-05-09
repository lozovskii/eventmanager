package com.ncgroup2.eventmanager.dao.impl;

import com.ncgroup2.eventmanager.dao.DAO;
import com.ncgroup2.eventmanager.dto.ItemTagDto;
import com.ncgroup2.eventmanager.entity.Entity;
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
public class ItemDaoImpl extends JdbcDaoSupport implements DAO {

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }


    @Override
    public Collection<Item> getAll() {

        String sql =
                "SELECT  i.*," +
                        "t.id AS tag_id," +
                        "t.name AS tag_name," +
                        "t.count AS tag_count " +
                        "FROM \"Item\" i " +
                        "INNER JOIN \"Item_Tag\" it ON (it.item_id = i.id) " +
                        "INNER JOIN \"Tag\" t ON (it.tag_id = t.id) " +
                        "ORDER BY t.count DESC";

        Map<Item, List<ItemTagDto>> itemMap =
                this.getJdbcTemplate().query(sql, new ItemMapExtractor());

        return itemMap.isEmpty() ?
                null : Mapper.mapItemToCollection(itemMap);
    }

    @Override
    public Item getById(Object id) {

        return Objects.requireNonNull(
                getEntitiesByField("i.id", id).iterator().next(),
                "Object not found");

    }

    @Override
    public Item getEntityByField(String fieldName, Object fieldValue) {

        return Objects.requireNonNull(
                getEntitiesByField(fieldName, fieldValue).iterator().next(),
                "Item not found"
                );
    }

    @Override
    public Collection<Item> getEntitiesByField(String fieldName, Object fieldValue) {

        String sql =
                "SELECT  it.id AS item_tag_id," +
                        "i.*," +
                        "t.id AS tag_id," +
                        "t.name AS tag_name," +
                        "t.count AS tag_count " +
                        "FROM \"Item\" i\n" +
                        "INNER JOIN \"Item_Tag\" it ON (it.item_id = i.id)\t\n" +
                        "INNER JOIN \"Tag\" t ON (it.tag_id = t.id)\t" +
                        "WHERE " + fieldName + " = '" + fieldValue + "'::UUID " +
                        "ORDER BY t.count desc";

        Map<Item, List<ItemTagDto>> itemMap =
                this.getJdbcTemplate().query(sql, new ItemMapExtractor());

        return itemMap.isEmpty() ?
                null : Mapper.mapItemToCollection(itemMap);
    }

    @Override
    public void update(Entity entity) {

        Item item = (Item) entity;

        String itemUpdateSql =
                "UPDATE \"Item\"" +
                        "SET name = ?, description = ?, image = ?, link = ?, due_date = ?" +
                        "WHERE id = '" + item.getId() + "'::UUID";

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

    public void deleteTags(List<ItemTagDto> trash) {

        if (trash != null && (!trash.isEmpty())) {

            String tagDeleteSql =
                    "DELETE FROM \"Item_Tag\" " +
                            "WHERE id = ?::UUID";

            String decreaseTagCount =
                    "UPDATE \"Tag\"" +
                            "SET count = \"Tag\".count-1" +
                            "WHERE id = ?::UUID";

            String sql = tagDeleteSql + decreaseTagCount;

            this.getJdbcTemplate().batchUpdate(
                    sql, trash, trash.size(),
                    (ps, tagDto) -> {
                            ps.setString(1, tagDto.getItemTagId());
                            ps.setString(2, tagDto.getTag().getId());
                        });
        }
    }

    @Override
    public void create(Entity entity) {

        Item item = (Item) entity;

        String itemInsertSql =
                "INSERT INTO \"Item\"" +
                        "(name, description, image, link, due_date, id)" +
                        "VALUES (?,?,?,?,?,?::UUID) ;";

        this.getJdbcTemplate().update(itemInsertSql, item.getParams());

        List<ItemTagDto> tags = item.getTags();

        if (tags != null && (!tags.isEmpty())) {

            addTags(tags, item.getId());
        }

    }

    public void createItems(List<Item> items){

        if (items != null && (!items.isEmpty())){

            String itemsInsertSql =
                    "INSERT INTO \"Item\"" +
                            "(name, description, image, link, due_date, id)" +
                            "VALUES (?,?,?,?,?,?::UUID) " +
                            "ON CONFLICT DO NOTHING;";

            this.getJdbcTemplate().batchUpdate(
                    itemsInsertSql, items, items.size(),

                    (ps, item) -> {
                        ps.setString(1, item.getName());
                        ps.setString(2, item.getDescription());
                        ps.setString(3, item.getImage());
                        ps.setString(4, item.getLink());
                        ps.setDate(5,   Date.valueOf(item.getDueDate()));
                        ps.setString(6, item.getId());
                    });

            for (Item item : items) {

                List<ItemTagDto> tags = item.getTags();

                if (tags != null && (!tags.isEmpty())) {

                    addTags(tags, item.getId());
                }
            }
        }

    }

    public void addTags(List<ItemTagDto> tags, Object item_id) {

        if (tags != null && (!tags.isEmpty())) {

            String tagsInsertSql =
                    "INSERT INTO \"Tag\"" +
                            " (id, name) " +
                            "VALUES (?::UUID, ?)" +
                            "ON CONFLICT (name) DO UPDATE SET count = \"Tag\".count + 1 " +
                            "RETURNING id;";

//TODO: BatchUpdate doesn't return conflicted id's

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
// See TOD0                   "VALUES (?::uuid, '"+ item.getId() + "'::uuid);";
                            "VALUES ((SELECT id FROM \"Tag\" WHERE name = ?)::uuid, '" + item_id + "'::uuid);";


            this.getJdbcTemplate().batchUpdate(
                    itemTagInsertSql, tags, tags.size(),
                    (ps, tag) -> ps.setString(1, tag.getTag().getName()));
        }
    }
}

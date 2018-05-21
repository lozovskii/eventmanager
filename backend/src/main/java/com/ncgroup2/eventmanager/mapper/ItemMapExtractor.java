package com.ncgroup2.eventmanager.mapper;

import com.ncgroup2.eventmanager.objects.ExtendedTag;
import com.ncgroup2.eventmanager.entity.Item;
import com.ncgroup2.eventmanager.entity.Tag;
import com.ncgroup2.eventmanager.objects.Item_Rater;
import com.ncgroup2.eventmanager.util.Mapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ItemMapExtractor implements ResultSetExtractor<Collection<Item>> {

    @Override
    public Collection<Item> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Collection<Item> items = new ArrayList<>();
        int rowNum = 0;

        while (rs.next()) {
            List<ExtendedTag> extendedTagList = new ArrayList<>();

            Item item = new ItemMapper().mapRow(rs, rowNum);

            Array tagsSql = rs.getArray("tags");
            Object[] tagsSqlArray = (Object[]) tagsSql.getArray();

            if (tagsSqlArray != null && tagsSqlArray[0] != null) {
                List<Tag> tags = new ArrayList<>(Mapper.mapTagObjectsToList(tagsSqlArray));

                Array itemTagSql = rs.getArray("itag_ids");
                Object[] itemTagArray = (Object[]) itemTagSql.getArray();
                String[] itemTagId = Mapper.mapObjectsToStringArray(itemTagArray);

                int i = 0;
                for (Tag tag : tags) {
                    ExtendedTag extendedTag = new ExtendedTag();
                    extendedTag.setTag(tag);
                    extendedTag.setItemTagId(itemTagId[i++]);
                    extendedTagList.add(extendedTag);
                }
                item.setTags(extendedTagList);
            }

            Array ratingSql = rs.getArray("rating");
            Object[] itemRatersArray = (Object[]) ratingSql.getArray();

            if (itemRatersArray != null && itemRatersArray[0] != null) {
                List<Item_Rater> raters = new ArrayList<>(Mapper.mapRaterObjectsToList(itemRatersArray));
                item.setRaters(raters);
            }
            items.add(item);
        }
        return items;
    }
}

package com.ncgroup2.eventmanager.mapper;

import com.ncgroup2.eventmanager.objects.ExtendedTag;
import com.ncgroup2.eventmanager.entity.Item;
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
            Item item = new ItemMapper().mapRow(rs, rowNum++);

            Array tagsSql = rs.getArray("tags");

            if (tagsSql != null) {
                Object[] tagsSqlArray = (Object[]) tagsSql.getArray();
                assert item != null;
                item.setTags(getExtTags(tagsSqlArray));
            }

            Array ratingSql = rs.getArray("rating");

            if (ratingSql != null){
                Object[] itemRatersArray = (Object[]) ratingSql.getArray();
                assert item != null;
                item.setRaters(getRaters(itemRatersArray));
            }
            items.add(item);
        }
        return items;
    }

    List<ExtendedTag> getExtTags(Object[] tagsSqlArray) {

        return (tagsSqlArray != null && tagsSqlArray[0] != null) ?
                new ArrayList<>(Mapper.getTags(tagsSqlArray)) :
                null;
    }

    List<Item_Rater> getRaters(Object[] itemRatersArray) {

        return (itemRatersArray != null && itemRatersArray[0] != null) ?
                new ArrayList<>(Mapper.getRaters(itemRatersArray)) :
                null;
    }
}
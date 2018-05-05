package com.ncgroup2.eventmanager.mapper;

import com.ncgroup2.eventmanager.dto.ItemTagDto;
import com.ncgroup2.eventmanager.entity.Item;
import com.ncgroup2.eventmanager.entity.Tag;
import org.jetbrains.annotations.Nullable;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ItemMapExtractor implements ResultSetExtractor<Map<Item, List<ItemTagDto>>> {

    @Nullable
    @Override
    public Map<Item, List<ItemTagDto>> extractData(ResultSet rs) throws SQLException, DataAccessException {

        Map<Item, List<ItemTagDto>> itemMap = new HashMap<>();

        int rowNum = 0;

        while (rs.next()) {

            Item item = new ItemMapper().mapRow(rs,rowNum);

            Tag tag = new TagMapper().mapRow(rs, rowNum++);

            ItemTagDto tagDto = new ItemTagDto();

            tagDto.setTag(tag);

            tagDto.setItemTagId(rs.getString("item_tag_id"));

            List<ItemTagDto> tags = itemMap.get(item);

            if (tags == null) {

                List<ItemTagDto> newTags = new ArrayList<>();

                newTags.add(tagDto);

                itemMap.put(item, newTags);

            } else {

                tags.add(tagDto);
            }

        }

        return itemMap;
    }
}

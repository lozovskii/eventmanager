package com.ncgroup2.eventmanager.mapper;

import com.ncgroup2.eventmanager.entity.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TagMapper implements RowMapper<Tag> {

    @Nullable
    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {

        Tag tag = new Tag();

        tag.setId(rs.getString("tag_id"));

        tag.setName(rs.getString("tag_name"));

        tag.setCount(rs.getInt("tag_count"));

        return tag;
    }
}


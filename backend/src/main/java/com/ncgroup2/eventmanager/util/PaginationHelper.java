package com.ncgroup2.eventmanager.util;

import com.ncgroup2.eventmanager.entity.Page;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public class PaginationHelper<E> {
    public Page<E> getPage(
            JdbcTemplate jdbcTemplate,
            String countRows,
            String getRows,
            Object args[],
            int pageNo,
            int pageSize,
            RowMapper<E> rowMapper) {

        int rowCount = jdbcTemplate.queryForObject(countRows, args, Integer.class);

        int pageCount = rowCount / pageSize;

        if (rowCount > pageSize * pageCount) {
            pageCount++;
        }

        Page<E> page = new Page<>();
        page.setPageNumber(pageNo);
        page.setPagesAvailable(pageCount);

        int startRow = (pageNo - 1) * pageSize;

        jdbcTemplate.query(getRows, args, (ResultSetExtractor<Object>) resultSet -> {
            List<E> pageItems = page.getPageItems();
            int currentRow = 0;

            while (resultSet.next() && currentRow < startRow + pageSize) {
                if (currentRow >= startRow) {
                    pageItems.add(rowMapper.mapRow(resultSet, currentRow));
                }

                currentRow++;
            }

            return pageItems;
        });

        return page;
    }
}
package com.ncgroup2.eventmanager.mapper;

import com.ncgroup2.eventmanager.objects.WishListItem;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WishListMapExtractor implements ResultSetExtractor<Map<String, List<WishListItem>>> {

    private String wishListId;

    public WishListMapExtractor() {
        this.wishListId = "event_id";
    }

    public WishListMapExtractor(String wishListIdType) {
        if (wishListIdType.equals("ew.id"))
            this.wishListId = "event_wishlist_id";
        else
            this.wishListId = wishListIdType;
    }

    @Override
    public Map<String, List<WishListItem>> extractData(ResultSet rs) throws SQLException {
        Map<String, List<WishListItem>> wishListsMap = new HashMap<>();
        int rowNum = 0;

        while (rs.next()) {
            String wishListId = rs.getString(this.wishListId);
            WishListItem item = new WishListItem();
            item.setEvent_wishlist_id(rs.getString("event_wishlist_id"));
            item.setEvent_id(rs.getString("event_id"));
            item.setItem_wishlist_id(rs.getString("item_wishlist_id"));
            item.setBooker_customer_login(rs.getString("booker_customer_login"));
            item.setPriority(rs.getInt("priority"));

            ItemMapper itemMapper = new ItemMapper();
            item.setItem(itemMapper.mapRow(rs, rowNum++));

            List<WishListItem> items = wishListsMap.get(wishListId);
            if (items == null) {
                List<WishListItem> newItems = new ArrayList<>();
                newItems.add(item);
                wishListsMap.put(wishListId, newItems);
            } else {
                items.add(item);
            }
        }
        return wishListsMap;
    }
}
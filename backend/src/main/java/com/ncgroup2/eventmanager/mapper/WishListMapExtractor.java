package com.ncgroup2.eventmanager.mapper;

import com.ncgroup2.eventmanager.entity.Item;
import com.ncgroup2.eventmanager.entity.WishList;
import com.ncgroup2.eventmanager.objects.WishListItem;
import com.ncgroup2.eventmanager.util.Mapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.lang.Nullable;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class WishListMapExtractor implements ResultSetExtractor<Collection<WishList>> {

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

    @Nullable
    @Override
    public Collection<WishList> extractData(ResultSet rs) throws SQLException {
        Map<String, List<WishListItem>> wishListsMap = new HashMap<>();
        int rowNum = 0;

        while (rs.next()) {
            String wishListId = rs.getString(this.wishListId);
            WishListItem wishListItem = new WishListItem();
            wishListItem.setEvent_wishlist_id(rs.getString("event_wishlist_id"));
            wishListItem.setEvent_id(rs.getString("event_id"));
            wishListItem.setItem_wishlist_id(rs.getString("item_wishlist_id"));
            wishListItem.setBooker_customer_login(rs.getString("booker_customer_login"));
            wishListItem.setPriority(rs.getInt("priority"));

            Item item = new ItemMapper().mapRow(rs, rowNum);

            Array tagsSql = rs.getArray("tags");
            if (tagsSql != null) {
                Object[] tagsSqlArray = (Object[]) tagsSql.getArray();
                item.setTags(new ItemMapExtractor().getExtTags(tagsSqlArray));
            }

            Array ratingSql = rs.getArray("rating");
            if (ratingSql != null) {
                Object[] itemRatersArray = (Object[]) ratingSql.getArray();
                item.setRaters(new ItemMapExtractor().getRaters(itemRatersArray));
            }

            wishListItem.setItem(item);

            List<WishListItem> items = wishListsMap.get(wishListId);
            if (items == null) {
                List<WishListItem> newItems = new ArrayList<>();
                newItems.add(wishListItem);
                wishListsMap.put(wishListId, newItems);
            } else {
                items.add(wishListItem);
            }
        }

        return wishListsMap.isEmpty() ?
                null : Mapper.getWishLists(wishListsMap);
    }
}
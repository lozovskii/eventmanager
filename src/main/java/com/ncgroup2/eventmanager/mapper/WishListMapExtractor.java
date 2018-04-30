package com.ncgroup2.eventmanager.mapper;

import com.ncgroup2.eventmanager.dto.ItemWishListDto;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WishListMapExtractor implements ResultSetExtractor<Map<String, List<ItemWishListDto>>> {

    @Override
    public Map<String, List<ItemWishListDto>> extractData(ResultSet rs) throws SQLException {

        Map<String, List<ItemWishListDto>> wishListsMap = new HashMap<>();

        while (rs.next()) {

            String wishListId = rs.getString("event_id");

            ItemWishListDto item = new ItemWishListDto();

            item.setEvent_wishlist_id(rs.getString("event_wishlist_id"));

            item.setItem_wishlist_id(rs.getString("item_wishlist_id"));

            item.setBooker_customer_id(rs.getString("booker_customer_id"));

            item.setPriority(rs.getString("priority"));

            ItemMapper itemMapper = new ItemMapper();

            item.setItem(itemMapper.mapRow(rs,1));

            List<ItemWishListDto> items = wishListsMap.get(wishListId);

            if (items == null) {

                List<ItemWishListDto> newItems = new ArrayList<>();

                newItems.add(item);

                wishListsMap.put(wishListId, newItems);

            } else {

                items.add(item);
            }

        }

        return wishListsMap;

    }


    //    @Override
//    public Map<String, HashMap<String, String>> extractData(ResultSet rs) throws SQLException {
//
//        Map<String, HashMap<String, String>> wishListsMap = new HashMap<>();
//
//        while (rs.next()) {
//
//            String wishListId = rs.getString("event_id");
//
//            String item_wishlist_id = rs.getString("item_wishlist_id");
//
//            String itemId = rs.getString("item_id");
//
//            HashMap<String, String> items = wishListsMap.get(wishListId);
//
//            if (items == null) {
//
//                HashMap<String, String> newItems = new HashMap<>();
//
//                newItems.put(item_wishlist_id, itemId);
//
//                wishListsMap.put(wishListId, newItems);
//
//            } else {
//
//                items.put(item_wishlist_id, itemId);
//
//            }
//
//        }
//
//        return wishListsMap;
//
//    }
}
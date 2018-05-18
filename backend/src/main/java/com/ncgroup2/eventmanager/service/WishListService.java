package com.ncgroup2.eventmanager.service;

import com.ncgroup2.eventmanager.dto.ItemWishListDto;
import com.ncgroup2.eventmanager.entity.WishList;

import java.util.List;

public interface WishListService {

    WishList getById(String wishlist_id);

    WishList getByEventId(String event_id);

    WishList getBookedItems(String booker_customer_login);

    void create(WishList wishList);

    void updateByField(Object item_wishlist_id, String fieldName, Object fieldValue);

    void update(WishList wishList);

    void deleteItems(List<ItemWishListDto> trash);

}

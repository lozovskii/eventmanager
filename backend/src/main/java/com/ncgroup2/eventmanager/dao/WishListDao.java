package com.ncgroup2.eventmanager.dao;

import com.ncgroup2.eventmanager.dto.ItemWishListDto;
import com.ncgroup2.eventmanager.entity.WishList;

import java.util.List;

public interface WishListDao extends DAO<WishList, Object> {

    void deleteItems(List<ItemWishListDto> trash);

    void addItems(WishList wishList);
}

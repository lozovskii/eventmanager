package com.ncgroup2.eventmanager.dao;

import com.ncgroup2.eventmanager.objects.WishListItem;
import com.ncgroup2.eventmanager.entity.WishList;

import java.util.List;

public interface WishListDao extends DAO<WishList, Object> {

    void deleteItems(List<WishListItem> trash);

    void addItems(WishList wishList);
}

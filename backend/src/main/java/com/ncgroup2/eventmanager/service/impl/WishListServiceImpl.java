package com.ncgroup2.eventmanager.service.impl;

import com.ncgroup2.eventmanager.dao.impl.WishListDaoImpl;
import com.ncgroup2.eventmanager.entity.WishList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WishListServiceImpl {

    @Autowired
    private WishListDaoImpl wishListDao;

    public WishList getById(String wishlist_id){

        return wishListDao.getById(wishlist_id);
    }

    public WishList getByEventId(String event_id){

        return wishListDao.getEntityByField("event_id", event_id);
    }

    public WishList getBookedItems(String booker_customer_id){

        return wishListDao.getEntityByField("booker_customer_id", booker_customer_id);
    }

    public void createWishlist(WishList wishList){

        wishListDao.create(wishList);
    }

    public void updateByField(Object item_wishlist_id, String fieldName, Object fieldValue){

        wishListDao.updateField(item_wishlist_id, fieldName, fieldValue);
    }
}

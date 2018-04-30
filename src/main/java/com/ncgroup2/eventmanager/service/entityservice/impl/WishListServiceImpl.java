package com.ncgroup2.eventmanager.service.entityservice.impl;

import com.ncgroup2.eventmanager.dao.impl.postgres.WishListDaoImpl;
import com.ncgroup2.eventmanager.entity.WishList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WishListServiceImpl {

    @Autowired
    private WishListDaoImpl wishListDao;

    public WishList getWishListByEventId(){

        return wishListDao.getEntityByField("ew.event_id", "edc215f0-4afb-11e8-a311-b3e80f6788de");

    }

    public WishList getWishListByBookerId(){
        return wishListDao.getEntityByField("iw.booker_customer_id", "9bcbdf60-4afb-11e8-88dc-7b08ea7947a1");
    }
}

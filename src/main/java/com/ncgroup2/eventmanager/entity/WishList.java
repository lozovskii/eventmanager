package com.ncgroup2.eventmanager.entity;

import com.ncgroup2.eventmanager.dto.ItemWishListDto;
import lombok.Data;

import java.util.List;

@Data
public class WishList extends Entity{

//    // Key = Item_WishList.id
//    // Value = Item.id
//    LinkedHashMap<String, String> items;
//
//    // Key = Item_WishList.booker_customer_id
//    // Value = Event_WishList.id
//    LinkedHashMap<String, String> bookers;

    List<ItemWishListDto> items;

    @Override
    public Object[] getParams() {

        return new Object[]{
                this.getId()
        };
    }
}

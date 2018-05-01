package com.ncgroup2.eventmanager.dto;

import com.ncgroup2.eventmanager.entity.Item;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ItemWishListDto {

    Item item;

    String item_wishlist_id;

    String event_wishlist_id;

    String event_id;

    String booker_customer_id;

    String priority;

}

package com.ncgroup2.eventmanager.objects;

import com.ncgroup2.eventmanager.entity.Item;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class WishListItem {

    Item item;

    String item_wishlist_id;

    String event_wishlist_id;

    String event_id;

    String booker_customer_login;

    int priority;

    public WishListItem() {

        this.item_wishlist_id = UUID.randomUUID().toString();

        this.event_wishlist_id = UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return "WishListItem{" +
                "item=" + item.toString() +
                "\n, item_wishlist_id='" + item_wishlist_id + '\'' +
                ", event_wishlist_id='" + event_wishlist_id + '\'' +
                ", event_id='" + event_id + '\'' +
                ", booker_customer_login='" + booker_customer_login + '\'' +
                ", priority=" + priority +
                '}';
    }
}

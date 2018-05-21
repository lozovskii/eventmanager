package com.ncgroup2.eventmanager.entity;

import com.ncgroup2.eventmanager.objects.WishListItem;
import lombok.Data;

import java.util.List;

@Data
public class WishList extends Entity{

    List<WishListItem> items;

    @Override
    public Object[] getParams() {

        return new Object[]{
                this.getId()
        };
    }
}
